# DriveRobot

Note -- 

In order to build this project, your local machine must have the libraries for the TalonSRX (from [CTRE](http://www.ctr-electronics.com/)), and the NavX libraries (from [Kauailabs](http://www.pdocs.kauailabs.com(). The libraries we need total about 200KB. (That's "kilo", i.e. thousand bytes, not million). But if you use the vendors' standard install programs on Windows you'll get about 100 Megabytes of various programs, driver, and libraries for several languages.

I stonrgly advise simply installing the libraries. THis can most easily accomplished by installing the cross-platform packages (intended for non-Windows machines), rather than using the full fat installer programs.

1. The TalonSRX libraries can be downloaded from [CTRE](http://www.ctr-electronics.com/hro.html#product_tabs_technical_resources). Ignore the installer and download the
[CTRE Toolsuite (No Installer) package 4.4.1.9 (.zip)](http://www.ctr-electronics.com//downloads/lib/CTRE_FRCLibs_NON-WINDOWS_v4.4.1.9.zip) instead.
For Java, we only need one directory out of that project -- the java/lib directory. This contents of this directory (CTRLib.jar, libCTRLibDriver.so,
libCTRLibDriver.so.debug) should be copied to your wpilib/user/java/lib directory. On Windows your wpilib directory is installed in your user
profile (%userprofile% -- usually c:\\Users\\*yourusername*)
2. The NavX libraries from kauailabs labs. Download the cross-platform library package. from this page: http://www.pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/java/. Navigate to navx-mxp-libs\roborio\java\lib in the .zip file, and copy navx_frc.jar. Copy that into wpilib/user/java/lib directory, just like you did for the TalonSRX
