
package org.ligerbots.robot.subsystems;

import java.util.Arrays;

import org.ligerbots.robot.RobotMap;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
	CANTalon mLeft1;
	CANTalon mLeft2;
	CANTalon mLeft3;
	CANTalon mRight1;
	CANTalon mRight2;
	CANTalon mRight3;
	RobotDrive mRobotDrive;

	AHRS mNavX;

	public DriveSubsystem() {
		mLeft1 = new CANTalon(RobotMap.CT_DRIVE_LEFT1);
		mLeft2 = new CANTalon(RobotMap.CT_DRIVE_LEFT2);
		mLeft3 = new CANTalon(RobotMap.CT_DRIVE_LEFT3);
		mRight1 = new CANTalon(RobotMap.CT_DRIVE_RIGHT1);
		mRight2 = new CANTalon(RobotMap.CT_DRIVE_RIGHT2);
		mRight3 = new CANTalon(RobotMap.CT_DRIVE_RIGHT3);

		/*
		 * WPILIb offers two versions of the RobotDrive class: one that controls
		 * two motor controllers, and one that controls four motor controllers
		 * Problem is, we have six motors. So we slave the motors 2 and 3 on
		 * each side to motor 1, so as far as RobotDrive is concerned, we have
		 * one motor on each side.
		 */

		mLeft1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		mRight1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);

		mLeft2.changeControlMode(CANTalon.TalonControlMode.Follower);
		mLeft2.set(RobotMap.CT_DRIVE_LEFT1);

		mLeft3.changeControlMode(CANTalon.TalonControlMode.Follower);
		mLeft3.set(RobotMap.CT_DRIVE_LEFT1);

		mRight2.changeControlMode(CANTalon.TalonControlMode.Follower);
		mRight2.set(RobotMap.CT_DRIVE_RIGHT1);

		mRight3.changeControlMode(CANTalon.TalonControlMode.Follower);
		mRight3.set(RobotMap.CT_DRIVE_RIGHT1);

		Arrays.asList(mLeft1, mLeft2, mLeft3, mRight1, mRight2, mRight3)
				.forEach((CANTalon talon) -> talon.enableBrakeMode(true));

		mRobotDrive = new RobotDrive(mLeft1, mRight1);

		mNavX = new AHRS(SPI.Port.kMXP);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void drive(double y, double x) {
		mRobotDrive.arcadeDrive(y, x);
	}
	
	public void zeroYaw() {
		mNavX.zeroYaw();
	}

	public void updateSmartDashboard() {
		SmartDashboard.putBoolean("IMU_Connected", mNavX.isConnected());
		SmartDashboard.putBoolean("IMU_IsCalibrating", mNavX.isCalibrating());
		SmartDashboard.putNumber("IMU_Yaw", mNavX.getYaw());
		SmartDashboard.putNumber("IMU_Pitch", mNavX.getPitch());
		SmartDashboard.putNumber("IMU_Roll", mNavX.getRoll());

		SmartDashboard.putNumber("IMU_CompassHeading",
				mNavX.getCompassHeading());

		SmartDashboard.putNumber("IMU_FusedHeading", mNavX.getFusedHeading());

		SmartDashboard.putNumber("IMU_TotalYaw", mNavX.getAngle());
		SmartDashboard.putNumber("IMU_YawRateDPS", mNavX.getRate());

		SmartDashboard.putNumber("IMU_Accel_X", mNavX.getWorldLinearAccelX());
		SmartDashboard.putNumber("IMU_Accel_Y", mNavX.getWorldLinearAccelY());
		SmartDashboard.putBoolean("IMU_IsMoving", mNavX.isMoving());
		SmartDashboard.putBoolean("IMU_IsRotating", mNavX.isRotating());

		SmartDashboard.putNumber("Velocity_X", mNavX.getVelocityX());
		SmartDashboard.putNumber("Velocity_Y", mNavX.getVelocityY());
		SmartDashboard.putNumber("Displacement_X", mNavX.getDisplacementX());
		SmartDashboard.putNumber("Displacement_Y", mNavX.getDisplacementY());

		SmartDashboard.putNumber("RawGyro_X", mNavX.getRawGyroX());
		SmartDashboard.putNumber("RawGyro_Y", mNavX.getRawGyroY());
		SmartDashboard.putNumber("RawGyro_Z", mNavX.getRawGyroZ());
		SmartDashboard.putNumber("RawAccel_X", mNavX.getRawAccelX());
		SmartDashboard.putNumber("RawAccel_Y", mNavX.getRawAccelY());
		SmartDashboard.putNumber("RawAccel_Z", mNavX.getRawAccelZ());
		SmartDashboard.putNumber("RawMag_X", mNavX.getRawMagX());
		SmartDashboard.putNumber("RawMag_Y", mNavX.getRawMagY());
		SmartDashboard.putNumber("RawMag_Z", mNavX.getRawMagZ());
		SmartDashboard.putNumber("IMU_Temp_C", mNavX.getTempC());

		AHRS.BoardYawAxis yaw_axis = mNavX.getBoardYawAxis();
		SmartDashboard.putString("YawAxisDirection",
				yaw_axis.up ? "Up" : "Down");
		SmartDashboard.putNumber("YawAxis", yaw_axis.board_axis.getValue());

		SmartDashboard.putString("FirmwareVersion", mNavX.getFirmwareVersion());

		
		SmartDashboard.putNumber("QuaternionW", mNavX.getQuaternionW());
		SmartDashboard.putNumber("QuaternionX", mNavX.getQuaternionX());
		SmartDashboard.putNumber("QuaternionY", mNavX.getQuaternionY());
		SmartDashboard.putNumber("QuaternionZ", mNavX.getQuaternionZ());

		SmartDashboard.putNumber("IMU_Byte_Count", mNavX.getByteCount());
		SmartDashboard.putNumber("IMU_Update_Count", mNavX.getUpdateCount());
	}
}
