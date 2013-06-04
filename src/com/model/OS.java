package com.model;

public class OS
{

    private static final boolean osIsMacOSX;
    private static final boolean osIsWindows;
    private static final boolean osIsLinux;
 
    static
    {
        String os = System.getProperty("os.name");
        if (os != null)
            os = os.toLowerCase();
        osIsMacOSX = "mac os x".equals(os);
        osIsWindows = os != null && os.indexOf("windows") != -1;
        osIsLinux = os != null && os.indexOf("linux") != -1;
    }

    public static boolean isMacOSX()
    {
        return osIsMacOSX;
    }

    public static boolean isWindows()
    {
        return osIsWindows;
    }

    public static boolean isLinux()
    {
        return osIsLinux;
    }

}
