package org.ligerbots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.robot.Robot;

/**
 * Command for toggling the compressor.
 */
public class CompressorCommand extends Command {

  public CompressorCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.compressorSubsystem);
  }

  // Called just before this Command runs the first time
  protected void initialize() {
    Robot.compressorSubsystem.toggleCompressor();
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {}

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {}
}
