package sk.stuba.fiit.perconik.core.resources;

import sk.stuba.fiit.perconik.core.listeners.CompletionListener;
import sk.stuba.fiit.perconik.core.resources.CompletionHook.Support;

enum CompletionHandler implements Handler<CompletionListener> {
  INSTANCE;

  private final Support support = new Support();

  public void register(final CompletionListener listener) {
    this.support.hook(DefaultResources.getPartResource(), listener);
  }

  public void unregister(final CompletionListener listener) {
    this.support.unhook(DefaultResources.getPartResource(), listener);
  }
}
