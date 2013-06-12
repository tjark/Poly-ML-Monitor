Poly/ML Monitor
===============

A graphical monitoring application for Poly/ML processes.

By Magnus Stenqvist and Tjark Weber <tjark.weber@it.uu.se>.

![Poly/ML Monitor][1]

Project Website
---------------

[https://bitbucket.org/tjark/poly-ml-monitor/][2]

Installation Notes (Linux)
--------------------------

1. Install [Java][3]. Make sure that `java` invokes the Java VM.

2. Install [Poly/ML][4]. Use the latest version from SVN trunk, *not*
the current release version 5.5. Make sure that `poly` invokes
Poly/ML.

3. Download and unpack [poly-ml-monitor.tgz][5].

4. Run `poly-ml-monitor`.

*Caveat:* If you have several versions of Poly/ML on your machine,
make sure that the Poly/ML monitor and the application(s) that you
want to monitor are using the same version. Use `Settings > Poly/ML
Command` to point the Poly/ML monitor to a specific Poly/ML
executable.

Other operating systems are currently unsupported.

Bugs
----

Please report bugs and feature requests via the [issue tracker][6].

[1]: https://bitbucket.org/tjark/poly-ml-monitor/downloads/screenshot.png
[2]: https://bitbucket.org/tjark/poly-ml-monitor/
[3]: http://www.java.com/
[4]: http://www.polyml.org/
[5]: https://bitbucket.org/tjark/poly-ml-monitor/downloads/poly-ml-monitor.tgz
[6]: https://bitbucket.org/tjark/poly-ml-monitor/issues
