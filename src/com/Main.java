package com;

import javax.swing.SwingUtilities;

import com.control.GUI;
import com.model.OS;

/**
 * Starts the application.
 */
class Main
{

    public static void main(String[] args)
    {
        if (OS.isLinux())
            {
                SwingUtilities.invokeLater(new GUI());
            }
        else
            {
                System.out.println("OS unsupported (please use Linux).");
            }
    }

}
