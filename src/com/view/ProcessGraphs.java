package com.view;

import java.util.SortedMap;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.model.Model;
import com.model.PolyMLStatistics;

/**
 * The panel for all the graphs
 */
public class ProcessGraphs extends JPanel implements ChangeListener
{

    private MainWindow mainWindow;

    private Integer selectedProcess;

    private JLabel noProcessSelectedLabel;
    private ChartPanel timePanel;
    private ChartPanel sizePanel;
    private ChartPanel threadsPanel;
    private ChartPanel userPanel;

    TimeSeries gcFullGCs;
    TimeSeries gcPartialGCs;
    TimeSeries sizeAllocation;
    TimeSeries sizeAllocationFree;
    TimeSeries sizeHeap;
    TimeSeries sizeHeapFreeLastFullGC;
    TimeSeries sizeHeapFreeLastGC;
    TimeSeries threadsInML;
    TimeSeries threadsTotal;
    TimeSeries threadsWaitCondVar;
    TimeSeries threadsWaitIO;
    TimeSeries threadsWaitMutex;
    TimeSeries threadsWaitSignal;
    TimeSeries timeGCSystem;
    TimeSeries timeGCUser;
    TimeSeries timeNonGCSystem;
    TimeSeries timeNonGCUser;
    TimeSeries[] userCounters = new TimeSeries[8];

