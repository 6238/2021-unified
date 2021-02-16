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
    private final Joystick joystick;

    /**
     * @param driveSubsystem The robot's drivetrain
     * @param joystick       The joystick controller to use
     */
    public DriveCommand(Factory f, DriveSubsystem driveSubsystem, Joystick joystick) {
        this.driveSubsystem = driveSubsystem;
        this.joystick = joystick;

        maxSpeedSlider = f.getSlider("Max Speed", 1.0, 0.0, 1.0);
        
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.setMaxSpeed(maxSpeedSlider.getDouble());
        driveSubsystem.drive(-joystick.getY(), joystick.getX());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
