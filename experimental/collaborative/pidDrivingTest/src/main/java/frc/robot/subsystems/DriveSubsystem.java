// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.LinkedList;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.ToggleButton;

public class DriveSubsystem extends SubsystemBase {
    private final WPI_TalonSRX leftA;
    private final WPI_TalonSRX leftB;
    private final WPI_TalonSRX leftC;

    private final SpeedControllerGroup left;

    private final WPI_TalonSRX rightA;
    private final WPI_TalonSRX rightB;
    private final WPI_TalonSRX rightC;

    private final SpeedControllerGroup right;

    private final ADIS16470_IMU imu;
    private final NetworkTableEntry imuEntry;
    private final ToggleButton reverseDriveButton;
    private final NetworkTableEntry leftEncoderEntry;
    private final NetworkTableEntry rightEncoderEntry;
    private final DifferentialDrive differentialDrive;

    private final LinkedList<Double> leftEncoderArray;
    private final LinkedList<Double> rightEncoderArray;

    private boolean reverseDrive = false;

    public DriveSubsystem() {
        leftA = new WPI_TalonSRX(Constants.DRIVE_LEFT_MOTOR_A);
        leftB = new WPI_TalonSRX(Constants.DRIVE_LEFT_MOTOR_B);
        leftC = new WPI_TalonSRX(Constants.DRIVE_LEFT_MOTOR_C);
        left = new SpeedControllerGroup(leftA, leftB, leftC);

        rightA = new WPI_TalonSRX(Constants.DRIVE_RIGHT_MOTOR_A);
        rightB = new WPI_TalonSRX(Constants.DRIVE_RIGHT_MOTOR_B);
        rightC = new WPI_TalonSRX(Constants.DRIVE_RIGHT_MOTOR_C);
        right = new SpeedControllerGroup(rightA, rightB, rightC);

        differentialDrive = new DifferentialDrive(left, right);

        imuEntry = Shuffleboard.getTab("SmartDashboard").add("imu", 0).getEntry();
        leftEncoderEntry = Shuffleboard.getTab("SmartDashboard").add("leftEncoder", 0).withWidget(BuiltInWidgets.kGraph)
                .getEntry();
        rightEncoderEntry = Shuffleboard.getTab("SmartDashboard").add("rightEncoder", 0)
                .withWidget(BuiltInWidgets.kGraph).getEntry();
        reverseDriveButton = new ToggleButton("reverseDrive", reverseDrive);

        leftC.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rightC.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        left.setInverted(false);
        right.setInverted(false);

        imu = new ADIS16470_IMU();
        imu.setYawAxis(IMUAxis.kY);

        leftEncoderArray = new LinkedList<Double>();
        rightEncoderArray = new LinkedList<Double>();
    }

    public void arcadeDrive(double xSpeed, double rot) {
        differentialDrive.arcadeDrive(reverseDrive ? -xSpeed : xSpeed, rot, false);
    }

    public void leftDrive(double input) {
        left.setVoltage(input);
        System.out.println("left: " + input);
    }

    public void rightDrive(double input) {
        right.setVoltage(-input);
        System.out.println("right: " + (-input));
    }

    public void setMaxSpeed(double maxSpeed) {
        differentialDrive.setMaxOutput(maxSpeed);
    }

    public ADIS16470_IMU getIMU() {
        return imu;
    }

    public double getLeftVelocity() {
        if (leftEncoderArray.size() >= 25) {
            leftEncoderArray.pop();
        }
        leftEncoderArray.add(leftC.getSelectedSensorVelocity());

        double sum = 0;
        for (double i : leftEncoderArray) {
            sum += i;
        }

        return sum / 25;
    }

    public double getRightVelocity() {
        if (rightEncoderArray.size() >= 25) {
            rightEncoderArray.pop();
        }
        rightEncoderArray.add(rightC.getSelectedSensorVelocity());

        double sum = 0;
        for (double i : rightEncoderArray) {
            sum += i;
        }

        return sum / 25;
    }

    public boolean getReverseDrive() {
        return reverseDrive;
    }

    @Override
    public void periodic() {
        reverseDrive = reverseDriveButton.get();

        imuEntry.setNumber(imu.getAngle());
        leftEncoderEntry.setNumber(leftC.getSelectedSensorVelocity());
        rightEncoderEntry.setNumber(rightC.getSelectedSensorVelocity());
    }
}
