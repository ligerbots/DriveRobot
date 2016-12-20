
package org.ligerbots.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */


public class ShooterSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	DoubleSolenoid mShooterSolenoid;
	final int PCM_HIGH_FLOW_CAN = 7;
	final int PCM_SHOOTER_FIRE = 5;
	final int PCM_SHOOTER_RETRACT = 4;

	public ShooterSubsystem() {
		mShooterSolenoid = new DoubleSolenoid(PCM_HIGH_FLOW_CAN, PCM_SHOOTER_FIRE, PCM_SHOOTER_RETRACT);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void firePiston() {
    	mShooterSolenoid.set(DoubleSolenoid.Value.kForward);
    	
    }
    public void retractPiston() {
    	mShooterSolenoid.set(DoubleSolenoid.Value.kReverse);    	
    }
    
}