    public ProcessGraphs(MainWindow mainWindow)
    {
        if (mainWindow == null)
            {
                throw new NullPointerException();
            }
        this.mainWindow = mainWindow;

        setOpaque(true);
        setBorder(LineBorder.createBlackLineBorder());

        // noProcessSelectedLabel
        noProcessSelectedLabel =
            new JLabel("Please select a process.", JLabel.CENTER);

        // data series
        gcFullGCs = new TimeSeries("Full GCs");
        gcPartialGCs = new TimeSeries("Partial GCs");
        sizeAllocation = new TimeSeries("Allocation");
        sizeAllocationFree = new TimeSeries("Allocation Free");
        sizeHeap = new TimeSeries("Heap");
        sizeHeapFreeLastFullGC = new TimeSeries("Heap Free (Last Full GC)");
        sizeHeapFreeLastGC = new TimeSeries("Heap Free (Last GC)");
        threadsInML = new TimeSeries("In ML");
        threadsTotal = new TimeSeries("Total");
        threadsWaitCondVar = new TimeSeries("Waiting for CondVar");
        threadsWaitIO = new TimeSeries("Waiting for I/O");
        threadsWaitMutex = new TimeSeries("Waiting for Mutex");
        threadsWaitSignal = new TimeSeries("Waiting for Signal");
        timeGCSystem = new TimeSeries("GC System");
        timeGCUser = new TimeSeries("GC User");
        timeNonGCSystem = new TimeSeries("Non-GC System");
        timeNonGCUser = new TimeSeries("Non-GC User");
        for (int i = 0; i < userCounters.length; i++)
            {
                userCounters[i] = new TimeSeries("Counter " + i);
            }

        // timePanel
        TimeSeriesCollection timeCollection = new TimeSeriesCollection();
        timeCollection.addSeries(timeNonGCUser);
        timeCollection.addSeries(timeNonGCSystem);
        timeCollection.addSeries(timeGCUser);
        timeCollection.addSeries(timeGCSystem);
        timePanel = new ChartPanel(ChartFactory.createTimeSeriesChart("Time", null, null, timeCollection, true, true, true));
        ValueAxis axis = timePanel.getChart().getXYPlot().getRangeAxis();
        axis.setLabelFont(axis.getTickLabelFont());
        axis.setLabel("seconds");

        // sizePanel
        TimeSeriesCollection sizeCollection = new TimeSeriesCollection();
        sizeCollection.addSeries(sizeAllocation);
        sizeCollection.addSeries(sizeAllocationFree);
        sizeCollection.addSeries(sizeHeap);
        sizeCollection.addSeries(sizeHeapFreeLastFullGC);
        sizeCollection.addSeries(sizeHeapFreeLastGC);
        sizePanel = new ChartPanel(ChartFactory.createTimeSeriesChart("Size", null, null, sizeCollection, true, true, true));
        axis = sizePanel.getChart().getXYPlot().getRangeAxis();
        axis.setLabelFont(axis.getTickLabelFont());
        axis.setLabel("bytes");
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // threadsPanel
        TimeSeriesCollection threadsCollection = new TimeSeriesCollection();
        threadsCollection.addSeries(threadsTotal);
        threadsCollection.addSeries(threadsInML);
        threadsCollection.addSeries(threadsWaitCondVar);
        threadsCollection.addSeries(threadsWaitIO);
        threadsCollection.addSeries(threadsWaitMutex);
        threadsCollection.addSeries(threadsWaitSignal);
        threadsCollection.addSeries(gcFullGCs);
        threadsCollection.addSeries(gcPartialGCs);
        threadsPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("Threads and GC", null, null, threadsCollection, true, true, true));
        axis = threadsPanel.getChart().getXYPlot().getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // userPanel
        TimeSeriesCollection userCollection = new TimeSeriesCollection();
        for (TimeSeries userCounter : userCounters)
            {
                userCollection.addSeries(userCounter);
            }
        userPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("User Counters", null, null, userCollection, true, true, true));
        axis = userPanel.getChart().getXYPlot().getRangeAxis();
        axis.setAutoRangeMinimumSize(2.0);
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // initial state
        add(noProcessSelectedLabel);
        update();

        mainWindow.getModel().addChangeListener(this);
    }

    private void update()
    {
        Model model = mainWindow.getModel();
        Integer selectedProcessNow = model.getSelectedProcess();
        if (selectedProcess != selectedProcessNow)
            {
                gcFullGCs.clear();
                gcPartialGCs.clear();
                sizeAllocation.clear();
                sizeAllocationFree.clear();
                sizeHeap.clear();
                sizeHeapFreeLastFullGC.clear();
                sizeHeapFreeLastGC.clear();
                threadsInML.clear();
                threadsTotal.clear();
                threadsWaitCondVar.clear();
                threadsWaitIO.clear();
                threadsWaitMutex.clear();
                threadsWaitSignal.clear();
                timeGCSystem.clear();
                timeGCUser.clear();
                timeNonGCSystem.clear();
                timeNonGCUser.clear();
                for (TimeSeries userCounter : userCounters)
                    {
                        userCounter.clear();
                    }
            }

        if (selectedProcessNow != null)
            {
                // copy actual data from model into charts
                SortedMap<Long, PolyMLStatistics> processData =
                    model.getProcessData().get(selectedProcessNow);
                assert (processData != null);

                long lastTime;
                int itemCount = gcFullGCs.getItemCount();
                if (itemCount > 0)
                    {
                        lastTime = gcFullGCs.getTimePeriod(itemCount-1).getFirstMillisecond();
                        assert (lastTime == gcFullGCs.getTimePeriod(itemCount-1).getLastMillisecond());
                        lastTime++;
                        processData = processData.tailMap(new Long(lastTime));
                    }

                for (SortedMap.Entry<Long, PolyMLStatistics> entry : processData.entrySet())
                    {
                        FixedMillisecond key = new FixedMillisecond(entry.getKey().longValue());
                        PolyMLStatistics value = entry.getValue();

                        gcFullGCs.add(key, value.getGcFullGCs());
                        gcPartialGCs.add(key, value.getGcPartialGCs());
                        sizeAllocation.add(key, value.getSizeAllocation());
                        sizeAllocationFree.add(key, value.getSizeAllocationFree());
                        sizeHeap.add(key, value.getSizeHeap());
                        sizeHeapFreeLastFullGC.add(key, value.getSizeHeapFreeLastFullGC());
                        sizeHeapFreeLastGC.add(key, value.getSizeHeapFreeLastGC());
                        threadsInML.add(key, value.getThreadsInML());
                        threadsTotal.add(key, value.getThreadsTotal());
                        threadsWaitCondVar.add(key, value.getThreadsWaitCondVar());
                        threadsWaitIO.add(key, value.getThreadsWaitIO());
                        threadsWaitMutex.add(key, value.getThreadsWaitMutex());
                        threadsWaitSignal.add(key, value.getThreadsWaitSignal());
                        timeGCSystem.add(key, value.getTimeGCSystem());
                        timeGCUser.add(key, value.getTimeGCUser());
                        timeNonGCSystem.add(key, value.getTimeNonGCSystem());
                        timeNonGCUser.add(key, value.getTimeNonGCUser());
                        long [] valueUserCounters = value.getUserCounters();
                        assert (valueUserCounters.length == userCounters.length);
                        for (int i = 0; i < userCounters.length; i++)
                            {
                                userCounters[i].add(key, valueUserCounters[i]);
                            }
                    }

                if (selectedProcess == null)
                    {
                        remove(noProcessSelectedLabel);

                        // add chart panels
                        setLayout(new GridLayout(4,1));
                        add(timePanel);
                        add(sizePanel);
                        add(threadsPanel);
                        add(userPanel);
                        validate();
                    }
            }

        selectedProcess = selectedProcessNow;
    }

    public void stateChanged(ChangeEvent e)
    {
        update();
    }

}
