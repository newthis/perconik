package com.gratex.perconik.activity.ide.listeners;

import static com.gratex.perconik.activity.ide.IdeData.setApplicationData;
import static com.gratex.perconik.activity.ide.IdeData.setEventData;
import static com.gratex.perconik.activity.ide.listeners.Utilities.currentTime;
import static com.gratex.perconik.activity.ide.listeners.Utilities.dereferenceEditor;
import static com.gratex.perconik.activity.ide.listeners.Utilities.isNull;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaFlag.MOVED_TO;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaFlag.OPEN;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaKind.ADDED;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaKind.REMOVED;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceEventType.POST_CHANGE;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceType.FILE;
import static sk.stuba.fiit.perconik.eclipse.core.resources.ResourceType.PROJECT;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPart;
import sk.stuba.fiit.perconik.core.java.JavaElements;
import sk.stuba.fiit.perconik.core.java.JavaProjects;
import sk.stuba.fiit.perconik.core.listeners.EditorListener;
import sk.stuba.fiit.perconik.core.listeners.FileBufferListener;
import sk.stuba.fiit.perconik.core.listeners.ResourceListener;
import sk.stuba.fiit.perconik.core.listeners.SelectionListener;
import sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaFlag;
import sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaKind;
import sk.stuba.fiit.perconik.eclipse.core.resources.ResourceDeltaResolver;
import sk.stuba.fiit.perconik.eclipse.core.resources.ResourceEventType;
import sk.stuba.fiit.perconik.eclipse.core.resources.ResourceType;
import sk.stuba.fiit.perconik.eclipse.core.runtime.RuntimeCoreException;
import sk.stuba.fiit.perconik.eclipse.swt.widgets.DisplayTask;
import sk.stuba.fiit.perconik.eclipse.ui.Editors;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.gratex.perconik.activity.ide.UacaProxy;
import com.gratex.perconik.services.uaca.ide.IdeDocumentEventRequest;
import com.gratex.perconik.services.uaca.ide.type.IdeDocumentEventType;

/**
 * A listener of IDE document events. This listener handles desired
 * events and eventually builds corresponding data transfer objects
 * of type {@link IdeDocumentEventRequest} and passes them to the
 * {@link UacaProxy} to be transferred into the <i>User Activity Central
 * Application</i> for further processing.
 * 
 * <p>Document operation types that this listener is interested in are
 * determined by the {@link IdeDocumentEventType} enumeration:
 * 
 * <ul>
 *   <li>Add - a document is added into the project.
 *   <li>Close - an opened document is closed.
 *   <li>Open - a closed document is opened.
 *   <li>Remove - a document is removed from the project.
 *   <li>Rename - currently not supported.
 *   <li>Save - a document is saved.
 *   <li>Switch to - focus is changed from one document to another
 *   and editor selections (tabs and text). Note that structured
 *   selections in package explorer are supported.
 * </ul>
 * 
 * <p>Data available in an {@code IdeDocumentEventRequest}:
 * 
 * <ul>
 *   <li>{@code document} - see {@code IdeDocumentDto} below.
 *   <li>See {@link IdeListener} for documentation of inherited data.
 * </ul>
 * 
 * <p>Data available in an {@code IdeDocumentDto}:
 * 
 * <ul>
 *   <li>{@code branch} - current Git branch name for the document.
 *   <li>{@code changesetIdInRcs} - most recent Git commit
 *   identifier for the document (40 hexadecimal characters),
 *   for example {@code "984dd5f359532d7d806a92b47ef5bfc39d772d64"}.
 *   <li>{@code localPath} - path to the document relative to the workspace root,
 *   for example {@code "com.gratex.perconik.activity/src/com/gratex/perconik/activity/ide/listeners/IdeCommitListener.java"}.
 *   <li>{@code rcsServer} - see documentation of {@code RcsServerDto}
 *   in {@link IdeCommitListener} for more details.
 *   <li>{@code serverPath} - always the same as {@code localPath}.
 * </ul>
 * 
 * <p>Note that in case of not editable source code, such as classes from JRE
 * system library, fields {@code branchName}, {@code changesetIdInRcs},
 * and {@code rcsServer} are unused and set to {@code null}.
 * 
 * @author Pavol Zbell
 * @since 1.0
 */
public final class IdeDocumentListener extends IdeListener implements EditorListener, FileBufferListener, ResourceListener, SelectionListener
{
	// TODO note that switch_to is generated before open/close 
	// TODO open is also generated on initial switch to previously opened tab directly after eclipse launch 
	
