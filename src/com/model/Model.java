package com.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class Model implements ActionListener
{

    /**
     * Internal state
     */

    private LinkedList<ChangeListener> listenerList = new LinkedList<ChangeListener>();
    private Util util;
    private Timer timer;

    /**
     * Auxiliary (UI) data
     */

    private int updateDelay = 500; // milliseconds
    private String polyCmd = "poly -q --use polystats.sml"; // command to run Poly/ML
    private Integer selectedProcess = null;

    /**
     * Statistics data
     */

    // process IDs running at last update
    private SortedSet<Integer> currentProcessIDs =
        new TreeSet<Integer>();

    // map of process IDs and system times to PolyMLStatistics objects
    private SortedMap<Integer, SortedMap<Long, PolyMLStatistics>> processData =
        new TreeMap<Integer, SortedMap<Long, PolyMLStatistics>>();

    public Model()
    {
        util = null;
        try
            {
                setPolyCmd(polyCmd);
            }
        catch (IOException e)
            {
                // ignore error, proceed with util == null
            }
        // set up timer to drive updates
        timer = new Timer(updateDelay, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    // timer event
    public void actionPerformed(ActionEvent e)
    {
        // get running processes
        currentProcessIDs = Util.getCurrentProcessIDs();

        // for every running process, try to get statistics data
        for (Integer id : currentProcessIDs)
            {
                PolyMLStatistics stats = null;
                if (util != null)
                    {
                        stats = util.getPolyMLStatistics(id);
                    }
                if (stats != null)
                    {
                        // add statistics data to model
                        Long now = new Date().getTime();
                        SortedMap<Long, PolyMLStatistics> map = processData.get(id);
                        if (map == null)
                            {
                                map = new TreeMap<Long, PolyMLStatistics>();
                                processData.put(id, map);
                            }
                        map.put(now, stats);
                    }
            }

        // if no process is selected, select the first process
        if (selectedProcess == null && !processData.isEmpty())
            {
                selectedProcess = processData.firstKey();
            }

        // fire change notifications
        fireStateChanged();
    }

    public int getUpdateDelay()
    {
        return updateDelay;
    }

    public void setUpdateDelay(int updateDelay)
    {
        if (updateDelay < 0 )
            {
                throw new IllegalArgumentException("negative delay");
            }
        this.updateDelay = updateDelay;

        // restart timer
        timer.setDelay(updateDelay);
        timer.restart();
    }

    public String getPolyCmd()
    {
        return polyCmd;
    }

    public void setPolyCmd(String polyCmd) throws IOException
    {
        Util newUtil = new Util(polyCmd);
        // do not modify "util" until creation of "newUtil" has succeeded
        if (util != null)
            {
                try
                    {
                        util.close();
                    }
                catch (IOException e)
                    {
                    }
            }
        util = newUtil;
        this.polyCmd = polyCmd;
    }

    public Integer getSelectedProcess()
    {
        return selectedProcess;
    }

    public void setSelectedProcess(Integer selectedProcess)
    {
        if (this.selectedProcess != selectedProcess)
            {
                if (selectedProcess != null && !processData.containsKey(selectedProcess))
                    {
                        throw new IllegalArgumentException("cannot select non-existing process");
                    }

                this.selectedProcess = selectedProcess;
                fireStateChanged();
            }
    }

    public SortedSet<Integer> getCurrentProcessIDs()
    {
        return Collections.unmodifiableSortedSet(currentProcessIDs);
    }

    public SortedMap<Integer, SortedMap<Long, PolyMLStatistics>> getProcessData()
    {
        // FIXME: individual maps in processData should be unmodifiable as well
        return Collections.unmodifiableSortedMap(processData);
    }

    public void addChangeListener(ChangeListener l)
    {
        if (l != null)
            {
                listenerList.addFirst(l);
            }
    }

    public void removeChangeListener(ChangeListener l)
    {
        if (l != null)
            {
                listenerList.remove(l);
            }
    }

    private void fireStateChanged()
    {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : listenerList)
            {
                l.stateChanged(e);
            }
    }

}
