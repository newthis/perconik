package com.gratex.perconik.uaca;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

import com.gratex.perconik.uaca.plugin.Activator;
import com.gratex.perconik.uaca.preferences.UacaOptions;
import com.gratex.perconik.uaca.preferences.UacaPreferences;

import sk.stuba.fiit.perconik.eclipse.core.runtime.ForwardingPluginConsole;
import sk.stuba.fiit.perconik.eclipse.core.runtime.PluginConsole;
import sk.stuba.fiit.perconik.utilities.configuration.Configurable;
import sk.stuba.fiit.perconik.utilities.time.TimeSource;

import static com.google.common.base.Preconditions.checkNotNull;

import static sk.stuba.fiit.perconik.utilities.SmartStringBuilder.builder;
import static sk.stuba.fiit.perconik.utilities.time.TimeSource.systemTimeSource;

public final class UacaConsole extends ForwardingPluginConsole implements Configurable {
  private static final UacaConsole shared = new UacaConsole(UacaPreferences.getShared(), systemTimeSource());

  private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private final PluginConsole delegate;

  private final UacaOptions options;

  private final TimeSource source;

  private UacaConsole(final UacaOptions options, final TimeSource source) {
    this.delegate = Activator.defaultInstance().getConsole();
    this.options = checkNotNull(options);
    this.source = checkNotNull(source);
  }

  public static UacaConsole create(final UacaOptions options) {
    return create(options, systemTimeSource());
  }

  public static UacaConsole create(final UacaOptions options, final TimeSource source) {
    return new UacaConsole(options, source);
  }

  public static UacaConsole getShared() {
    return shared;
  }

  @Override
  protected PluginConsole delegate() {
    return this.delegate;
  }

  private String hook(final String message) {
    Date time = new Date(this.source.read());

    return builder().format(format, time).append(" ").append(message).toString();
  }

  @Override
  public PluginConsole append(@Nullable final CharSequence s) {
    return super.append(this.hook(String.valueOf(s)));
  }

  @Override
  public PluginConsole append(@Nullable final CharSequence s, final int from, final int to) {
    return super.append(this.hook(String.valueOf(s).substring(from, to)));
  }

  @Override
  public PluginConsole append(final char c) {
    return super.append(this.hook(String.valueOf(c)));
  }

  @Override
  public void put(final @Nullable String message) {
    super.put(this.hook(String.format(String.valueOf(message))));
  }

  @Override
  public void put(final String format, final Object ... args) {
    super.put(this.hook(String.format(format, args)));
  }

  @Override
  public void print(final @Nullable String message) {
    super.print(this.hook(String.format(String.valueOf(message))));
  }

  @Override
  public void print(final String format, final Object ... args) {
    super.print(this.hook(String.format(format, args)));
  }

  public UacaOptions getOptions() {
    return this.options;
  }
}
