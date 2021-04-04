
package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;

public class PIDDriveCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Joystick joystick;

    private final PIDController leftController;
    private final PIDController rightController;

    public PIDDriveCommand(DriveSubsystem driveSubsystem, Joystick joystick) {
        this.driveSubsystem = driveSubsystem;
        this.joystick = joystick;

        leftController = new PIDController(Constants.kP, Constants.kI, Constants.kD);
        rightController = new PIDController(Constants.kP, Constants.kI, Constants.kD);

        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.leftDrive(leftController.calculate(driveSubsystem.getLeftTalon().getSelectedSensorVelocity(),
                (driveSubsystem.getReverseDrive() ? -1 : 1) * Constants.MAX_SPEED
                        * (joystick.getY() + joystick.getX())));
        driveSubsystem.rightDrive(rightController.calculate(driveSubsystem.getRightTalon().getSelectedSensorVelocity(),
                (driveSubsystem.getReverseDrive() ? -1 : 1) * Constants.MAX_SPEED
                        * (joystick.getY() - joystick.getX())));
    }
}