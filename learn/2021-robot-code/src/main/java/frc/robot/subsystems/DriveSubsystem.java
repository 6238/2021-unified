package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase {
    private final WPI_TalonSRX leftA;
    private final WPI_TalonSRX leftB;
    private final WPI_TalonSRX leftC;

    private final SpeedControllerGroup left;

    private final WPI_TalonSRX rightA;
    private final WPI_TalonSRX rightB;
    private final WPI_TalonSRX rightC;

    private final SpeedControllerGroup right;

    private final DifferentialDrive differentialDrive;

    private double maxSpeed = 1.0;

    private double xSpeed = 0.0;
    private double rot = 0.0;

    public DriveSubsystem(Factory f) {
        leftA = f.getTalonMotor(Constants.DRIVE_LEFT_MOTOR_A);
        leftB = f.getTalonMotor(Constants.DRIVE_LEFT_MOTOR_B);
        leftC = f.getTalonMotor(Constants.DRIVE_LEFT_MOTOR_C);
        left = new SpeedControllerGroup(leftA, leftB, leftC);

        rightA = f.getTalonMotor(Constants.DRIVE_RIGHT_MOTOR_A);
        rightB = f.getTalonMotor(Constants.DRIVE_RIGHT_MOTOR_B);
        rightC = f.getTalonMotor(Constants.DRIVE_RIGHT_MOTOR_C);
        right = new SpeedControllerGroup(rightA, rightB, rightC);

        differentialDrive = new DifferentialDrive(left, right);
    }

    public void drive(double xSpeed, double rot) {
        this.xSpeed = xSpeed;
        this.rot = rot;
    }

    public void rotate(double rot) {
        this.xSpeed = 0.0;
        this.rot = rot;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        differentialDrive.setMaxOutput(maxSpeed);
    }

    @Override
    public void periodic() {
        if (Math.abs(xSpeed) >= Constants.SPEED_THRESHOLD) {
            differentialDrive.arcadeDrive(xSpeed, rot, false);
        } else if (Math.abs(rot) < Constants.ROTATE_THRESHOLD) {
            brake();
        } else {
            differentialDrive.setMaxOutput(1.0);
            differentialDrive.tankDrive(Math.max(rot, 0.3), -Math.max(rot, 0.3), false);
            differentialDrive.setMaxOutput(maxSpeed);
        }
    }

    public void brake() {
        differentialDrive.tankDrive(0.0, 0.0, false);
    }
}
