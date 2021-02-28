package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Slider;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.PiSubsystem;

public class RandomDriveCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final PiSubsystem piSubsystem;
    private final Slider zSlider;

    private double z;

    public RandomDriveCommand(DriveSubsystem driveSubsystem, PiSubsystem piSubsystem, Factory factory) {
        this.driveSubsystem = driveSubsystem;
        this.piSubsystem = piSubsystem;

        zSlider = factory.getSlider("zSlider", 0, 0, 30);

        addRequirements(driveSubsystem, piSubsystem);
    }

    @Override
    public void execute() {
        z = piSubsystem.getZ();
        zSlider.setDouble(z);
        driveSubsystem.drive(z/15 - 1, 0);
    }

    @Override
    public boolean isFinished() {
        return driveSubsystem.getLeftPosition() < -1800 || driveSubsystem.getLeftPosition() > 64000;
    }
}
