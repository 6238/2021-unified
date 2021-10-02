
package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.io.Slider;
import frc.robot.io.Info;
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

        leftController = new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD);
        rightController = new PIDController(DriveConstants.kP, DriveConstants.kI, DriveConstants.kD);

        feedforward = new SimpleMotorFeedforward(DriveConstants.kS, DriveConstants.kV, DriveConstants.kA);

        pidSlider = new Slider("PID Speed", 1000, -3400, 3400);
        pInfo = new Info("P", DriveConstants.kP);
        iInfo = new Info("I", DriveConstants.kI);
        dInfo = new Info("D", DriveConstants.kD);

        addRequirements(driveSubsystem);
    }

    public double clampInput(double input) {
        if (input > 1) {
            return 1;
        } else if (input < -1) {
            return -1;
        } else {
            return input;
        }
    }

    @Override
    public void execute() {
        leftController.setP(pInfo.getDouble());
        leftController.setI(iInfo.getDouble());
        leftController.setD(dInfo.getDouble());

        rightController.setP(pInfo.getDouble());
        rightController.setI(iInfo.getDouble());
        rightController.setD(dInfo.getDouble());

        double leftTarget = clampInput(joystick.getY() + joystick.getX()) * DriveConstants.MAX_SPEED;
        double rightTarget = clampInput(joystick.getY() - joystick.getX()) * DriveConstants.MAX_SPEED;

        double leftOutput = leftController.calculate(driveSubsystem.getLeftVelocity(), leftTarget);
        double rightOutput = rightController.calculate(driveSubsystem.getRightVelocity(), rightTarget);

        double leftFeedforward = feedforward.calculate(leftTarget);
        double rightFeedforward = feedforward.calculate(rightTarget);

        pidSlider.setDouble(joystick.getY() * DriveConstants.MAX_SPEED);
        driveSubsystem.pidDrive(leftOutput + leftFeedforward, rightOutput + rightFeedforward);
    }
}