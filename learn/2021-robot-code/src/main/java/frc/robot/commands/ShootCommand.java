package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.io.Slider;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private Slider speedSlider;

    public ShootCommand(Factory f, ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        speedSlider = f.getSlider("Shooter speed: ", Constants.INITIAL_SHOOTER, 0.0, 1.0);
    }


    @Override
    public void execute() {
        shooterSubsystem.setSpeed(speedSlider.getDouble());
    }
}