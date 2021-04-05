
package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Slider;
import frc.robot.Info;
import frc.robot.subsystems.DriveSubsystem;

public class PIDDriveCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Joystick joystick;

    private final PIDController leftController;
    private final PIDController rightController;
    private final SimpleMotorFeedforward feedforward;
    private final Slider pidSlider;
    private final Info pInfo;
    private final Info iInfo;
    private final Info dInfo;

    public PIDDriveCommand(DriveSubsystem driveSubsystem, Joystick joystick) {
        this.driveSubsystem = driveSubsystem;
        this.joystick = joystick;

        leftController = new PIDController(Constants.kP, Constants.kI, Constants.kD);
        rightController = new PIDController(Constants.kP, Constants.kI, Constants.kD);

        feedforward = new SimpleMotorFeedforward(Constants.kS, Constants.kV, Constants.kA);

        pidSlider = new Slider("PID Speed", 1000, -3400, 3400);
        pInfo = new Info("P", Constants.kP);
        iInfo = new Info("I", Constants.kI);
        dInfo = new Info("D", Constants.kD);

        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        leftController.setP(pInfo.getDouble());
        leftController.setI(iInfo.getDouble());
        leftController.setD(dInfo.getDouble());

        rightController.setP(pInfo.getDouble());
        rightController.setI(iInfo.getDouble());
        rightController.setD(dInfo.getDouble());

        double leftTarget = pidSlider.getDouble();
        double rightTarget = pidSlider.getDouble();

        double leftOutput = leftController.calculate(driveSubsystem.getLeftVelocity(), leftTarget);
        double rightOutput = rightController.calculate(driveSubsystem.getRightVelocity(), rightTarget);

        double leftFeedforward = feedforward.calculate(leftTarget);
        double rightFeedforward = feedforward.calculate(rightTarget);

        driveSubsystem.leftDrive(leftOutput + leftFeedforward);
        driveSubsystem.rightDrive(rightOutput + rightFeedforward);

        /*
         * driveSubsystem.leftDrive(leftController.calculate(driveSubsystem.getLeftTalon
         * ().getSelectedSensorVelocity(), -Constants.MAX_SPEED * joystick.getY()));
         * driveSubsystem.rightDrive(rightController.calculate(driveSubsystem.
         * getRightTalon().getSelectedSensorVelocity(), Constants.MAX_SPEED *
         * joystick.getY()));
         */
        // driveSubsystem.leftDrive(leftController.calculate(driveSubsystem.getLeftTalon().getSelectedSensorVelocity(),
        // /* (driveSubsystem.getReverseDrive() ? -1 : 1) * */ Constants.MAX_SPEED
        // * (joystick.getY() + joystick.getX())));
        // driveSubsystem.rightDrive(rightController.calculate(driveSubsystem.getRightTalon().getSelectedSensorVelocity(),
        // /* (driveSubsystem.getReverseDrive() ? -1 : 1) * */ Constants.MAX_SPEED
        // * (joystick.getY() - joystick.getX())));
    }
}