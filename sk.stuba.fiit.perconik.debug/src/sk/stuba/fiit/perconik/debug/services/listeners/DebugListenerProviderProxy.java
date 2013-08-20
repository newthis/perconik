package sk.stuba.fiit.perconik.debug.services.listeners;

import sk.stuba.fiit.perconik.core.Listener;
import sk.stuba.fiit.perconik.core.services.listeners.ListenerProvider;
import sk.stuba.fiit.perconik.debug.Debug;
import sk.stuba.fiit.perconik.debug.DebugConsole;
import sk.stuba.fiit.perconik.debug.DebugListeners;
import sk.stuba.fiit.perconik.debug.services.DebugNameableProxy;
import com.google.common.base.Preconditions;

public final class DebugListenerProviderProxy extends DebugNameableProxy implements DebugListenerProvider
{
	private final ListenerProvider provider;
	
	private DebugListenerProviderProxy(final ListenerProvider provider, final DebugConsole console)
	{
		super(console);
		
		this.provider = Preconditions.checkNotNull(provider);
	}

	public static final DebugListenerProviderProxy wrap(final ListenerProvider provider)
	{
		return wrap(provider, Debug.getDefaultConsole());
	}
	
	public static final DebugListenerProviderProxy wrap(final ListenerProvider provider, final DebugConsole console)
	{
		if (provider instanceof DebugListenerProviderProxy)
		{
			return (DebugListenerProviderProxy) provider;
		}
		
		return new DebugListenerProviderProxy(provider, console);
	}

	public static final ListenerProvider unwrap(final ListenerProvider provider)
	{
		if (provider instanceof DebugListenerProviderProxy)
		{
			return ((DebugListenerProviderProxy) provider).delegate();
		}
		
		return provider;
	}
	
	@Override
	protected final ListenerProvider delegate()
	{
		return this.provider;
	}

	public final <L extends Listener> L forClass(final Class<L> type)
	{
		this.put("Requesting listener for type %s ... ", DebugListeners.toString(type));
		
		L listener = this.delegate().forClass(type);
		
		this.print(listener != null ? "done" : "failed");
		
		return listener;
	}

	public final Class<? extends Listener> loadClass(final String name) throws ClassNotFoundException
	{
		return this.delegate().loadClass(name);
	}

	public final Iterable<Class<? extends Listener>> classes()
	{
		return this.delegate().classes();
	}
}