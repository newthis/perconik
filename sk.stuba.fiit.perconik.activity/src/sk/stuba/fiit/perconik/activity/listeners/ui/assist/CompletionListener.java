package sk.stuba.fiit.perconik.activity.listeners.ui.assist;

import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import sk.stuba.fiit.perconik.activity.events.Event;
import sk.stuba.fiit.perconik.activity.events.LocalEvent;
import sk.stuba.fiit.perconik.activity.listeners.ActivityListener;
import sk.stuba.fiit.perconik.core.annotations.Unsupported;
import sk.stuba.fiit.perconik.core.annotations.Version;

import static sk.stuba.fiit.perconik.activity.listeners.ui.assist.CompletionListener.Action.APPLY;
import static sk.stuba.fiit.perconik.data.content.StructuredContents.key;

/**
 * TODO
 *
 * @author Pavol Zbell
 * @since 1.0
 */
@Version("0.0.0.alpha")
@Unsupported
public final class CompletionListener extends ActivityListener implements sk.stuba.fiit.perconik.core.listeners.CompletionListener {
  public CompletionListener() {}

  enum Action implements ActivityListener.Action {
    APPLY;

    private final String name;

    private final String path;

    private Action() {
      this.name = actionName("eclipse", "completion", this);
      this.path = actionPath(this.name);
    }

    public String getName() {
      return this.name;
    }

    public String getPath() {
      return this.path;
    }
  }

  // TODO
  //  private static void put(final StructuredContent content, ) {
  //    content.put(key(""), );
  //  }

  static Event build(final long time, final Action action, final ICompletionProposal proposal) {
    Event data = LocalEvent.of(time, action.getName());

    // ICompletionProposal
    // IJavaCompletionProposal
    // IContextInformation
    // + a lot of *Extension*

    data.put(key("proposal", "displayString"), proposal.getDisplayString());
    data.put(key("proposal", "additionalInformation"), proposal.getAdditionalProposalInfo());

    // TODO refactor
    data.put(key("proposal", "context", "displayString"), proposal.getContextInformation().getContextDisplayString());
    data.put(key("proposal", "context", "informationDisplayString"), proposal.getContextInformation().getInformationDisplayString());

    return data;
  }

  void process(final long time, final Action action, final ICompletionProposal proposal) {
    this.send(action.getPath(), build(time, action, proposal));
  }

  public void applied(final ICompletionProposal proposal) {
    final long time = this.currentTime();

    this.execute(new Runnable() {
      public void run() {
        process(time, APPLY, proposal);
      }
    });
  }

  public void assistSessionStarted(final ContentAssistEvent event) {
    // ignore
  }

  public void assistSessionRestarted(final ContentAssistEvent event) {
    // ignore
  }

  public void assistSessionEnded(final ContentAssistEvent event) {
    // ignore
  }

  public void selectionChanged(final ICompletionProposal proposal, final boolean smart) {
    // ignore
  }
}
