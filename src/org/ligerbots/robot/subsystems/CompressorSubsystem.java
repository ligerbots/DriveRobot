package org.ligerbots.robot.subsystems;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CompressorSubsystem  extends Subsystem {
	Compressor compressor;
	
	public CompressorSubsystem() {
		compressor = new Compressor(RobotMap.CAN_PCM_ID);
	}
	
	public void setCompressorOn(boolean isOn) {
		compressor.setClosedLoopControl(isOn);
	}
	
	public void toggleCompressor() {
		if (isCompressorOn()) compressor.setClosedLoopControl(false);
		else  compressor.setClosedLoopControl(false);
	}
	
	
	public boolean isCompressorOn() {
		return compressor.getClosedLoopControl();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
