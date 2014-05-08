package com.wrpkm.openedge.perspectiveswitcher;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

import com.wrpkm.openedge.perspectiveswitcher.Constants;

public class PerspectiveListener implements IPartListener, IPerspectiveListener {

	public void perspectiveActivated(IWorkbenchPage arg0,
			IPerspectiveDescriptor arg1) {
		// TODO Auto-generated method stub

	}

	public void perspectiveChanged(IWorkbenchPage arg0,
			IPerspectiveDescriptor arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	public void partActivated(IWorkbenchPart part) {
		if (!(part instanceof IEditorPart))
			return;
		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		if (!preferenceStore.getBoolean("enabled"))
			return;
		IWorkbenchWindow workbenchWindow = part.getSite().getPage()
				.getWorkbenchWindow();
		if (workbenchWindow == null)
			return;
		IEditorPart editorPart = workbenchWindow.getActivePage()
				.getActiveEditor();
		if (editorPart == null)
			return;
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput == null)
			return;
		String fileName = editorInput.getName().toLowerCase().trim();
		if (fileName.length() == 0)
			return;
		IPerspectiveDescriptor perspectiveDescriptor = workbenchWindow
				.getActivePage().getPerspective();
		if (perspectiveDescriptor == null)
			return;
		String activeId = perspectiveDescriptor.getId();
		if (activeId == null)
			return;
		if (perspectiveDescriptor.getLabel() == null
				|| perspectiveDescriptor.getLabel().toLowerCase()
						.indexOf("debug") > -1)
			return;
		String nextPerspectiveId = findKeyForFileName(editorPart, fileName,
				preferenceStore);
		if (nextPerspectiveId == null || activeId.equals(nextPerspectiveId)) {
			return;
		} else {
			switchToPerspective(nextPerspectiveId);
			return;
		}
	}

	public void partBroughtToTop(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	public void partClosed(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	public void partDeactivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	public void partOpened(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	private Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}

	private String[] getKeyList(IPreferenceStore preferenceStore) {
		ScopedPreferenceStore scopedStore = (ScopedPreferenceStore) preferenceStore;
		IEclipsePreferences eclipsePreferences[] = scopedStore
				.getPreferenceNodes(true);
		if (eclipsePreferences == null || eclipsePreferences.length == 0)
			return null;
		ArrayList allKeysList = new ArrayList();
		String allKeys[] = (String[]) null;
		try {
			IEclipsePreferences aieclipsepreferences[];
			int j = (aieclipsepreferences = eclipsePreferences).length;
			for (int i = 0; i < j; i++) {
				IEclipsePreferences ep = aieclipsepreferences[i];
				Collections.addAll(allKeysList, ep.keys());
			}

			allKeys = (String[]) allKeysList.toArray(new String[allKeysList
					.size()]);
		} catch (BackingStoreException _ex) {
		}
		return allKeys;
	}

	private String findKeyForFileName(IEditorPart editorPart, String fileName,
			IPreferenceStore preferenceStore) {
		int idxLastDot = fileName.lastIndexOf(".");
		if (idxLastDot == -1)
			return null;
		String ext = fileName.substring(idxLastDot).trim();
		if (ext.length() == 0)
			return null;
		String allKeys[] = getKeyList(preferenceStore);
		if (allKeys == null)
			return null;
		String as[];
		int j = (as = allKeys).length;
		for (int i = 0; i < j; i++) {
			String key = as[i];
			String fileExtensions = preferenceStore.getString(key)
					.toLowerCase().trim();
			if (fileExtensions.indexOf(ext) > -1) {
				if (editorPart.getClass().getCanonicalName()
						.equals(Constants.OETEXTEDITOR_EDITOR_ID)) {
					if (key.equals(Constants.PROGRESS_PERSPECTIVE_ID)) {
						return Constants.PROGRESS_PERSPECTIVE_ID;
					}
				} else if (editorPart.getClass().getCanonicalName()
						.equals(Constants.APPBUILDER_EDITOR_ID)) {
					if (key.equals(Constants.OESTUDIO_PERSPECTIVE_ID)) {
						return Constants.OESTUDIO_PERSPECTIVE_ID;
					}
				} else if (editorPart.getClass().getCanonicalName()
						.equals(Constants.VISUALDESIGNER_EDITOR_ID)) {
					if (key.equals(Constants.VISUALEDITOR_PERSPECTIVE_ID)) {
						return Constants.VISUALEDITOR_PERSPECTIVE_ID;
					}
				} else {
					return key;
				}
			}
		}

		return null;
	}

	private void switchToPerspective(final String nextPerspectiveId) {
		if (nextPerspectiveId == null) {
			return;
		} else {
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						IWorkbenchWindow workbenchWindow = Activator
								.getDefault().getWorkbench()
								.getActiveWorkbenchWindow();
						workbenchWindow.getWorkbench().showPerspective(
								nextPerspectiveId, workbenchWindow);
					} catch (WorkbenchException _ex) {
					}
				}
			});
			return;
		}
	}

}
