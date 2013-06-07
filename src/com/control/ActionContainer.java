package com.control;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.model.Model;
import com.view.ProcessList;

/**
 * Class that contains all the actions in the program
 */
public class ActionContainer
{

    private Model model;
    private Integer selectedProcess;

    private AboutAction aboutAction = new AboutAction();
    private PolyCmdAction polyCmdAction = new PolyCmdAction();
    private QuitProgramAction quitProgramAction = new QuitProgramAction();
    private SelectProcessAction selectProcessAction = new SelectProcessAction();
    private UpdateDelayAction updateDelayAction = new UpdateDelayAction();

    public ActionContainer(Model model)
    {
        if (model == null)
            {
                throw new NullPointerException();
            }
        this.model = model;
    }

    private class AboutAction extends AbstractAction
    {
        public AboutAction()
        {
            super("About");
        }

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null,
                                          "<html>Poly/ML Process Monitor 1.1<br>By Magnus Stenqvist and Tjark Weber<br><br><a href='https://bitbucket.org/tjark/poly-ml-monitor'>https://bitbucket.org/tjark/poly-ml-monitor</a></html>",
                                          "About Poly/ML Process Monitor",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class PolyCmdAction extends AbstractAction
    {
        public PolyCmdAction()
        {
            super("Poly/ML Command");
        }

        public void actionPerformed(ActionEvent e)
        {
            String input = JOptionPane.showInputDialog(null,
                                                       "Enter command to run Poly/ML:",
                                                       model.getPolyCmd());
            if (input != null)
                {
                    try
                        {
                            model.setPolyCmd(input);
                        }
                    catch (IOException ex)
                        {
                            JOptionPane.showMessageDialog(null,
                                                          "Failed to create Poly/ML process.",
                                                          "Error",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                }
        }
    }

    private class QuitProgramAction extends AbstractAction
    {
        public QuitProgramAction()
        {
            super("Quit");
        }

        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    private class SelectProcessAction implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            if (!e.getValueIsAdjusting())
                {
                    ProcessList processList = (ProcessList)e.getSource();
                    model.setSelectedProcess(processList.getSelectedValue());
                }
        }
    }

    private class UpdateDelayAction extends AbstractAction
    {
        public UpdateDelayAction()
        {
            super("Update Delay");
        }

        public void actionPerformed(ActionEvent e)
        {
            int updateDelay = model.getUpdateDelay();
            // FIXME: instead of post-hoc validation, disable OK
            // button for invalid input
            while (true)
                {
                    String input = JOptionPane.showInputDialog(null,
                                                               "Enter delay in milliseconds:",
                                                               String.valueOf(updateDelay));
                    if (input != null)
                        {
                            try
                                {
                                    updateDelay = Integer.parseInt(input);
                                    model.setUpdateDelay(updateDelay);
                                }
                            catch (IllegalArgumentException ex)
                                {
                                    JOptionPane.showMessageDialog(null,
                                                                  "Delay must be a non-negative integer value.",
                                                                  "Error",
                                                                  JOptionPane.ERROR_MESSAGE);
                                    continue;
                                }
                        }
                    break;
                }
        }
    }

    public AboutAction getAboutAction()
    {
        return aboutAction;
    }

    public PolyCmdAction getPolyCmdAction()
    {
        return polyCmdAction;
    }

    public QuitProgramAction getQuitProgramAction()
    {
        return quitProgramAction;
    }

    public SelectProcessAction getSelectProcessAction()
    {
        return selectProcessAction;
    }

    public UpdateDelayAction getUpdateDelayAction()
    {
        return updateDelayAction;
    }

}
