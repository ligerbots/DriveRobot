
package org.ligerbots.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.robot.commands.DriveCommand;
import org.ligerbots.robot.subsystems.CompressorSubsystem;
import org.ligerbots.robot.subsystems.DriveSubsystem;
import org.ligerbots.robot.subsystems.ShooterSubsystem;
import org.ligerbots.robot.subsystems.VisionSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {

  public static final DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static final CompressorSubsystem compressorSubsystem = new CompressorSubsystem();
  public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  public static final VisionSubsystem visionSubsystem = new VisionSubsystem();
  public static final DriveCommand driveCommand = new DriveCommand();
  public static OperatorInterface oi;
  private long mtimeSplit = 0; // like a stopwatch "split"
  private int mtickCounter = 0; // count control loop ticks

  Command autonomousCommand;
  SendableChooser<?> chooser;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public void robotInit() {
    oi = new OperatorInterface();
    /*
     * autogenerated gubbish. No, the DriveCommand is not our Autonoumous chooser = new
     * SendableChooser(); chooser.addDefault("Default Auto", new DriveCommand()); //
     * chooser.addObject("My Auto", new MyAutoCommand()); SmartDashboard.putData("Auto mode",
     * chooser);
     */

  }

  /**
   * This function is called once each time the robot enters Disabled mode. You can use it to reset
   * any subsystem information you want to clear when the robot is disabled.
   */
  public void disabledInit() {

  }

  /**
   * Runs every tick during disabled mode.
   */
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
    driveSubsystem.updateSmartDashboard();
    visionSubsystem.setVisionEnabled(false);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString code to get the auto name from the text box below the Gyro. You can add
   * additional auto modes by adding additional commands to the chooser code above (like the
   * commented example) or additional comparisons to the switch structure below with additional
   * strings & commands.
   */
  public void autonomousInit() {
    autonomousCommand = (Command) chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new MyAutoCommand(); break; case
     * "Default Auto": default: autonomousCommand = new ExampleCommand(); break; }
     */
    visionSubsystem.setLedRingOn(true);
    visionSubsystem.setVisionEnabled(true);

    // schedule the autonomous command (example)
    if (autonomousCommand != null) {
      autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  public void autonomousPeriodic() {
    commonPeriodic();
  }

  /**
   * Runs at the beginning of teleop.
   */
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    driveCommand.start();

    compressorSubsystem.setCompressorOn(true);
    visionSubsystem.setLedRingOn(true);
    visionSubsystem.setVisionEnabled(true);
    driveSubsystem.zeroYaw();
  }

  /**
   * This function is called periodically during operator control.
   */
  public void teleopPeriodic() {
    commonPeriodic();
  }


  private void commonPeriodic() {
    Scheduler.getInstance().run();
    driveSubsystem.updateSmartDashboard();
    // take time ticks every 50 cycles
    if ((mtickCounter++) % 50 == 0) {
      long lastSplit = mtimeSplit;
      mtimeSplit = System.nanoTime();
      double deltaTime = (double) (mtimeSplit - lastSplit) / 1.0E9;
      SmartDashboard.putNumber("TimeFor50Ticks", deltaTime);
      // spam console every 250 ticks (should be 5 seconds!). Only output time for last 50 ticks
      if ((mtickCounter++ % 250) == 0) {
        System.out.printf("50 ticks took %4.2f seconds", deltaTime);
      }
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  public void testPeriodic() {
    LiveWindow.run();
  }
}
