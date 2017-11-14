
package org.ligerbots.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;
import java.util.Arrays;
import org.ligerbots.robot.RobotMap;

/**
 * Subsystem that represents the drive train.
 */
public class DriveSubsystem extends Subsystem {
  CANTalon left1;
  CANTalon left2;
  CANTalon left3;
  CANTalon right1;
  CANTalon right2;
  CANTalon right3;
  CANTalon centerMaster;
  CANTalon centerSlave;
  RobotDrive robotDrive;
  AHRS navX;
  
  /**
   * Creates the instance of DriveSubsystem.
   */
  public DriveSubsystem() {
    // You must have at least one left and right drive (right?)
    left1 = makeMasterCANTalon(RobotMap.CT_DRIVE_LEFT1);

    left2 = makeSlaveCANTalon(RobotMap.CT_DRIVE_LEFT2, RobotMap.CT_DRIVE_LEFT1);
    left3 = makeSlaveCANTalon(RobotMap.CT_DRIVE_LEFT3, RobotMap.CT_DRIVE_LEFT1);
   
    right1 = makeMasterCANTalon(RobotMap.CT_DRIVE_RIGHT1);
    right2 = makeSlaveCANTalon(RobotMap.CT_DRIVE_RIGHT2, RobotMap.CT_DRIVE_RIGHT1);
    right3 = makeSlaveCANTalon(RobotMap.CT_DRIVE_RIGHT3, RobotMap.CT_DRIVE_RIGHT1);

    centerMaster = makeMasterCANTalon(RobotMap.CT_CENTER_1);
    centerSlave = makeSlaveCANTalon(RobotMap.CT_CENTER_2, RobotMap.CT_CENTER_2);
    
    /*
     * WPILIb offers two versions of the RobotDrive class: one that controls two motor controllers,
     * and one that controls four motor controllers Problem is, we have six motors. So we slave the
     * motors 2 and 3 on each side to motor 1, so as far as RobotDrive is concerned, we have one
     * motor on each side.
     */

    robotDrive = new RobotDrive(left1, right1);

    navX = new AHRS(SPI.Port.kMXP);
  }
  
  public static CANTalon makeMasterCANTalon(int ct_id) {
    CANTalon canTalon = null;
    if (ct_id != RobotMap.CT_ABSENT) {
      // TODO: check talon ct_id actually exists?
      System.out.printf("Creating master CANTalon id %d\n", ct_id);
      canTalon = new CANTalon(ct_id);
      canTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
      canTalon.enableBrakeMode(true);
    }
    return canTalon;
  }
  
  public static CANTalon makeSlaveCANTalon(int ct_id, int ct_master_id) {
    CANTalon canTalon = null;
    if (ct_id!= RobotMap.CT_ABSENT) {
      // TODO: check talons ct_id and ct_master_id actually exist?
      System.out.printf("Creating slave CANTalon id %d following %d\n", ct_id, ct_master_id);
      canTalon = new CANTalon(ct_id);
      canTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
      canTalon.set(ct_master_id);
      canTalon.enableBrakeMode(true);
    }
    return canTalon;
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void arcadeDrive(double throttle, double rotate) {
    robotDrive.arcadeDrive(throttle, rotate);
  }
  
  public void strafe(double x) {
    if (centerMaster != null) {
      centerMaster.set(x);    
    }
  }
  
  public void mecanumDrive(double forward, double strafe, double rotate, double gyroAngle) {
    robotDrive.mecanumDrive_Cartesian(strafe, forward, rotate, gyroAngle);
  } 

  public double getYaw() {
    return navX.getYaw();
  }

  public void zeroYaw() {
    navX.zeroYaw();
  }

  /**
   * Sends SmartDashboard updates for drive.
   */
  public void updateSmartDashboard() {
    SmartDashboard.putBoolean("IMU_Connected", navX.isConnected());
    SmartDashboard.putBoolean("IMU_IsCalibrating", navX.isCalibrating());
    SmartDashboard.putNumber("IMU_Yaw", navX.getYaw());
    SmartDashboard.putNumber("IMU_Pitch", navX.getPitch());
    SmartDashboard.putNumber("IMU_Roll", navX.getRoll());

    SmartDashboard.putNumber("IMU_CompassHeading", navX.getCompassHeading());

    SmartDashboard.putNumber("IMU_FusedHeading", navX.getFusedHeading());

    SmartDashboard.putNumber("IMU_TotalYaw", navX.getAngle());
    SmartDashboard.putNumber("IMU_YawRateDPS", navX.getRate());

    SmartDashboard.putNumber("IMU_Accel_X", navX.getWorldLinearAccelX());
    SmartDashboard.putNumber("IMU_Accel_Y", navX.getWorldLinearAccelY());
    SmartDashboard.putBoolean("IMU_IsMoving", navX.isMoving());
    SmartDashboard.putBoolean("IMU_IsRotating", navX.isRotating());

    SmartDashboard.putNumber("Velocity_X", navX.getVelocityX());
    SmartDashboard.putNumber("Velocity_Y", navX.getVelocityY());
    SmartDashboard.putNumber("Displacement_X", navX.getDisplacementX());
    SmartDashboard.putNumber("Displacement_Y", navX.getDisplacementY());

    SmartDashboard.putNumber("RawGyro_X", navX.getRawGyroX());
    SmartDashboard.putNumber("RawGyro_Y", navX.getRawGyroY());
    SmartDashboard.putNumber("RawGyro_Z", navX.getRawGyroZ());
    SmartDashboard.putNumber("RawAccel_X", navX.getRawAccelX());
    SmartDashboard.putNumber("RawAccel_Y", navX.getRawAccelY());
    SmartDashboard.putNumber("RawAccel_Z", navX.getRawAccelZ());
    SmartDashboard.putNumber("RawMag_X", navX.getRawMagX());
    SmartDashboard.putNumber("RawMag_Y", navX.getRawMagY());
    SmartDashboard.putNumber("RawMag_Z", navX.getRawMagZ());
    SmartDashboard.putNumber("IMU_Temp_C", navX.getTempC());

    AHRS.BoardYawAxis yawAxis = navX.getBoardYawAxis();
    SmartDashboard.putString("YawAxisDirection", yawAxis.up ? "Up" : "Down");
    SmartDashboard.putNumber("YawAxis", yawAxis.board_axis.getValue());

    SmartDashboard.putString("FirmwareVersion", navX.getFirmwareVersion());


    SmartDashboard.putNumber("QuaternionW", navX.getQuaternionW());
    SmartDashboard.putNumber("QuaternionX", navX.getQuaternionX());
    SmartDashboard.putNumber("QuaternionY", navX.getQuaternionY());
    SmartDashboard.putNumber("QuaternionZ", navX.getQuaternionZ());

    SmartDashboard.putNumber("IMU_Byte_Count", navX.getByteCount());
    SmartDashboard.putNumber("IMU_Update_Count", navX.getUpdateCount());
  }
}
