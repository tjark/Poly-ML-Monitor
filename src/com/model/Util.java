package com.model;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.SortedSet;
import java.util.TreeSet;

public class Util
{

    private BufferedWriter polyStdin;
    private BufferedReader polyStdout;

    public Util(String polyCmd) throws IOException
    {
        if (polyCmd == null)
            {
                throw new NullPointerException();
            }

        // launch new Poly/ML process
        Process polyProcess = Runtime.getRuntime().exec(polyCmd);
        polyStdin = new BufferedWriter(new OutputStreamWriter(polyProcess.getOutputStream()));
        polyStdout = new BufferedReader(new InputStreamReader(polyProcess.getInputStream()));
    }

    public void close() throws IOException
    {
        String ctrlD = "\u0004";
        polyStdin.write(ctrlD);

        polyStdin.close();
        polyStdout.close();
    }

    public static SortedSet<Integer> getCurrentProcessIDs()
    {
        SortedSet<Integer> currentProcessIDs = new TreeSet<Integer>();

        try
            {
                if (OS.isLinux())
                    {
                        Process p = Runtime.getRuntime().exec("ps -eo pid");
                        BufferedReader input =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line;
                        while ((line = input.readLine()) != null)
                            {
                                line = line.replace(" ", "");
                                try
                                    {
                                        Integer id = new Integer(line);
                                        currentProcessIDs.add(id);
                                    }
                                catch (NumberFormatException e)
                                    {
                                        // ignore non-numeric output
                                    }
                            }
                    }
            }
        catch (IOException e)
            {
                System.out.println("I/O error while determining running processes.");
            }

        return currentProcessIDs;
    }

    public PolyMLStatistics getPolyMLStatistics(int processId)
    {
        if (processId < 0)
            {
                throw new IllegalArgumentException("negative process ID");
            }

        if (polyStdin == null || polyStdout == null)
            {
                return null;
            }

        try
            {
                //System.out.println("[>>poly] " + Integer.toString(processId));
                polyStdin.write(Integer.toString(processId));
                polyStdin.newLine();
                polyStdin.flush();

                String line = polyStdout.readLine();
                //System.out.println("[<<poly] " + line);
                if (!"<polymlresponse>".equals(line))
                    {
                        System.out.println("Unexpected response from Poly/ML process.");
                        return null;
                    }

                StringBuffer response = new StringBuffer();
                while (!"</polymlresponse>".equals(line = polyStdout.readLine()))
                    {
                        //System.out.println("[<<poly] " + line);
                        response.append(line);
                    }
                //System.out.println("[<<poly] " + line);

                if (response.length() == 0)
                    {
                        return null;
                    }

                // FIXME: character encoding?
                XMLDecoder d =
                    new XMLDecoder(new ByteArrayInputStream(response.toString().getBytes()));
                Object o = d.readObject();
                if (PolyMLStatistics.class.equals(o.getClass()))
                    {
                        return (PolyMLStatistics)o;
                    }
                else
                    {
                        System.out.println("Unexpected object type in Poly/ML output.");
                        return null;
                    }
            }
        catch (IOException e)
            {
                return null;
            }
    }

}
