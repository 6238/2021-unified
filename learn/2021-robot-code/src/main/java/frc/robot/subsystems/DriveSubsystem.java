package frc.robot.subsystems;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.io.BoardManager;

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

    private final NetworkTableEntry leftEncoderEntry;
    private final NetworkTableEntry rightEncoderEntry;

    private final ADIS16470_IMU imu;

    private double maxSpeed = 1.0;

    private double xSpeed = 0.0;
    private double rot = 0.0;

    public DriveSubsystem(Factory f) {
        leftA = f.getTalonMotor(DriveConstants.DRIVE_LEFT_MOTOR_A);
        leftB = f.getTalonMotor(DriveConstants.DRIVE_LEFT_MOTOR_B);
        leftC = f.getTalonMotor(DriveConstants.DRIVE_LEFT_MOTOR_C);
        left = new SpeedControllerGroup(leftA, leftB, leftC);

        rightA = f.getTalonMotor(DriveConstants.DRIVE_RIGHT_MOTOR_A);
        rightB = f.getTalonMotor(DriveConstants.DRIVE_RIGHT_MOTOR_B);
        rightC = f.getTalonMotor(DriveConstants.DRIVE_RIGHT_MOTOR_C);
        right = new SpeedControllerGroup(rightA, rightB, rightC);

        differentialDrive = new DifferentialDrive(left, right);

        leftEncoderEntry = BoardManager.getManager().getTab().add("leftEncoder", 0).getEntry();
        rightEncoderEntry = BoardManager.getManager().getTab().add("rightEncoder", 0).getEntry();

        leftA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
        rightA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);

        imu = new ADIS16470_IMU();
        imu.setYawAxis(IMUAxis.kZ);
        BoardManager.getManager().getTab().add(imu);
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

    public ADIS16470_IMU getIMU() {
        return imu;
    }

    public WPI_TalonSRX getLeftTalon() {
        return leftA;
    }

    public WPI_TalonSRX getRightTalon() {
        return rightA;
    }

    @Override
    public void periodic() {
        if (Math.abs(xSpeed) >= DriveConstants.SPEED_THRESHOLD) {
            differentialDrive.arcadeDrive(xSpeed, rot, false);
        } else if (Math.abs(rot) < DriveConstants.ROTATE_THRESHOLD) {
            brake();
        } else {
            differentialDrive.setMaxOutput(1.0);
            differentialDrive.tankDrive(Math.max(rot, 0.3), -Math.max(rot, 0.3), false);
            differentialDrive.setMaxOutput(maxSpeed);
        }

        leftEncoderEntry.setNumber(leftA.getSelectedSensorVelocity());
        rightEncoderEntry.setNumber(rightA.getSelectedSensorVelocity());
    }

    public void brake() {
        differentialDrive.tankDrive(0.0, 0.0, false);
    }
}
