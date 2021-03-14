// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.TargetingSubsystem;
import frc.robot.subsystems.PiSubsystem;
import frc.robot.io.Slider;

public class TargetingCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final TargetingSubsystem targetingSubsystem;
    private final PiSubsystem piSubsystem;

    private double x = 0.5;
    private double y = 0.6;
    private double z;

    private final Slider absSpeed;
    private double speed;

    private final Slider rotMagnitudeSlider;
    private double rot;

    public static boolean tripped = false;

    /** Creates a new TargetingCommand. */
    public TargetingCommand(Factory f, DriveSubsystem driveSubsystem, TargetingSubsystem targetingSubsystem,
            PiSubsystem piSubsystem) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.driveSubsystem = driveSubsystem;
        this.targetingSubsystem = targetingSubsystem;
        this.piSubsystem = piSubsystem;

        x = piSubsystem.getX();
        y = piSubsystem.getY();
        z = piSubsystem.getZ();

        rotMagnitudeSlider = f.getSlider("rotation magnitude", 0.0, 0.0, 1.0);

        absSpeed = f.getSlider("absSpeed", 0.0, 0.0, 1.0);
        speed = targetingSubsystem.getSpeed(y, absSpeed.getDouble());

        addRequirements(driveSubsystem, targetingSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        tripped = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        x = piSubsystem.getX();
        y = piSubsystem.getY();
        z = piSubsystem.getZ();

        speed = targetingSubsystem.getSpeed(y, absSpeed.getDouble());
        if (targetingSubsystem.getAngle(x) == 0) {
            rot = 0.0;
        } else if (targetingSubsystem.getAngle(x) > 0) {
            rot = rotMagnitudeSlider.getDouble();
        } else {
            rot = -rotMagnitudeSlider.getDouble();
        }
        if (!tripped) {
            driveSubsystem.drive(speed, rot);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return tripped || targetingSubsystem.isCentered(x, y);
    }
}
