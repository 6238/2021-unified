package frc.robot.subsystems;

import java.util.LinkedList;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.io.BoardManager;
import frc.robot.io.ToggleButton;

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

    private final NetworkTableEntry imuEntry;
    private final NetworkTableEntry leftEncoderEntry;
    private final NetworkTableEntry rightEncoderEntry;
    private final ToggleButton reverseDriveButton;
    private final ToggleButton tankDriveButton;
    private final ToggleButton arcadeDriveButton;

    private final LinkedList<Double> leftEncoderArray;
    private final LinkedList<Double> rightEncoderArray;

    private final ADIS16470_IMU imu;

    private boolean reverseDrive = false;
    private boolean tankDrive = false;
    private boolean arcadeDrive = true;

    private final Joystick joystick;

    public DriveSubsystem(Factory f, Joystick joystick) {
        leftA = f.getTalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_A);
        leftB = f.getTalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_B);
        leftC = f.getTalonSRX(DriveConstants.DRIVE_LEFT_MOTOR_C);
        left = new SpeedControllerGroup(leftA, leftB, leftC);

        rightA = f.getTalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_A);
        rightB = f.getTalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_B);
        rightC = f.getTalonSRX(DriveConstants.DRIVE_RIGHT_MOTOR_C);
        right = new SpeedControllerGroup(rightA, rightB, rightC);

        differentialDrive = new DifferentialDrive(left, right);

        imuEntry = BoardManager.getManager().getTab().add("imu", 0).getEntry();
        leftEncoderEntry = BoardManager.getManager().getTab().add("leftEncoder", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
        rightEncoderEntry = BoardManager.getManager().getTab().add("rightEncoder", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
        reverseDriveButton = f.getToggleButton("reverseDrive", reverseDrive);
        tankDriveButton = f.getToggleButton("tankDrive", tankDrive);
        arcadeDriveButton = f.getToggleButton("arcadeDrive", arcadeDrive);
        
        leftC.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PID_LOOP_IDX,
                Constants.TIMEOUT_MS);
        rightC.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PID_LOOP_IDX,
                Constants.TIMEOUT_MS);

        leftEncoderArray = new LinkedList<Double>();
        rightEncoderArray = new LinkedList<Double>();

        imu = new ADIS16470_IMU();
        imu.setYawAxis(IMUAxis.kY);

        this.joystick = joystick;
    }

    public void drive(double xSpeed, double rot, double lSpeed, double rSpeed) {
        if (tankDrive) {
            differentialDrive.tankDrive(reverseDrive ? -lSpeed : lSpeed, reverseDrive ? -rSpeed : rSpeed, false);
        } else {
            differentialDrive.arcadeDrive(reverseDrive ? -lSpeed : xSpeed, rot, false);
        }
    }

    public void pidDrive(double lInput, double rInput) {
        left.setVoltage(-lInput);
        right.setVoltage(rInput);
    }

    public void setMaxSpeed(double maxSpeed) {
        differentialDrive.setMaxOutput(maxSpeed);
    }

    public ADIS16470_IMU getIMU() {
        return imu;
    }

    public double getLeftVelocity() {
        if (leftEncoderArray.size() >= 25) {
            leftEncoderArray.pop();
        }
        leftEncoderArray.add(leftC.getSelectedSensorVelocity());

        double sum = 0;
        for (double i : leftEncoderArray) {
            sum += i;
        }

        return sum / 25;
    }

    public double getRightVelocity() {
        if (rightEncoderArray.size() >= 25) {
            rightEncoderArray.pop();
        }
        rightEncoderArray.add(rightC.getSelectedSensorVelocity());

        double sum = 0;
        for (double i : rightEncoderArray) {
            sum += i;
        }

        return sum / 25 * 1.08;
    }

    @Override
    public void periodic() {
        imuEntry.setNumber(imu.getAngle());
        leftEncoderEntry.setNumber(leftC.getSelectedSensorVelocity());
        rightEncoderEntry.setNumber(rightC.getSelectedSensorVelocity());
        // reverseDrive = reverseDriveButton.get();
        reverseDrive = joystick.getTrigger();

        if (tankDrive != tankDriveButton.get()) {
            tankDrive = tankDriveButton.get();
            arcadeDrive = !tankDrive;
            arcadeDriveButton.set(arcadeDrive);
        } else if (arcadeDrive != arcadeDriveButton.get()) {
            arcadeDrive = arcadeDriveButton.get();
            tankDrive = !arcadeDrive;
            tankDriveButton.set(tankDrive);
        }
    }
}
