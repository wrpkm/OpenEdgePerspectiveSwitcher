package com.wrpkm.openedge.perspectiveswitcher.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.wrpkm.openedge.perspectiveswitcher.Activator;

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

    public PreferencePage()
    {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Please provide a list of file extensions that should trigger a switch to the listed perspectives (.p .w .cls), leave blank to disable automatic switching to that perspective.\n\nOpenEdge Editor/Perspective relationship rules take priority when multiple perspectives include the same extension.\n\n");
    }

    public void createFieldEditors()
    {
        IPerspectiveDescriptor perspectives[] = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
        if(perspectives == null || perspectives.length == 0)
        {
            setErrorMessage("Error: no perspectives found.");
            return;
        }
        addField(new BooleanFieldEditor("enabled", "&Enable Automatic Perspective Switching", getFieldEditorParent()));
        IPerspectiveDescriptor openedgeperspectivedescriptor[];
        int j = (openedgeperspectivedescriptor = perspectives).length;
        for(int i = 0; i < j; i++)
        {
            IPerspectiveDescriptor pd = openedgeperspectivedescriptor[i];
            if(pd.getId() != null && pd.getId().startsWith("com.openedge") && pd.getLabel() != null && pd.getLabel().toLowerCase().indexOf("debug") <= -1 )
                addField(new StringFieldEditor(pd.getId(), (new StringBuilder(pd.getLabel())).append(" Extensions:").toString(), getFieldEditorParent()));
        }
    }

    public void init(IWorkbench workbench)
    {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }
}