
package org.ligerbots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.robot.Robot;

/**
 * The command that handles joystick driving.
 */
public class DriveCommand extends Command {

  public DriveCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveSubsystem);
  }

  // Called just before this Command runs the first time
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    double joystickY = Robot.oi.joystick.getRawAxis(1);

    // Ligerbots traditionally do "tank" drive. That means we use the
    // right joystick on the XBox controller for left/right control, instead
    // using the main joystick. getRawAxis(4) gives us the left <-> right position
    // of the right joystick
    // double x = Robot.oi.mJoystick.getX(); -- normal "Arcade Drive"
    double joystickX = Robot.oi.joystick.getRawAxis(4); // Ligerbots tank drive

    // -y is forward!
    Robot.driveSubsystem.drive(-joystickY, joystickX);
  }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
    // Drive command runs forever, or else the robot would stop driving
    return false;
  }

  // Called once after isFinished returns true
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {}
}
