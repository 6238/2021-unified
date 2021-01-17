package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    public final WPI_TalonSRX throatLeft;
    public final WPI_TalonSRX throatRight;
    public final WPI_TalonSRX elevatorLeft;
    public final WPI_TalonSRX elevatorRight;
    public final WPI_TalonSRX feeder;
    private double throatSpeed = 0.0;
    private double elevatorSpeed = 0.0;
    private double feederSpeed = 0.0;
    private final DoubleSolenoid solenoid;

    public IntakeSubsystem(Factory f) {
        throatLeft = f.getTalonMotor(Constants.THROAT_FRONT);
        throatRight = f.getTalonMotor(Constants.THROAT_BACK);
        elevatorLeft = f.getTalonMotor(Constants.ELEVATOR_FRONT);
        elevatorRight = f.getTalonMotor(Constants.ELEVATOR_BACK);
        feeder = f.getTalonMotor(Constants.FEEDER);
        solenoid = f.getDoubleSolenoid(Constants.INTAKE_SOLENOID_FORWARD, Constants.INTAKE_SOLENOID_REVERSE);
    }

    public void setThroatSpeed(double speed) {
        throatSpeed = speed;
    }

    public void setElevatorSpeed(double speed) {
        elevatorSpeed = speed;
    }

    public void setFeederSpeed(double speed) {
        feederSpeed = speed;
    }

    public void activateSolenoid() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void deactivateSolenoid() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void stop() {
        setThroatSpeed(0.0);
        setElevatorSpeed(0.0);
    }

    @Override
    public void periodic() {
        throatLeft.set(throatSpeed);
        throatRight.set(-throatSpeed);

        elevatorLeft.set(elevatorSpeed);
        elevatorRight.set(-elevatorSpeed);

        feeder.set(feederSpeed);
    }
}