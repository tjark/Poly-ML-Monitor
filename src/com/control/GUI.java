package com.control;

import com.model.Model;
import com.view.MainWindow;

public class GUI implements Runnable
{

    public void run()
    {
        Model model = new Model();
        ActionContainer actionContainer = new ActionContainer(model);
        MainWindow mainWindow = new MainWindow(actionContainer, model);
        mainWindow.setVisible(true);
    }

}
