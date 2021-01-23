package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.io.Slider;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private Slider speedSlider;
    private boolean shooterButton;

    public ShooterCommand(Factory f, ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        speedSlider = f.getSlider("Shooter speed: ", ShooterConstants.INITIAL_SHOOTER_SPEED, 0.0, 1.0);

        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.setSpeed(shooterButton ? speedSlider.getDouble() : 0);
        System.out.println(shooterSubsystem.getSpeed());
    }

    public void toggleShooter(boolean status) {
        shooterButton = status;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
