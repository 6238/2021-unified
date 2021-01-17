/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Factory;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    public final Factory factory = new Factory();
    private final Joystick joystick;
    private final DriveSubsystem driveSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final ShooterSubsystem shooterSubsystem;

    private final DriveCommand driveCommand;
    private final IntakeCommand intakeCommand;
    private final ShootCommand shootCommand;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        joystick = new Joystick(Constants.JOYSTICK_A);

        driveSubsystem = new DriveSubsystem(factory);
        intakeSubsystem = new IntakeSubsystem(factory);
        shooterSubsystem = new ShooterSubsystem(factory);

        driveCommand = new DriveCommand(factory, driveSubsystem, joystick);
        intakeCommand = new IntakeCommand(factory, intakeSubsystem);
        shootCommand = new ShootCommand(factory, shooterSubsystem/* , joystick */);

        driveSubsystem.setDefaultCommand(driveCommand);
        intakeSubsystem.setDefaultCommand(intakeCommand);
        shooterSubsystem.setDefaultCommand(shootCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link Joystick} or {@link XboxController}), and then
     * passing it to a {@link JoystickButton}.
     */
    private void configureButtonBindings() {
        new JoystickButton(joystick, Constants.SHOOTER_BUTTON).whenPressed(() -> shootCommand.toggleShooter(true))
                .whenReleased(() -> shootCommand.toggleShooter(false));
        
        new JoystickButton(joystick, Constants.ELEVATOR_BUTTON).whenPressed(() -> intakeCommand.setElevator(1)).whenReleased(() -> intakeCommand.setElevator(0));
        new JoystickButton(joystick, Constants.ELEVATOR_REVERSE_BUTTON).whenPressed(() -> intakeCommand.setElevator(-1)).whenReleased(() -> intakeCommand.setElevator(0));
        
        new JoystickButton(joystick, Constants.FEEDER_BUTTON).whenPressed(() -> intakeCommand.setFeeder(1)).whenReleased(() -> intakeCommand.setFeeder(0));
        new JoystickButton(joystick, Constants.FEEDER_REVERSE_BUTTON).whenPressed(() -> intakeCommand.setFeeder(-1)).whenReleased(() -> intakeCommand.setFeeder(0));
        
        new JoystickButton(joystick, Constants.THROAT_BUTTON).whenPressed(() -> intakeCommand.setThroat(1)).whenReleased(() -> intakeCommand.setThroat(0));
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
