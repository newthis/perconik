package sk.stuba.fiit.perconik.preferences.plugin;

import org.osgi.framework.BundleContext;
import sk.stuba.fiit.perconik.eclipse.ui.plugin.AbstractPlugin;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Pavol Zbell
 * @version 0.0.1
 */
public class Activator extends AbstractPlugin
{
	/**
	 * The plug-in identifier.
	 */
	public static final String PLUGIN_ID = "sk.stuba.fiit.perconik.preferences";

	/**
	 * The shared instance.
	 */
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator()
	{
	}

	/**
	 * Gets the shared instance.
	 * 
	 * @return Returns the shared instance.
	 */
	public static final Activator getDefault()
	{
		return plugin;
	}

	@Override
	public final void start(final BundleContext context) throws Exception
	{
		super.start(context);

		plugin = this;
	}

	@Override
	public final void stop(final BundleContext context) throws Exception
	{
		plugin = null;

		super.stop(context);
	}
}