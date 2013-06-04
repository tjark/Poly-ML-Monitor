package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.swing.border.LineBorder;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * This class draws all the components on the content pane.
 */
public class MainPanel extends JPanel implements ChangeListener
{

    private MainWindow mainWindow;

    private boolean isEmpty;
    private JLabel noProcessLabel;
    private JSplitPane splitPane;

    public MainPanel(MainWindow mainWindow)
    {
        if (mainWindow == null)
            {
                throw new NullPointerException();
            }
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 740));
        setOpaque(true);

        // noProcessLabel
        noProcessLabel =
            new JLabel("No Poly/ML process available.", JLabel.CENTER);

        // splitPane
        Box processBox = Box.createVerticalBox();
        processBox.setBorder(LineBorder.createBlackLineBorder());

	JLabel processLabel = new JLabel("Process ID");
        processLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	processBox.add(processLabel);

        ProcessList processList = new ProcessList(mainWindow);
        JScrollPane processScrollPane = new JScrollPane(processList);
        processScrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        processBox.add(processScrollPane);

        ProcessGraphs processGraphs = new ProcessGraphs(mainWindow);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, processBox, processGraphs);

        // initial state
        isEmpty = mainWindow.getModel().getProcessData().isEmpty();
        if (isEmpty)
            {
                add(noProcessLabel, BorderLayout.NORTH);
            }
        else
            {
                add(splitPane, BorderLayout.CENTER);
            }

        mainWindow.getModel().addChangeListener(this);
    }

    public void stateChanged(ChangeEvent e)
    {
        boolean isEmptyNow = mainWindow.getModel().getProcessData().isEmpty();
        if (isEmpty != isEmptyNow)
            {
                removeAll();
                if (isEmptyNow)
                    {
                        add(noProcessLabel, BorderLayout.NORTH);
                    }
                else
                    {
                        add(splitPane, BorderLayout.CENTER);
                    }
                validate();
                isEmpty = isEmptyNow;
            }
    }

}
