package sk.stuba.fiit.perconik.core.resources;

import sk.stuba.fiit.perconik.core.UnsupportedResourceException;
import sk.stuba.fiit.perconik.core.listeners.CommandManagerListener;

@Unimplemented
enum CommandManagerHandler implements Handler<CommandManagerListener>
{
	INSTANCE;
	
	public final void register(final CommandManagerListener listener)
	{
		throw new UnsupportedResourceException("Not implemented yet");
		
//		final Runnable addListener = new Runnable()
//		{
//			@Override
//			public final void run()
//			{
//			}
//		};
//	
//		Display.getDefault().asyncExec(addListener);
	}

	public final void unregister(final CommandManagerListener listener)
	{
		throw new UnsupportedResourceException("Not implemented yet");
	}
}
