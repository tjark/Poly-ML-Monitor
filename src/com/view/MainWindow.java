package com.view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.control.ActionContainer;
import com.model.Model;

public class MainWindow extends JFrame
{

    private ActionContainer actionContainer;
    private Model model;

    public MainWindow(ActionContainer actionContainer, Model model)
    {
        if (actionContainer == null || model == null)
            {
                throw new NullPointerException();
            }
        this.actionContainer = actionContainer;
        this.model = model;

	createMenuBar();
        setContentPane(new MainPanel(this));

        // FIXME: setIconImages(icons);
	setTitle("Poly/ML Process Monitor");
	pack();
	setResizable(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createMenuBar()
    {
	// File menu
	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic(KeyEvent.VK_F);

	JMenuItem quitProgram = new JMenuItem(actionContainer.getQuitProgramAction());
	quitProgram.setMnemonic(KeyEvent.VK_Q);
	quitProgram.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
	fileMenu.add(quitProgram);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");
	settingsMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem updateDelay = new JMenuItem(actionContainer.getUpdateDelayAction());
        updateDelay.setMnemonic(KeyEvent.VK_D);
        settingsMenu.add(updateDelay);

        JMenuItem polyCmd = new JMenuItem(actionContainer.getPolyCmdAction());
        polyCmd.setMnemonic(KeyEvent.VK_P);
        settingsMenu.add(polyCmd);

        // Help menu
	JMenu helpMenu = new JMenu("Help");
	helpMenu.setMnemonic(KeyEvent.VK_H);

	JMenuItem about = new JMenuItem(actionContainer.getAboutAction());
	about.setMnemonic(KeyEvent.VK_A);
	helpMenu.add(about);

        // Menu bar
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(fileMenu);
	menuBar.add(settingsMenu);
	menuBar.add(helpMenu);

	this.setJMenuBar(menuBar);
    }

    public ActionContainer getActionContainer()
    {
        return actionContainer;
    }

    public Model getModel()
    {
        return model;
    }

}
