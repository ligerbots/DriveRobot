package org.ligerbots.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.robot.RobotMap;

public class CompressorSubsystem extends Subsystem {
  Compressor compressor;

  public CompressorSubsystem() {
    compressor = new Compressor(RobotMap.CAN_PCM_ID);
  }

  public void setCompressorOn(boolean isOn) {
    compressor.setClosedLoopControl(isOn);
  }

  public void toggleCompressor() {
    compressor.setClosedLoopControl(!isCompressorOn());
  }


  public boolean isCompressorOn() {
    return compressor.getClosedLoopControl();
  }

  @Override
  protected void initDefaultCommand() {
  }

}
