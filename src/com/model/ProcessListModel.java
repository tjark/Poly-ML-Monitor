package com.model;

import javax.swing.DefaultListModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.view.ProcessList;

/**
 * Contains all entries for the process list
 */
public class ProcessListModel extends DefaultListModel<Integer> implements ChangeListener
{

    private Model model;

    public ProcessListModel(Model model)
    {
        if (model == null)
            {
                throw new NullPointerException();
            }
        this.model = model;

        ensureCapacity(model.getProcessData().size());
        for (Integer processID : model.getProcessData().keySet())
            {
                addElement(processID);
            }

        model.addChangeListener(this);
    }

    public void stateChanged(ChangeEvent e)
    {
        // merge new IDs from model into this
        ensureCapacity(model.getProcessData().size());
        int index = 0;
        for (Integer processID : model.getProcessData().keySet())
            {
                if (index == size() || processID.compareTo(get(index)) < 0)
                    {
                        add(index, processID);
                    }

                // the model only adds process IDs, but never removes them
                assert(processID == get(index));

                index++;
            }

        // any process may have become (in)active and possibly needs
        // to be rendered again
        if (size() > 0)
            {
                fireContentsChanged(this, 0, size()-1);
            }
    }

}
