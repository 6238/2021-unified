// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;

import java.util.ArrayList;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An data capture command that uses the drive subsystem. */
public class DataCaptureCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final DriveSubsystem driveSubsystem;

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

    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    // file header
    private static final String HEADER = "imuAngle,imuRate,imuXAccel,imuYAccel,imuZAccel,lEncPos,lEncVel,rEncPos,rEncVel";

    /**
     * Creates a new DataCaptureCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public DataCaptureCommand(DriveSubsystem subsystem) {
        driveSubsystem = subsystem;
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

        // addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
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

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        imuAngle.add(imu.getAngle());
        imuRate.add(imu.getRate());

        imuXAccel.add(imu.getAccelInstantX());
        imuYAccel.add(imu.getAccelInstantY());
        imuZAccel.add(imu.getAccelInstantZ());

        lEncPos.add(leftTalon.getSelectedSensorPosition());
        lEncVel.add(leftTalon.getSelectedSensorVelocity());
        rEncPos.add(rightTalon.getSelectedSensorPosition());
        rEncVel.add(rightTalon.getSelectedSensorVelocity());

        System.out.println("imuA: " + imuAngle.get(imuAngle.size() - 1) + "imuR: " + imuRate.get(imuRate.size() - 1)
                + "imuX: " + imuXAccel.get(imuXAccel.size() - 1) + "imuY: " + imuYAccel.get(imuYAccel.size() - 1)
                + "imuZ: " + imuZAccel.get(imuZAccel.size() - 1) + "lPos: " + lEncPos.get(lEncPos.size() - 1) + "lVel: "
                + lEncVel.get(lEncVel.size() - 1) + "rPos: " + rEncPos.get(rEncPos.size() - 1) + "rVel: "
                + rEncVel.get(rEncVel.size() - 1));
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        String output = "";

        output += HEADER;
        output += LINE_SEPARATOR;

        for (int i = 0; i < imuAngle.size(); i++) {
            output += "" + imuAngle.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuRate.get(i);
            output += COMMA_DELIMITER;

            output += "" + imuXAccel.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuYAccel.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuZAccel.get(i);
            output += COMMA_DELIMITER;

            output += "" + lEncPos.get(i);
            output += COMMA_DELIMITER;
            output += "" + lEncVel.get(i);
            output += COMMA_DELIMITER;

            output += "" + rEncPos.get(i);
            output += COMMA_DELIMITER;
            output += "" + rEncVel.get(i);
            output += LINE_SEPARATOR;
        }

        System.out.println("\n\n\n" + output + "\n\n\n");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}