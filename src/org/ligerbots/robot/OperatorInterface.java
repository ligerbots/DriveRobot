package org.ligerbots.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.ligerbots.robot.commands.CompressorCommand;
import org.ligerbots.robot.commands.ShootCommand;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OperatorInterface {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  public Joystick joystick;
  public JoystickButton compressorButton;
  public JoystickButton shootButton;

  Joystick.ButtonType toggleCompressor;

  XboxController xbox;

  /**
   * Creates the instance of OperatorInterface.
   */
  public OperatorInterface() {
    CompressorCommand toggleCompressorCommand = new CompressorCommand();
    joystick = new Joystick(0);
    compressorButton = new JoystickButton(joystick, 8);
    compressorButton.whenPressed(toggleCompressorCommand);

    shootButton = new JoystickButton(joystick, 3);
    shootButton.whenPressed(new ShootCommand());
    xbox = new XboxController(0);
  }

  
  /**
   * 
   * @return the throttle (forward) amount NOTE forward is positive!
   */
  public double getThrottle() {
    return -xbox.getY(GenericHID.Hand.kLeft);
  }
  
  public double getTurn() {
    return xbox.getX(GenericHID.Hand.kRight);
  }
  
  public double getStrafe() {
    return xbox.getX(GenericHID.Hand.kLeft);
  }
}

