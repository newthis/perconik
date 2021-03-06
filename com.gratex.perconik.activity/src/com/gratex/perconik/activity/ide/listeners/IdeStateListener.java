package com.gratex.perconik.activity.ide.listeners;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;

import com.gratex.perconik.activity.uaca.IdeUacaProxy;
import com.gratex.perconik.activity.uaca.IdeUacaUris;
import com.gratex.perconik.services.uaca.ide.IdeStateChangeEventRequest;

import sk.stuba.fiit.perconik.core.listeners.LaunchListener;
import sk.stuba.fiit.perconik.core.listeners.PerspectiveListener;
import sk.stuba.fiit.perconik.eclipse.core.resources.Projects;

import static com.gratex.perconik.activity.ide.IdeData.setApplicationData;
import static com.gratex.perconik.activity.ide.IdeData.setEventData;
import static com.gratex.perconik.activity.ide.IdeData.setProjectData;
import static com.gratex.perconik.activity.ide.listeners.Utilities.currentTime;

/**
 * A listener of IDE state change events. This listener handles desired
 * events and eventually builds corresponding data transfer objects
 * of type {@link IdeStateChangeEventRequest} and passes them to the
 * {@link IdeUacaProxy} to be transferred into the <i>User Activity Central
 * Application</i> for further processing.
 *
 * <p>State changes are logged when an application launches from Eclipse,
 * or Eclipse perspective changes.
 *
 * <p>Data available in an {@code IdeStateChangeEventRequest}:
 *
 * <ul>
 *   <li>{@code stateTypeUri} - in case of an application run or debug start
 *   the state type consists of the launch mode concatenated to a string
 *   {@code " (launch)"}, for example {@code "run (launch)"} or
 *   {@code "debug (launch)"}. In case of a perspective change it
 *   consists of the perspective name in lowercase concatenated to string
 *   {@code " (perspective)"}, for example {@code java (perspective)} or
 *   {@code debug (perspective)}.
 *   <li>See {@link IdeListener} for documentation of inherited data.
 * </ul>
 *
 * @author Pavol Zbell
 * @since 1.0
 */
public final class IdeStateListener extends IdeListener implements LaunchListener, PerspectiveListener {
  public IdeStateListener() {}

  static IdeStateChangeEventRequest build(final long time, final IProject project, final String state) {
    final IdeStateChangeEventRequest data = new IdeStateChangeEventRequest();

    data.setStateTypeUri(IdeUacaUris.forStateChangeType(state));

    setProjectData(data, project);
    setApplicationData(data);
    setEventData(data, time);

    return data;
  }

  void processLaunch(final long time, final ILaunch launch) {
    Iterable<IProject> projects = Projects.fromLaunch(launch);

    String state = launch.getLaunchMode() + " (launch)";

    for (IProject project: projects) {
      this.proxy.sendStateChangeEvent(build(time, project, state));
    }
  }

  void processPerspective(final long time, final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {
    IProject project = Projects.fromPage(page);

    String state = descriptor.getLabel().toLowerCase() + " (perspective)";

    this.proxy.sendStateChangeEvent(build(time, project, state));
  }

  public void launchAdded(final ILaunch launch) {
    final long time = currentTime();

    execute(new Runnable() {
      public void run() {
        processLaunch(time, launch);
      }
    });
  }

  public void launchRemoved(final ILaunch launch) {}

  public void launchChanged(final ILaunch launch) {}

  public void perspectiveOpened(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {}

  public void perspectiveClosed(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {}

  public void perspectiveActivated(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {
    final long time = currentTime();

    execute(new Runnable() {
      public void run() {
        processPerspective(time, page, descriptor);
      }
    });
  }

  public void perspectiveDeactivated(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {}

  public void perspectivePreDeactivate(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor) {}

  public void perspectiveChanged(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor, final String change) {}

  public void perspectiveChanged(final IWorkbenchPage page, final IPerspectiveDescriptor descriptor, final IWorkbenchPartReference reference, final String change) {}

  public void perspectiveSavedAs(final IWorkbenchPage page, final IPerspectiveDescriptor before, final IPerspectiveDescriptor after) {}
}
