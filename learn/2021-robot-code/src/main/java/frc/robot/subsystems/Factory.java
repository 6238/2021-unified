package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.io.Info;
import frc.robot.io.Slider;

public class Factory {
    /**
     * Creates a CANSparkMax object
     * @param busID the ID of the motor controller
     * @return the CANSparkMax object
     */
    public CANSparkMax getSparkMotor(int busID) {
        return new CANSparkMax(busID, CANSparkMaxLowLevel.MotorType.kBrushless);
    }

    /**
     * Creates a Talon Motor
     * @param busID the ID of the motor controller
     * @return the TalonSRX
     */
    public WPI_TalonSRX getTalonMotor(int busID) {
        return new WPI_TalonSRX(busID);
    }

    public DoubleSolenoid getDoubleSolenoid(int forwardID, int reverseID) {
        return new DoubleSolenoid(forwardID, reverseID);
    }

    public Slider getSlider(String name, double defaultValue) {
        return new Slider(name, defaultValue);
    }

    public Slider getSlider(String name, double defaultValue, double min, double max) {
        if (Slider.cache.containsKey(name)) {
            return Slider.cache.get(name);
        }
        return new Slider(name, defaultValue, min, max);
    }

    public Slider getSlider(String name, double defaultValue, int x, int y, int w, int h) {
        return new Slider(name, defaultValue, x, y, w, h);
    }

    public Slider getSlider(String name, double defaultValue, double min, double max, int x, int y, int w, int h) {
        return new Slider(name, defaultValue, min, max, x, y, w, h);
    }

    public Info getInfo(String name, double defaultValue) {
        return new Info(name, defaultValue);
    }
}
