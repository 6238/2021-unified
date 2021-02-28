// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;

import frc.robot.commands.PiCommand;
import frc.robot.commands.RandomDriveCommand;
import frc.robot.commands.DriveCommand;

import frc.robot.subsystems.Factory;
import frc.robot.subsystems.PiSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final Factory factory = new Factory();
    private final Joystick joystick;
    
    private final PiSubsystem piSubsystem;
    private final DriveSubsystem driveSubsystem;

    private final PiCommand piCommand;
    private final DriveCommand driveCommand;

    // private final RandomDriveCommand randomDriveCommand;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        joystick = new Joystick(OIConstants.JOYSTICK_A);
        
        piSubsystem = new PiSubsystem(factory);
        driveSubsystem = new DriveSubsystem(factory);

        piCommand = new PiCommand(factory, piSubsystem);
        driveCommand = new DriveCommand(factory, driveSubsystem, joystick);
        // randomDriveCommand = new RandomDriveCommand(driveSubsystem, piSubsystem);

        // randomDriveCommand.schedule();

        piSubsystem.setDefaultCommand(piCommand);
        driveSubsystem.setDefaultCommand(driveCommand);

        // System.out.println(randomDriveCommand.isScheduled());

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
     * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        new JoystickButton(joystick, 1).whenPressed(new RandomDriveCommand(driveSubsystem, piSubsystem, factory));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return null;
    }
}
