package org.ligerbots.robot.subsystems;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;

public class CompressorSubsystem {
	Compressor compressor;
	
	public CompressorSubsystem() {
		compressor = new Compressor(RobotMap.CAN_PCM_ID);
	}
	
	public void setCompressorOn(boolean isOn) {
		compressor.setClosedLoopControl(isOn);
	}
	
	public boolean isCompressorOn() {
		return compressor.getClosedLoopControl();
	}
}
