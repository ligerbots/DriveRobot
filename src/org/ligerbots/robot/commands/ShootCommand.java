package org.ligerbots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.robot.Robot;

/**
 * The command that fires the shooter solenoid.
 */
public class ShootCommand extends Command {

  public ShootCommand() {
    requires(Robot.shooterSubsystem);
  }

  protected void initialize() {
    setTimeout(1);
  }

  protected void execute() {
    Robot.shooterSubsystem.firePiston();
  }

  protected boolean isFinished() {
    return isTimedOut();
  }

  protected void end() {
    Robot.shooterSubsystem.retractPiston();
  }

  protected void interrupted() {}
}
