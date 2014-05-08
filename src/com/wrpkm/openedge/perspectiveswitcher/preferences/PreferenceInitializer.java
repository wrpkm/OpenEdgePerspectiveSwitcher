package com.wrpkm.openedge.perspectiveswitcher.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

import com.wrpkm.openedge.perspectiveswitcher.*;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("enabled", true);
		IPerspectiveDescriptor perspectives[] = PlatformUI.getWorkbench()
				.getPerspectiveRegistry().getPerspectives();
		if (perspectives != null) {
			IPerspectiveDescriptor openedgeperspectivedescriptor[];
			int j = (openedgeperspectivedescriptor = perspectives).length;
			for (int i = 0; i < j; i++) {
				IPerspectiveDescriptor pd = openedgeperspectivedescriptor[i];
				if (Constants.PROGRESS_PERSPECTIVE_ID
						.equals(pd.getId()))
					store.setDefault(Constants.PROGRESS_PERSPECTIVE_ID,
							".p .w .i .cls");
				else if (Constants.OESTUDIO_PERSPECTIVE_ID
						.equals(pd.getId()))
					store.setDefault(Constants.OESTUDIO_PERSPECTIVE_ID,
							".w");
				else if (Constants.VISUALEDITOR_PERSPECTIVE_ID
						.equals(pd.getId()))
					store.setDefault(Constants.VISUALEDITOR_PERSPECTIVE_ID,
							".cls");
			}
		}
	}
}
