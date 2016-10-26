
package org.usfirst.frc.team2877.robot.subsystems;

import org.usfirst.frc.team2877.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	CANTalon mLeft1;
	CANTalon mLeft2;
	CANTalon mLeft3;
	CANTalon mRight1;
	CANTalon mRight2;
	CANTalon mRight3;
	RobotDrive mRobotDrive;
	
	DriveSubsystem() {
		mLeft1 = new CANTalon(RobotMap.CT_DRIVE_LEFT1);
		mLeft2 = new CANTalon(RobotMap.CT_DRIVE_LEFT2);
		mLeft3 = new CANTalon(RobotMap.CT_DRIVE_LEFT3);
		mRight1 = new CANTalon(RobotMap.CT_DRIVE_RIGHT1);
		mRight2 = new CANTalon(RobotMap.CT_DRIVE_RIGHT2);
		mRight3 = new CANTalon(RobotMap.CT_DRIVE_RIGHT3);
		
		/* WPILIb offers two versions of the RobotDrive class:
		 * one that controls two motor controllers, and one that controls four motor controllers
		 * Problem is, we have six motors. So we slave the motors 2 and 3 on each side to 
		 * motor 1, so as far as RobotDrive is concerned, we have one motor on each side.
		*/
		
		mLeft1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		mRight1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		mLeft2.changeControlMode(CANTalon.TalonControlMode.Follower);
		mLeft2.set(RobotMap.CT_DRIVE_LEFT1);
		mLeft3.changeControlMode(CANTalon.TalonControlMode.Follower);
		mLeft3.set(RobotMap.CT_DRIVE_LEFT1);
		
		mRight2.changeControlMode(CANTalon.TalonControlMode.Follower);
		mRight2.set(RobotMap.CT_DRIVE_RIGHT1);

		mRight3.changeControlMode(CANTalon.TalonControlMode.Follower);
		mRight3.set(RobotMap.CT_DRIVE_RIGHT1);
		
		mRobotDrive = new RobotDrive(mLeft1, mRight1);
		
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void drive(double y, double x) {
    	mRobotDrive.arcadeDrive(y, x);
    }
}

