
package org.ligerbots.robot.commands;

import org.ligerbots.robot.OI;
import org.ligerbots.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveCommand extends Command {

    public DriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.exampleSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double y = Robot.oi.mJoystick.getY();
    	
    	// Ligerbots traditionally do "tank" drive. That means we use the 
    	// right joystick on the XBox controller for left/right control, instead 
    	// using the main joystick. getRawAxis(4) gives us the left <-> right position
    	// of the right joystick
    	// double x = Robot.oi.mJoystick.getX(); -- normal "Arcade Drive"
    	double x = Robot.oi.mJoystick.getRawAxis(4);  // Ligerbots tank drive
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
