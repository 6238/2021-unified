package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    public final WPI_TalonSRX feeder;
    public final WPI_TalonSRX throatLeft;
    public final WPI_TalonSRX throatRight;
    public final WPI_TalonSRX elevatorLeft;
    public final WPI_TalonSRX elevatorRight;

    private double feederSpeed = 0.0;
    private double throatSpeed = 0.0;
    private double elevatorSpeed = 0.0;

    public IntakeSubsystem(Factory f) {
        feeder = f.getTalonMotor(IntakeConstants.FEEDER);

        throatLeft = f.getTalonMotor(IntakeConstants.THROAT_FRONT);
        throatRight = f.getTalonMotor(IntakeConstants.THROAT_BACK);

        elevatorLeft = f.getTalonMotor(IntakeConstants.ELEVATOR_FRONT);
        elevatorRight = f.getTalonMotor(IntakeConstants.ELEVATOR_BACK);
    }

    public void setFeederSpeed(double speed) {
        feederSpeed = speed;
    }

    public void setThroatSpeed(double speed) {
        throatSpeed = speed;
    }

    public void setElevatorSpeed(double speed) {
        elevatorSpeed = speed;
    }

    @Override
    public void periodic() {
        feeder.set(feederSpeed);

        throatLeft.set(throatSpeed);
        throatRight.set(-throatSpeed);

        elevatorLeft.set(elevatorSpeed);
        elevatorRight.set(-elevatorSpeed);
    }
}