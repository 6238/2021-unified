package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PiSubsystem extends SubsystemBase {
    private final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private final NetworkTable table = inst.getTable("SmartDashboard");

    private final NetworkTableEntry xEntry;
    private final NetworkTableEntry yEntry;
    private final NetworkTableEntry zEntry;

    private double x = 0;
    private double y = 0;
    private double z = 0;

    public PiSubsystem(Factory f) {
        xEntry = table.getEntry("x");
        yEntry = table.getEntry("y");
        zEntry = table.getEntry("z");
    }

    @Override
    public void periodic() {
        x = xEntry.getDouble(x);
        y = yEntry.getDouble(y);
        z = zEntry.getDouble(z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
