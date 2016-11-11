
package org.ligerbots.robot.subsystems;

import java.util.Arrays;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CompressorSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	Compressor mCompressor; 
	AnalogInput mPressureSensor;
	boolean mHaveSensor;
	boolean mEnable;
	
	public CompressorSubsystem() {
		mCompressor = new Compressor(RobotMap.PCM_CAN);
		mPressureSensor = new AnalogInput(RobotMap.AI_PRESSURE_SENSOR);
		double reading = mPressureSensor.getVoltage();
		if(reading > 0) {
			mHaveSensor = true;
		}
		mEnable = true;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    

	double getPressurePSI(){
		if(!mHaveSensor){
			return -1;
		}
		double reading = mPressureSensor.getVoltage();
		// formula from datasheet:
		// http://www.revrobotics.com/wp-content/uploads/2015/11/REV-11-1107-DS-00.pdf
		// Vcc assumed to be 5V
		double psi = 250 * (reading / 5) - 25;
		return psi;
	}
	
	void setCompressor(boolean on) {
		if (mEnable) mCompressor.setClosedLoopControl(on);
	}
	
	boolean isCompressorOn() {
		return mEnable && mCompressor.getClosedLoopControl();
	}
	
	boolean isPressureSwitchTriggered() {
		return mEnable && mCompressor.getPressureSwitchValue();
	}
	
	void toggleCompressor() {
		if (mEnable) setCompressor(!(isCompressorOn()));
	}

}

