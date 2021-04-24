package frc.robot.io;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

import java.util.HashMap;

public class Info {
    private final SimpleWidget value;
    private double lastDouble;

    public static HashMap<String, Info> cache = new HashMap<>();

    public static Info checkCache(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        return null;
    }

    private void saveToCache() {
        Info.cache.put(value.getTitle(), this);
    }

    public Info(String name, double defaultValue) {
        value = Shuffleboard.getTab("SmartDashboard").add(name, defaultValue);
        lastDouble = defaultValue;
        saveToCache();
    }

    public NetworkTableEntry getEntry() {
        return value.getEntry();
    }

    public double getDouble(double def) {
        lastDouble = getEntry().getDouble(def);
        return getEntry().getDouble(def);
    }

    public double getDouble() {
        return getDouble(lastDouble);
    }
}
