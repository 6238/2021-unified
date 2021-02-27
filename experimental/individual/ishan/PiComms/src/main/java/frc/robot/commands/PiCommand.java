// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.io.Slider;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.PiSubsystem;

/** An example command that uses an example subsystem. */
public class PiCommand extends CommandBase {
    private final Slider xSlider;
    private final Slider ySlider;
    private final Slider zSlider;

    private final Factory factory;
    private final PiSubsystem piSubsystem;

    private double x = 0;
    private double y = 0;
    private double z = 0;

    /**
     * Creates a new ExampleCommand.
     *
     * @param factory Factory object needed for Sliders
     */
    public PiCommand(Factory f, PiSubsystem subsystem) {
        factory = f;
        piSubsystem = subsystem;

        xSlider = factory.getSlider("xSlider", 0, 0, 1280);
        ySlider = factory.getSlider("ySlider", 0, 0, 720);
        zSlider = factory.getSlider("zSlider", 0, 0, 30);

        addRequirements(piSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        x = piSubsystem.getX();
        y = piSubsystem.getY();
        z = piSubsystem.getZ();

        xSlider.setDouble(x);
        ySlider.setDouble(y);
        zSlider.setDouble(z);
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
