package com.wrpkm.openedge.perspectiveswitcher;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.*;

public class Startup implements IStartup {

	public void earlyStartup() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {

            public void run()
            {
                IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                if(window != null)
                {
                    PerspectiveListener listener = new PerspectiveListener();
                    window.getActivePage().addPartListener(listener);
                    window.addPerspectiveListener(listener);
                }
            }
        });
	}

}
