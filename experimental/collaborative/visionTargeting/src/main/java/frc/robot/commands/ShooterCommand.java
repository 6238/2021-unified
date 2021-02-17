package frc.robot.commands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.io.Slider;
import frc.robot.io.ToggleButton;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private final Compressor compressor;
    private ToggleButton compressorEnabledButton;
    private Slider speedSlider;
    private boolean shooterButton = false;
    private int solenoidStatus = 0;

    public ShooterCommand(Factory f, ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        compressor = shooterSubsystem.getCompressor();
        compressor.setClosedLoopControl(true);

        speedSlider = f.getSlider("Shooter speed: ", ShooterConstants.INITIAL_SHOOTER_SPEED, 0.0, 1.0);
        compressorEnabledButton = f.getToggleButton("Compressor Enabled", compressor.enabled());

        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.setSpeed(shooterButton ? speedSlider.getDouble() : 0);
        shooterSubsystem.setSolenoidPosition(solenoidStatus);
        compressorEnabledButton.set(compressor.enabled());
        // System.out.println(shooterSubsystem.getSpeed());
    }

    public void toggleShooter(boolean status) {
        shooterButton = status;
    }

    public void toggleSolenoid(int status) {
        solenoidStatus = status;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
