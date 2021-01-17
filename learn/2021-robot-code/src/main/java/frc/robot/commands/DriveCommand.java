package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Slider;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Factory;

/**
 * Drive command for a two motor system
 * 
 * @author sherif
 */
public class DriveCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Slider maxSpeedSlider;
    private final Joystick controller;

    /**
     * Takes in a speed
     * 
     * @param dr         The robot's drivetrain
     * @param controller The joystick controller to use
     */
    public DriveCommand(Factory f, DriveSubsystem dr, Joystick controller) {
        driveSubsystem = dr;
        this.controller = controller;

        maxSpeedSlider = f.getSlider("Max Speed", 1.0, 0.0, 1.0);

        addRequirements(dr);
    }

    @Override
    public void execute() {
        driveSubsystem.setMaxSpeed(maxSpeedSlider.getDouble());
        driveSubsystem.drive(-controller.getY(), controller.getTwist());
        driveSubsystem.drive(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}