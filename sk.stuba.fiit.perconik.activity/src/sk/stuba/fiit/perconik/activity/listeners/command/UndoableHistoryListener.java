package sk.stuba.fiit.perconik.activity.listeners.command;

import com.google.common.base.Optional;

import org.eclipse.core.commands.operations.OperationHistoryEvent;

import sk.stuba.fiit.perconik.activity.events.Event;
import sk.stuba.fiit.perconik.activity.events.LocalEvent;
import sk.stuba.fiit.perconik.core.annotations.Version;
import sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType;

import static java.util.Objects.requireNonNull;

import static sk.stuba.fiit.perconik.activity.listeners.command.UndoableHistoryListener.Action.fromType;
import static sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType.OPERATION_ADDED;
import static sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType.OPERATION_CHANGED;
import static sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType.OPERATION_NOT_OK;
import static sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType.OPERATION_REMOVED;
import static sk.stuba.fiit.perconik.eclipse.core.commands.operations.OperationHistoryEventType.valueOf;

/**
 * TODO
 *
 * @author Pavol Zbell
 * @since 1.0
 */
@Version("0.0.0.alpha")
public final class UndoableHistoryListener extends AbstractUndoableListener {
  public UndoableHistoryListener() {}

  enum Action {
    ADD(OPERATION_ADDED),

    REMOVE(OPERATION_REMOVED),

    FAIL(OPERATION_NOT_OK),

    CHANGE(OPERATION_CHANGED);

    final String name;

    final String path;

    final OperationHistoryEventType type;

    private Action(final OperationHistoryEventType type) {
      this.name = actionName("eclipse", "command", "history", this);
      this.path = actionPath(this.name);
      this.type = requireNonNull(type);
    }

    static Optional<Action> fromType(final OperationHistoryEventType type) {
      for (Action action: values()) {
        if (action.type == type) {
          return Optional.of(action);
        }
      }

      return Optional.absent();
    }
  }

  static Event build(final long time, final Action action, final OperationHistoryEvent event) {
    Event data = LocalEvent.of(time, action.name);

    put(data, event);

    return data;
  }

  @Override
  void process(final long time, final OperationHistoryEvent event) {
    Optional<Action> action = fromType(valueOf(event.getEventType()));

    if (action.isPresent()) {
      this.send(action.get().path, build(time, action.get(), event));
    }
  }
}