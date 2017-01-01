package org.ligerbots.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	// left = 1, 3, 5
	// right = 2, 4, 6
	public static final int CT_DRIVE_LEFT1 = 1;
	public static final int CT_DRIVE_LEFT2 = 3;
	public static final int CT_DRIVE_LEFT3 = 5;
	public static final int CT_DRIVE_RIGHT1 = 2;
	public static final int CT_DRIVE_RIGHT2 = 4;
	public static final int CT_DRIVE_RIGHT3 = 6;
	
	public static final int CAN_PCM_ID = 7;
	public static final int RELAY_LED_RING = 0;
}
