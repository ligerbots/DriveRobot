# DriveRobot

Note -- 

You will need to install two external libraries to build this project.

1. The TalonSRX libraries from [CTRE](http://www.ctr-electronics.com/hro.html#product_tabs_technical_resources).
I strongly advise not using the CTRE installer, but rather download the 
[CTRE Toolsuite (No Installer) package 4.4.1.9 (.zip)](http://www.ctr-electronics.com//downloads/lib/CTRE_FRCLibs_NON-WINDOWS_v4.4.1.9.zip).
For Java, we only need one directory out of that project -- the java/lib directory. This contents of this directory (CTRLib.jar, libCTRLibDriver.so,
libCTRLibDriver.so.debug) should be copied to your wpilib/user/java/lib directory. On Windows your wpilib directory is installed in your user
profile (%userprofile% -- or usually c:\\Users\\*yourusername*)
2. The NavX libraries from kauailabs labs. We've elected to include this from source, so you should clone https://github.com/kauailabs/navxmxp.git
to a peer directory of this projects.

As checked-in, the project assumes these libraries willb be in the locations referenced above.
