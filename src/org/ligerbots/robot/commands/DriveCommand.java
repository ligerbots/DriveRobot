
package org.ligerbots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.robot.OperatorInterface;
import org.ligerbots.robot.Robot;
import org.ligerbots.robot.subsystems.DriveSubsystem;

/**
 * The command that handles joystick driving.
 */
public class DriveCommand extends Command {

	
  public enum DriveType{TANK,HDRIVE,MECANUM};
  public DriveType driveType = DriveType.TANK;
  
  public DriveCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveSubsystem);
  }

  // Called just before this Command runs the first time
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    if (driveType == DriveType.TANK) {
      executeTankDrive();
    } else if (driveType == DriveType.HDRIVE) {
      executeHDrive();
    } else if (driveType == DriveType.MECANUM) {
      executeMecanumDrive();
    } else {
      // Getting here would be an error
    }
  }
  
  protected void executeTankDrive() {
    double joystickY = Robot.oi.joystick.getRawAxis(1);

    // Ligerbots traditionally do "tank" drive. That means we use the
    // right joystick on the XBox controller for left/right control, instead
    // using the main joystick. getRawAxis(4) gives us the left <-> right position
    // of the right joystick
    // double x = Robot.oi.mJoystick.getX(); -- normal "Arcade Drive"
    double joystickX = Robot.oi.joystick.getRawAxis(4); // Ligerbots tank drive

    // -y is forward!
    Robot.driveSubsystem.arcadeDrive(-joystickY, joystickX);
  }
  
  int ticks = 0;

  // Called repeatedly when this Command is scheduled to run
  protected void executeHDrive() {
    OperatorInterface oi = Robot.oi;
    DriveSubsystem driveTrain = Robot.driveSubsystem;
    
    double strafe = oi.getStrafe();
    double throttle = oi.getThrottle();
    double turn = oi.getTurn();
    driveTrain.strafe(strafe);
    driveTrain.arcadeDrive(throttle, turn);
    ticks += 1;
    if (ticks > 100) {
        ticks = 0;
        System.out.printf("Drive strafe: %4.2f throttle: %4.2f, turn: %4.2f\n", 
              strafe, throttle, turn);
    }
  }
  protected void executeMecanumDrive() {
    double strafe = Robot.oi.getStrafe();
    double throttle = Robot.oi.getThrottle();
    double turn = Robot.oi.getTurn();
    double yaw = Robot.driveSubsystem.getYaw(); // CHECK THIS !!!
    Robot.driveSubsystem.mecanumDrive(throttle, strafe, turn, yaw);
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
