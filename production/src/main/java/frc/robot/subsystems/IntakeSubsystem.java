package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final WPI_TalonSRX feeder;
    private final WPI_TalonSRX elevatorLeft;
    private final WPI_TalonSRX elevatorRight;

    private double feederSpeed = 0.0;
    private double elevatorSpeed = 0.0;

    public IntakeSubsystem(Factory f) {
        feeder = f.getTalonSRX(IntakeConstants.FEEDER);

        elevatorLeft = f.getTalonSRX(IntakeConstants.ELEVATOR_LEFT);
        elevatorRight = f.getTalonSRX(IntakeConstants.ELEVATOR_RIGHT);

        elevatorLeft.setInverted(true);
    }

    public void setFeederSpeed(double speed) {
        feederSpeed = speed;
    }

    public void setElevatorSpeed(double speed) {
        elevatorSpeed = speed;
    }

    @Override
    public void periodic() {
        feeder.set(feederSpeed);

        elevatorLeft.set(elevatorSpeed);
        elevatorRight.set(-elevatorSpeed);
    }
}