	private static final boolean processStructuredSelections = false;
	
	private static final Set<ResourceEventType> resourceEventTypes = ImmutableSet.of(POST_CHANGE);
	
	private final Object lock = new Object();
	
	@GuardedBy("lock")
	private UnderlyingResource<?> resource;
	
	public IdeDocumentListener()
	{
	}
	
	private final boolean updateResource(final UnderlyingResource<?> resource)
	{
		if (resource != null)
		{
			synchronized (this.lock)
			{
				if (!resource.equals(this.resource))
				{
					this.resource = resource;
					
					return true;
				}
			}
		}
		
		return false;
	}

	static final IdeDocumentEventRequest build(final long time, final IFile file)
	{
		return build(time, UnderlyingResource.of(file));
	}
	
	static final IdeDocumentEventRequest build(final long time, final UnderlyingResource<?> resource)
	{
		final IdeDocumentEventRequest data = new IdeDocumentEventRequest();

		resource.setDocumentData(data);
		resource.setProjectData(data);

		setApplicationData(data);
		setEventData(data, time);
		
		return data;
	}
	
	private static final class ResourceDeltaVisitor extends ResourceDeltaResolver
	{
		private final long time;
		
		private final ResourceEventType type;

		private final SetMultimap<IdeDocumentEventType, IFile> operations;
		
		ResourceDeltaVisitor(final long time, final ResourceEventType type)
		{
			assert time >= 0;
			assert type != null;
			
			this.time = time;
			this.type = type;

			this.operations = LinkedHashMultimap.create(3, 2);
		}

		@Override
		protected final boolean resolveDelta(final IResourceDelta delta, final IResource resource)
		{
			assert delta != null && resource != null;
			
			if (this.type != POST_CHANGE || !ResourceFilter.INSTANCE.apply(resource))
			{
				return false;
			}
			
			ResourceType type = ResourceType.valueOf(resource.getType());
			
			Set<ResourceDeltaFlag> flags = ResourceDeltaFlag.setOf(delta.getFlags());
			
			if (type == PROJECT && flags.contains(OPEN))
			{
				return false;
			}
			
			if (type != FILE)
			{
				return true;
			}
			
			if (flags.contains(MOVED_TO))
			{
				IPath path  = delta.getMovedToPath();
				IPath other = resource.getFullPath();
				
				if (path != null && other != null && !Objects.equals(path.lastSegment(), other.lastSegment()))
				{
					this.operations.put(IdeDocumentEventType.RENAME, (IFile) resource);
					
					return false;
				}
			}
			
			ResourceDeltaKind kind = ResourceDeltaKind.valueOf(delta.getKind());

			if (kind == ADDED)   this.operations.put(IdeDocumentEventType.ADD,    (IFile) resource);
			if (kind == REMOVED) this.operations.put(IdeDocumentEventType.REMOVE, (IFile) resource);

			return false;
		}

		@Override
		protected final boolean resolveResource(final IResource resource)
		{
			return false;
		}

		@Override
		protected final void postVisitOrProbe()
		{
			if (this.operations.containsKey(IdeDocumentEventType.RENAME))
			{
				this.operations.removeAll(IdeDocumentEventType.ADD);
			}
			
			for (Entry<IdeDocumentEventType, IFile> entry: this.operations.entries())
			{
				UacaProxy.sendDocumentEvent(build(this.time, entry.getValue()), entry.getKey());
			}
		}
	}
	
	private static enum ResourceFilter implements Predicate<IResource>
	{
		INSTANCE;

		public final boolean apply(@Nullable final IResource resource)
		{
			// TODO use JGit here if possible, ignore everything in .gitignore

			if (resource == null)
			{
				return false;
			}
			
			IProject project = resource.getProject();
			
			if (project != null)
			{
				try
				{
					if (JavaProjects.inOutputLocation(project, resource))
					{
						return false;
					}
				}
				catch (RuntimeCoreException e)
				{
					return true;
				}
			}
			
			return true;
		}
	}

	static final void processResource(final long time, final IResourceChangeEvent event)
	{
		ResourceEventType type  = ResourceEventType.valueOf(event.getType());
		IResourceDelta    delta = event.getDelta();

		new ResourceDeltaVisitor(time, type).visitOrProbe(delta, event);
	}
	
