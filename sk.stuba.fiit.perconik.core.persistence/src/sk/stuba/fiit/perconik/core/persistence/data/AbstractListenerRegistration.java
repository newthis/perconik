package sk.stuba.fiit.perconik.core.persistence.data;

import javax.annotation.Nullable;
import sk.stuba.fiit.perconik.core.Listeners;
import sk.stuba.fiit.perconik.core.persistence.ListenerRegistration;
import sk.stuba.fiit.perconik.core.services.Services;

public abstract class AbstractListenerRegistration implements ListenerRegistration
{
	protected AbstractListenerRegistration()
	{
	}

	@Override
	public final boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		
		if (!(o instanceof ListenerRegistration))
		{
			return false;
		}

		ListenerRegistration other = (ListenerRegistration) o;

		return this.getListenerClass() == other.getListenerClass();
	}

	@Override
	public final int hashCode()
	{
		return this.getListenerClass().hashCode();
	}
	
	@Override
	public final String toString()
	{
		return Utilities.toString(this);
	}
	
	public final boolean isRegistered()
	{
		return Listeners.isRegistered(this.getListenerClass());
	}
	
	public final boolean isProvided()
	{
		return Services.getListenerService().getListenerProvider().classes().contains(this.getListenerClass());
	}
}
