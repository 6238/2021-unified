package frc.robot.commands;

import frc.robot.io.Slider;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The command to intake a ball
 * 
 * @author sherif
 */
public class IntakeCommand extends CommandBase {
    private double elevatorSpeed;
    private double feederSpeed;

    private Slider elevatorSlider;
    private Slider feederSlider;

    private final IntakeSubsystem intakeSubsystem;

    /**
     * Creates and starts an intake
     * 
     * @param intakeSubsystem the IntakeControl subsystem
     */

    public IntakeCommand(Factory f, IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        elevatorSlider = f.getSlider("Elevator Speed", 1.0, -1.0, 1.0);
        feederSlider = f.getSlider("Feeder Speed", 1.0, -1.0, 1.0);

        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.setElevatorSpeed(elevatorSpeed);
        intakeSubsystem.setFeederSpeed(feederSpeed);
    }

    /**
     * @param status -1: reverse, 0: off, 1: forward
     */
    public void setElevator(int status) {
        elevatorSpeed = status * -1 * elevatorSlider.getDouble();
    }

    /**
     * @param status -1: reverse, 0: off, 1: forward
     */
    public void setFeeder(int status) {
        feederSpeed = status * feederSlider.getDouble();
    }

    /**
     * @param status 0: off, 1: forward
     */
    public void setIntake(int status) {
        setElevator(status);
        setFeeder(status);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
