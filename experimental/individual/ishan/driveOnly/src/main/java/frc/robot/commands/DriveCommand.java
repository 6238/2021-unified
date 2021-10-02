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
    private final Joystick leftJoystick;
    private final Joystick rightJoystick;

    /**
     * @param driveSubsystem The robot's drivetrain
     * @param leftJoystick       The joystick controller to use
     */
    public DriveCommand(Factory f, DriveSubsystem driveSubsystem, Joystick leftJoystick, Joystick rightJoystick) {
        this.driveSubsystem = driveSubsystem;
        this.leftJoystick = leftJoystick;
        this.rightJoystick = rightJoystick;

        maxSpeedSlider = f.getSlider("Max Speed", 1.0, 0.0, 1.0);
        
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.setMaxSpeed(maxSpeedSlider.getDouble());
        driveSubsystem.drive(-leftJoystick.getY(), leftJoystick.getX(), -leftJoystick.getY(), -rightJoystick.getY());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
