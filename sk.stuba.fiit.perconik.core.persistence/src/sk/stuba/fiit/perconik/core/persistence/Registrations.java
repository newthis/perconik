package sk.stuba.fiit.perconik.core.persistence;

import java.util.Set;
import sk.stuba.fiit.perconik.core.Listener;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public final class Registrations
{
	private Registrations()
	{
		throw new AssertionError();
	}
	
	public static final <R extends MarkableRegistration> Set<R> registered(final Set<R> registrations)
	{
		return selectByRegisteredStatus(registrations, true);
	}
	
	public static final <R extends MarkableRegistration> Set<R> unregistered(final Set<R> registrations)
	{
		return selectByRegisteredStatus(registrations, false);
	}

	public static final <R extends MarkableRegistration> Set<R> marked(final Set<R> registrations)
	{
		return selectByRegisteredMark(registrations, true);
	}
	
	public static final <R extends MarkableRegistration> Set<R> unmarked(final Set<R> registrations)
	{
		return selectByRegisteredMark(registrations, false);
	}

	public static final <R extends Registration> Set<R> selectByRegisteredStatus(final Set<R> registrations, final boolean status)
	{
		Set<R> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			if (registration.isRegistered() == status)
			{
				result.add(registration);
			}
		}
		
		return result;
	}

	public static final <R extends MarkableRegistration> Set<R> selectByRegisteredMark(final Set<R> registrations, final boolean status)
	{
		Set<R> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			if (registration.hasRegistredMark() == status)
			{
				result.add(registration);
			}
		}
		
		return result;		
	}
	
	public static final <R extends MarkableRegistration & RegistrationMarker<R>> Set<R> applyRegisteredMark(final Set<R> registrations)
	{
		Set<R> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			result.add(registration.applyRegisteredMark());
		}
		
		return result;
	}

	public static final <R extends MarkableRegistration & RegistrationMarker<R>> Set<R> updateRegisteredMark(final Set<R> registrations)
	{
		Set<R> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			result.add(registration.updateRegisteredMark());
		}
		
		return result;
	}

	public static final <R extends MarkableRegistration & RegistrationMarker<R>> Set<R> markRegistered(final Set<R> registrations, final boolean status)
	{
		return markRegistered(registrations, Functions.constant(status));
	}
	
	public static final <R extends MarkableRegistration & RegistrationMarker<R>> Set<R> markRegistered(final Set<R> registrations, final Function<? super R, Boolean> function)
	{
		Set<R> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			result.add(registration.markRegistered(function.apply(registration)));
		}
		
		return result;
	}
	
	public static final <R extends Registration> Set<R> supplement(final Set<? extends R> original, final Set<? extends R> supplements)
	{
		Set<R> result = Sets.newHashSet(original);
		
		for (R registration: supplements)
		{
			if (!result.contains(registration))
			{
				result.add(registration);
			}
		}
		
		return result;
	}
	
	// TODO beta
//	public static final <R extends ResourceRegistration> Map<Class<? extends Listener>, Map<String, R>> toResourceMap(final Set<R> registrations)
//	{
//		Map<Class<? extends Listener>, Map<String, R>> map = Maps.newHashMapWithExpectedSize(registrations.size());
//
//		for (R registration: registrations)
//		{
//			Class<? extends Listener> type = registration.getListenerType();
//			
//			Map<String, R> submap = map.get(type);
//			
//			if (submap == null)
//			{
//				map.put(type, submap = Maps.newHashMapWithExpectedSize(4));
//			}
//			
//			submap.put(registration.getResourceName(), registration);
//		}
//		
//		return map;
//	}
//	
//	public static final <R extends ListenerRegistration> Map<Class<? extends Listener>, R> toListenerMap(final Set<R> registrations)
//	{
//		Map<Class<? extends Listener>, R> map = Maps.newHashMapWithExpectedSize(registrations.size());
//		
//		for (R registration: registrations)
//		{
//			map.put(registration.getListenerClass(), registration);
//		}
//		
//		return map;
//	}

	public static final <R extends ResourceRegistration & MarkableRegistration> SetMultimap<Class<? extends Listener>, String> toResourceNames(final Set<R> registrations)
	{
		SetMultimap<Class<? extends Listener>, String> result = HashMultimap.create(registrations.size(), 4);
		
		for (R registration: registrations)
		{
			result.put(registration.getListenerType(), registration.getResourceName());
		}
		
		return result;
	}

	public static final <R extends ListenerRegistration & MarkableRegistration> Set<Class<? extends Listener>> toListenerClasses(final Set<R> registrations)
	{
		Set<Class<? extends Listener>> result = Sets.newHashSetWithExpectedSize(registrations.size());
		
		for (R registration: registrations)
		{
			result.add(registration.getListenerClass());
		}
		
		return result;
	}
}
