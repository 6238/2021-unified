// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
    private DifferentialDrive m_robotDrive;
    private Joystick m_stick;
    private WPI_TalonSRX leftA = new WPI_TalonSRX(34);
    private WPI_TalonSRX leftB = new WPI_TalonSRX(35);
    private WPI_TalonSRX leftC = new WPI_TalonSRX(36);
    private WPI_TalonSRX rightA = new WPI_TalonSRX(22);
    private WPI_TalonSRX rightB = new WPI_TalonSRX(23);
    private WPI_TalonSRX rightC = new WPI_TalonSRX(24);

    private SpeedControllerGroup left = new SpeedControllerGroup(leftA, leftB, leftC);
    private SpeedControllerGroup right = new SpeedControllerGroup(rightA, rightB, rightC);

    @Override
    public void robotInit() {

        m_robotDrive = new DifferentialDrive(left, right);
        m_stick = new Joystick(0);
    }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getZ());
  }
}
