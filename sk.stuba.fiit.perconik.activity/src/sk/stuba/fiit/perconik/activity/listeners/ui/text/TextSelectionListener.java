package sk.stuba.fiit.perconik.activity.listeners.ui.text;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.GuardedBy;

import com.google.common.base.Stopwatch;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import sk.stuba.fiit.perconik.activity.events.Event;
import sk.stuba.fiit.perconik.activity.events.LocalEvent;
import sk.stuba.fiit.perconik.activity.serializers.ui.selection.TextSelectionSerializer;
import sk.stuba.fiit.perconik.core.annotations.Version;
import sk.stuba.fiit.perconik.eclipse.jdt.ui.UnderlyingView;
import sk.stuba.fiit.perconik.eclipse.jface.text.LineRegion;

import static com.google.common.collect.Lists.newLinkedList;

import static sk.stuba.fiit.perconik.activity.listeners.ui.text.TextSelectionListener.Action.SELECT;
import static sk.stuba.fiit.perconik.data.content.StructuredContents.key;

/**
 * TODO
 *
 * @author Pavol Zbell
 * @since 1.0
 */
@Version("0.0.0.alpha")
public final class TextSelectionListener extends AbstractTextOperationListener implements sk.stuba.fiit.perconik.core.listeners.TextSelectionListener {
  static final long selectionEventWindow = 500;

  private final Object lock = new Object();

  @GuardedBy("lock")
  private final Stopwatch watch;

  @GuardedBy("lock")
  private LinkedList<TextSelectionEvent> continuousSelections;

  @GuardedBy("lock")
  private TextSelectionEvent lastSentSelection;

  public TextSelectionListener() {
    this.watch = Stopwatch.createUnstarted();
  }

  enum Action {
    SELECT;

    final String name;

    final String path;

    private Action() {
      this.name = actionName("eclipse", "ui", "text", this);
      this.path = actionPath(this.name);
    }
  }

  static Event build(final long time, final Action action, final IEditorPart editor, final UnderlyingView<?> view, final LineRegion region, final ITextSelection selection) {
    Event data = LocalEvent.of(time, action.name);

    put(data, editor);
    put(data, view);
    put(data, region);

    data.put(key("selection"), new TextSelectionSerializer().serialize(selection));

    return data;
  }

  void process(final long time, final Action action, final IWorkbenchPart part, final ITextSelection selection) {
    if (!(part instanceof IEditorPart)) {
      return;
    }

    IEditorPart editor = (IEditorPart) part;

    UnderlyingView<?> view = UnderlyingView.from(editor);

    if (view == null) {
      return;
    }

    LineRegion region = LineRegion.of(view.getDocument(), selection.getOffset(), selection.getLength(), selection.getText());

    this.send(action.path, build(time, action, editor, view, region, selection));
  }

  @Override
  public void preUnregister() {
    synchronized (this.lock) {
      if (this.watch.isRunning()) {
        this.stopWatchAndProcessLastSelectionEvent();
      }
    }
  }

  @GuardedBy("lock")
  private void startWatchAndClearSelectionEvents() {
    assert !this.watch.isRunning() && this.continuousSelections == null;

    this.continuousSelections = newLinkedList();

    this.watch.reset().start();
  }

  @GuardedBy("lock")
  private void stopWatchAndProcessLastSelectionEvent() {
    assert this.watch.isRunning() && this.continuousSelections != null;

    this.lastSentSelection = this.continuousSelections.getLast();

    selectionChanged(this.lastSentSelection);

    this.continuousSelections = null;

    this.watch.stop();
  }

  public void selectionChanged(final IWorkbenchPart part, final ITextSelection selection) {
    final long time = currentTime();

    if (selection.isEmpty()) {
      return;
    }

    synchronized (this.lock) {
      TextSelectionEvent event = new TextSelectionEvent(time, part, selection);

      boolean empty = event.isSelectionTextEmpty();

      if (empty && (this.lastSentSelection == null || this.lastSentSelection.part != part)) {
        return;
      }

      if (this.lastSentSelection != null && this.lastSentSelection.contentEquals(event)) {
        return;
      }

      if (this.watch.isRunning() && !this.continuousSelections.getLast().isContinuousWith(event)) {
        if (Log.isEnabled()) {
          Log.message("selection: watch running but selection not continuous%n").appendTo(this.log);
        }

        this.stopWatchAndProcessLastSelectionEvent();
      }

      if (!this.watch.isRunning()) {
        if (Log.isEnabled()) {
          Log.message("selection: watch not running%n").appendTo(this.log);
        }

        this.startWatchAndClearSelectionEvents();
      }

      long delta = this.watch.elapsed(TimeUnit.MILLISECONDS);

      this.continuousSelections.add(event);

      if (!empty && delta < selectionEventWindow) {
        if (Log.isEnabled()) {
          Log.message("selection: ignore %d < %d%n", delta, selectionEventWindow).appendTo(this.log);
        }

        this.watch.reset().start();

        return;
      }

      this.stopWatchAndProcessLastSelectionEvent();
    }
  }

  private void selectionChanged(final TextSelectionEvent event) {
    this.execute(new Runnable() {
      public void run() {
        process(event.time, SELECT, event.part, event.selection);
      }
    });
  }
}
