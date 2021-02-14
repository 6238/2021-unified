// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Slider;
import frc.robot.subsystems.Factory;

/** An example command that uses an example subsystem. */
public class PiCommand extends CommandBase {
    // Get the default instance of NetworkTables that was created automatically when
    // your program starts
    private final NetworkTableInstance inst = NetworkTableInstance.getDefault();

    // Get the table within that instance that contains the data. There can
    // be as many tables as you like and exist to make it easier to organize
    // your data. In this case, it's a table called datatable.
    private final NetworkTable table = inst.getTable("datatable");

    // Get the entries within that table that correspond to the X and Y values
    // for some operation in your program.
    private final NetworkTableEntry xEntry = table.getEntry("X");
    private final NetworkTableEntry yEntry = table.getEntry("Y");

    private final Slider xSlider;
    private final Slider ySlider;

    private final Factory factory;

    private double x = 0;
    private double y = 0;

    /**
     * Creates a new ExampleCommand.
     *
     * @param factory Factory object needed for Sliders
     */
    public PiCommand(Factory f) {
        factory = f;
        xSlider = factory.getSlider("x", 0, -640, 640);
        ySlider = factory.getSlider("y", 0, -360, 360);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Using the entry objects, set the value to a double that is constantly
        // increasing. The keys are actually "/datatable/X" and "/datatable/Y".
        // If they don't already exist, the key/value pair is added.
        xEntry.getDouble(x);
        yEntry.getDouble(y);

        xSlider.setDouble(x);
        ySlider.setDouble(y);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
