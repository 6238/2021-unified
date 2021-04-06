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
    private double speed = ShooterConstants.INITIAL_SHOOTER_SPEED;
    private int solenoidStatus = 0;

    public ShooterCommand(Factory f, ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        compressor = shooterSubsystem.getCompressor();
        compressor.setClosedLoopControl(true);

        speedSlider = f.getSlider("Shooter speed: ", speed, 0.0, 1.0);
        compressorEnabledButton = f.getToggleButton("Compressor Enabled", compressor.enabled());

        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        speed = speedSlider.getDouble();
        shooterSubsystem.setSpeed(shooterButton ? speed : 0);
        shooterSubsystem.setSolenoidPosition(solenoidStatus);
        compressorEnabledButton.set(compressor.enabled());
    }
    
    public void toggleShooter(boolean status) {
        shooterButton = status;
    }
    
    public void toggleSolenoid(int status) {
        solenoidStatus = status;
    }
    
    public void setShooterSpeed(int position) {
        speed = ShooterConstants.SHOOTER_SPEEDS[position];
        speedSlider.setDouble(speed);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
