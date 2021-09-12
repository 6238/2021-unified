// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
    private final WPI_TalonSRX leftTalon0; 
    private final WPI_TalonSRX leftTalon1; 
    private final WPI_TalonSRX leftTalon2; 
    private final WPI_TalonSRX rightTalon0; 
    private final WPI_TalonSRX rightTalon1; 
    private final WPI_TalonSRX rightTalon2; 
    private final SpeedControllerGroup leftController;
    private final SpeedControllerGroup rightController;
    private final DifferentialDrive drive;

    public DriveSubsystem() {
        leftTalon0 = new WPI_TalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_0);
        leftTalon1 = new WPI_TalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_1);
        leftTalon2 = new WPI_TalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_2);
        rightTalon0 = new WPI_TalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_0);
        rightTalon1 = new WPI_TalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_1);
        rightTalon2 = new WPI_TalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_2);
        leftController = new SpeedControllerGroup(leftTalon0, leftTalon1, leftTalon2);
        rightController = new SpeedControllerGroup(rightTalon0, rightTalon1, rightTalon2);
        drive = new DifferentialDrive(leftController, rightController);
    }

    public void drive(double speed, double rot) {
        drive.arcadeDrive(speed, rot);
    }
}
