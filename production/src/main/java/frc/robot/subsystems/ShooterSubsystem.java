package frc.robot.subsystems;

import java.util.LinkedList;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.io.BoardManager;
import frc.robot.io.Dial;
import frc.robot.io.Slider;

public class ShooterSubsystem extends SubsystemBase {
    private final CANSparkMax leftSide;
    private final CANSparkMax rightSide;
    private final Compressor compressor;
    private final CANEncoder encoder;
    private final DoubleSolenoid solenoid;
    private final NetworkTableEntry encoderEntry;
    private final LinkedList<Double> encoderArray;
    private CANPIDController pidController;

    private final boolean useFollower;
    private double speed = ShooterConstants.INITIAL_SHOOTER_SPEED;
    private Value solenoidPosition = Value.kOff;

    private Slider sliders[] = { null, null, null, null, null };
    private Dial rpmInfo = null;

    private double p;
    private double i;
    private double d;
    private double iRange;
    private double ff;

    private double target;

    private boolean usePID = false;

    public ShooterSubsystem(Factory f) {
        this(f, true);
    }

    public ShooterSubsystem(Factory f, boolean useFollower) {
        this.useFollower = useFollower;
        leftSide = f.getSparkMotor(ShooterConstants.SHOOTER_LEFT);
        rightSide = f.getSparkMotor(ShooterConstants.SHOOTER_RIGHT);
        solenoid = f.getDoubleSolenoid(ShooterConstants.SHOOTER_SOLENOID_FORWARD,
                ShooterConstants.SHOOTER_SOLENOID_REVERSE);
        compressor = f.getCompressor();
        encoder = leftSide.getEncoder();
        encoderEntry = BoardManager.getManager().getTab().add("shooterEncoder", 0).getEntry();
        encoderArray = new LinkedList<Double>();

        if (useFollower) {
            rightSide.follow(leftSide, true);
        }
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Value getSolenoidPosition() {
        return solenoid.get();
    }

    public Compressor getCompressor() {
        return compressor;
    }

    public double getVelocity() {
        if (encoderArray.size() >= 25) {
            encoderArray.pop();
        }
        encoderArray.add(encoder.getVelocity());

        double sum = 0;
        for (double i : encoderArray) {
            sum += i;
        }

        return sum / 25;
    }

    /**
     * @param status -1 is reverse, 0 is off, 1 is forward
     */
    public void setSolenoidPosition(int status) {
        switch (status) {
        case -1:
            solenoidPosition = Value.kReverse;
            break;
        case 1:
            solenoidPosition = Value.kForward;
            break;
        default:
            solenoidPosition = Value.kOff;
            break;
        }
    }

    public void usePID(Factory f) {
        sliders[0] = f.getSlider("P", 0.0, 0.0, 1.0);
        sliders[1] = f.getSlider("I", 0.0, 0.0, 1.0);
        sliders[2] = f.getSlider("D", 0.0, 0.0, 1.0);
        sliders[3] = f.getSlider("IZone", 0.0, 0.0, 1.0);
        sliders[4] = f.getSlider("FF", 0.0, 0.0, 1.0);
        rpmInfo = f.getDial("Current RPM", 0.0);
        usePID = true;
        pidController = leftSide.getPIDController();
    }

    private void readSliders() {
        if (sliders[0] != null && sliders[0].getDouble() != p) {
            p = sliders[0].getDouble();
            pidController.setP(p);
        }
        if (sliders[1] != null && sliders[1].getDouble() != i) {
            i = sliders[1].getDouble();
            pidController.setI(i);
        }
        if (sliders[2] != null && sliders[2].getDouble() != d) {
            d = sliders[2].getDouble();
            pidController.setD(d);
        }
        if (sliders[3] != null && sliders[3].getDouble() != iRange) {
            iRange = sliders[3].getDouble();
            pidController.setIZone(iRange);
        }
        if (sliders[4] != null && sliders[4].getDouble() != ff) {
            ff = sliders[4].getDouble();
            pidController.setFF(ff);
        }

        if (sliders[4] != null) {
            rpmInfo.setOutput(encoder.getVelocity());
        }
    }

    @Override
    public void periodic() {
        readSliders();

        if (!usePID) {
            leftSide.set(speed);
            if (!useFollower) {
                rightSide.set(-speed);
            }
        } else {
            pidController.setReference(target, ControlType.kVelocity);
        }

        encoderEntry.setNumber(getVelocity());
        solenoid.set(solenoidPosition);
    }
}
