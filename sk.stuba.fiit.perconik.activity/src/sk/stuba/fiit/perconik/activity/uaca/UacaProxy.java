package sk.stuba.fiit.perconik.activity.uaca;

import javax.annotation.Nullable;

import com.gratex.perconik.uaca.SharedUacaProxy;
import com.gratex.perconik.uaca.preferences.UacaOptions;

import sk.stuba.fiit.perconik.data.content.Content;
import sk.stuba.fiit.perconik.data.store.Store;
import sk.stuba.fiit.perconik.utilities.time.TimeSource;

public final class UacaProxy extends SharedUacaProxy implements Store<Object> {
  public UacaProxy() {}

  public UacaProxy(final UacaOptions options) {
    super(options);
  }

  public UacaProxy(final UacaOptions options, final TimeSource source) {
    super(options, source);
  }

  public Content load(final String path, @Nullable final Object request) {
    throw new UnsupportedOperationException();
  }

  public void save(final String path, @Nullable final Object resource) {
    this.send("generic/event", UacaEvent.of(path, resource));
  }
}