	static final void processResource(final long time, final IEditorReference reference, final IdeDocumentEventType type)
	{
		UnderlyingResource<?> resource = UnderlyingResource.from(dereferenceEditor(reference));
		
		if (resource != null)//TODO && ResourceFilter.INSTANCE.apply(resource.getFile()))
		{
			UacaProxy.sendDocumentEvent(build(time, resource), type);			
		}
	}
	
	final void processSelection(final long time, final IWorkbenchPart part, final ISelection selection)
	{
		UnderlyingResource<?> resource = null;

		if (processStructuredSelections)
		{
			if (selection instanceof StructuredSelection)
			{
				Object element = ((StructuredSelection) selection).getFirstElement();
	
				resource = UnderlyingResource.resolve(element);
	
				if (resource == null && element instanceof IJavaElement)
				{
					IResource other = JavaElements.resource((IJavaElement) element);
	
					if (other instanceof IFile)
					{
						resource = UnderlyingResource.of((IFile) other);
					}
				}
			}
		}
		
		if (isNull(resource) && part instanceof IEditorPart)
		{
			resource = UnderlyingResource.from((IEditorPart) part);
		}
		
		//TODO && ResourceFilter.INSTANCE.apply(resource.getFile()))
		
		if (this.updateResource(resource))
		{
			UacaProxy.sendDocumentEvent(build(time, resource), IdeDocumentEventType.SWITCH_TO);
		}
	}
	
	@Override
	public final void postRegister()
	{
		execute(new Runnable()
		{
			@Override
			public final void run()
			{
				IEditorPart editor = execute(DisplayTask.of(Editors.activeEditorSupplier()));
				
				UnderlyingResource<?> resource = UnderlyingResource.from(editor);

				if (resource == null)
				{
					return;
				}
				
				UacaProxy.sendDocumentEvent(build(currentTime(), resource), IdeDocumentEventType.OPEN);
			}
		});
	}

	public final void resourceChanged(final IResourceChangeEvent event)
	{
		final long time = currentTime();
		
		execute(new Runnable()
		{
			public final void run()
			{
				processResource(time, event);
			}
		});
	}

	public final void selectionChanged(final IWorkbenchPart part, final ISelection selection)
	{
		final long time = currentTime();
		
		execute(new Runnable()
		{
			public final void run()
			{
				processSelection(time, part, selection);
			}
		});
	}

	public final void editorOpened(final IEditorReference reference)
	{
		final long time = currentTime();
		
		execute(new Runnable()
		{
			public final void run()
			{
				processResource(time, reference, IdeDocumentEventType.OPEN);
			}
		});
	}

	public final void editorClosed(final IEditorReference reference)
	{
		final long time = currentTime();
		
		execute(new Runnable()
		{
			public final void run()
			{
				processResource(time, reference, IdeDocumentEventType.CLOSE);
			}
		});
	}

	public final void editorActivated(final IEditorReference reference)
	{
	}

	public final void editorDeactivated(final IEditorReference reference)
	{
	}

	public final void editorVisible(final IEditorReference reference)
	{
	}

	public final void editorHidden(final IEditorReference reference)
	{
	}

	public final void editorBroughtToTop(final IEditorReference reference)
	{
	}

	public final void editorInputChanged(final IEditorReference reference)
	{
	}
	
	public final void bufferCreated(final IFileBuffer buffer)
	{
	}

	public final void bufferDisposed(final IFileBuffer buffer)
	{
	}

	public final void bufferContentAboutToBeReplaced(final IFileBuffer buffer)
	{
	}

	public final void bufferContentReplaced(final IFileBuffer buffer)
	{
	}

	public final void stateChanging(final IFileBuffer buffer)
	{
	}

	public final void stateChangeFailed(final IFileBuffer buffer)
	{
	}

	public final void stateValidationChanged(final IFileBuffer buffer, final boolean stateValidated)
	{
	}

	public final void dirtyStateChanged(final IFileBuffer buffer, final boolean dirty)
	{
		final long time = currentTime();
		
		execute(new Runnable()
		{
			public final void run()
			{
				if (!dirty)
				{
					IFile file = FileBuffers.getWorkspaceFileAtLocation(buffer.getLocation());
					
					UacaProxy.sendDocumentEvent(build(time, file), IdeDocumentEventType.SAVE);
				}
			}
		});
	}

	public final void underlyingFileMoved(final IFileBuffer buffer, final IPath path)
	{
	}

	public final void underlyingFileDeleted(final IFileBuffer buffer)
	{
	}

	public final Set<ResourceEventType> getEventTypes()
	{
		return resourceEventTypes;
	}
}
