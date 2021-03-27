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
    private double z; //to be used later

    private final Slider speedSlider;
    private double velocity;

    private final Slider rotMagnitudeSlider;
    private double rot;

    private final Slider marginSlider;

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

        speedSlider = f.getSlider("speed", 0.0, 0.0, 1.0); //adjust magnitude of speed
        velocity = targetingSubsystem.getVelocity(y, speedSlider.getDouble());

        marginSlider = f.getSlider("margin of error", 0.0, 0.0, 1.0);

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

        targetingSubsystem.updateMargin(marginSlider.getDouble());

        velocity = targetingSubsystem.getVelocity(y, speedSlider.getDouble());
        if (targetingSubsystem.getAngle(x) == 0) { //checks if x is centered 
            rot = 0.0;  //if centered, rotational value = 0
        } else if (targetingSubsystem.getAngle(x) > 0) { //if target is to the right of robot
            rot = -rotMagnitudeSlider.getDouble(); //robot should rotate right, so rot is pos
        } else {                                    // target is left of robot
            rot = rotMagnitudeSlider.getDouble(); //should rotate left, rot is neg
        }
        if (!tripped) {
            driveSubsystem.drive(velocity, rot);
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
