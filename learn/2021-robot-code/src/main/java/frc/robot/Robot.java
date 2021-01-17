/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;

    private RobotContainer robotContainer;

    private DriveSubsystem driveSubsystem;
    private ADIS16470_IMU imu;
    private WPI_TalonSRX leftTalon;
    private WPI_TalonSRX rightTalon;

    private ArrayList<Double> imuAngle;
    private ArrayList<Double> imuRate;

    private ArrayList<Double> imuXAccel;
    private ArrayList<Double> imuYAccel;
    private ArrayList<Double> imuZAccel;

    private ArrayList<Double> lEncPos;
    private ArrayList<Double> lEncVel;
    private ArrayList<Double> rEncPos;
    private ArrayList<Double> rEncVel;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our autonomous chooser on the dashboard.
        robotContainer = new RobotContainer();

        driveSubsystem = robotContainer.getDriveSubsystem();
        imu = driveSubsystem.getIMU();
        leftTalon = driveSubsystem.getLeftTalon();
        rightTalon = driveSubsystem.getRightTalon();

        imuAngle = new ArrayList<Double>();
        imuRate = new ArrayList<Double>();

        imuXAccel = new ArrayList<Double>();
        imuYAccel = new ArrayList<Double>();
        imuZAccel = new ArrayList<Double>();

        lEncPos = new ArrayList<Double>();
        lEncVel = new ArrayList<Double>();
        rEncPos = new ArrayList<Double>();
        rEncVel = new ArrayList<Double>();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled commands, running already-scheduled commands, removing
        // finished or interrupted commands, and running subsystem periodic() methods.
        // This must be called from the robot's periodic block in order for anything in
        // the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        System.out.println(WriteCSVFile.writeData(imuAngle, imuRate, imuXAccel, imuYAccel, imuZAccel, lEncPos, lEncVel, rEncPos, rEncVel));
    }

    @Override
    public void disabledPeriodic() {
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }

        imuAngle.add(0.0);
        imuRate.add(0.0);

        imuXAccel.add(0.0);
        imuYAccel.add(0.0);
        imuZAccel.add(0.0);

        lEncPos.add(0.0);
        lEncVel.add(0.0);
        rEncPos.add(0.0);
        rEncVel.add(0.0);
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {

        imuAngle.add(imu.getAngle());
        imuRate.add(imu.getRate());

        imuXAccel.add(imu.getAccelInstantX());
        imuYAccel.add(imu.getAccelInstantY());
        imuZAccel.add(imu.getAccelInstantZ());

        lEncPos.add(leftTalon.getSelectedSensorPosition());
        lEncVel.add(leftTalon.getSelectedSensorVelocity());
        rEncPos.add(rightTalon.getSelectedSensorPosition());
        rEncVel.add(rightTalon.getSelectedSensorVelocity());

    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
