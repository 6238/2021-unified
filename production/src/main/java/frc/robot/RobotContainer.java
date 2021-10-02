/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.PIDDriveCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.TargetingCommand;

import frc.robot.subsystems.Factory;
import frc.robot.subsystems.PiSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TargetingSubsystem;

import frc.robot.io.DPad;
import frc.robot.Constants.OIConstants;

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
    private final PiSubsystem piSubsystem;
    private final DriveSubsystem driveSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final TargetingSubsystem targetingSubsystem;

    private final DriveCommand driveCommand;
    private final IntakeCommand intakeCommand;
    private final ShooterCommand shooterCommand;
    private final PIDDriveCommand pidDriveCommand;
    private final TargetingCommand targetingCommand;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        joystick = new Joystick(OIConstants.JOYSTICK_A);

        piSubsystem = new PiSubsystem();
        driveSubsystem = new DriveSubsystem(factory);
        intakeSubsystem = new IntakeSubsystem(factory);
        shooterSubsystem = new ShooterSubsystem(factory);
        targetingSubsystem = new TargetingSubsystem();

        driveCommand = new DriveCommand(factory, driveSubsystem, joystick);
        intakeCommand = new IntakeCommand(factory, intakeSubsystem);
        shooterCommand = new ShooterCommand(factory, shooterSubsystem);
        pidDriveCommand = new PIDDriveCommand(driveSubsystem, joystick);
        targetingCommand = new TargetingCommand(factory, driveSubsystem, targetingSubsystem, piSubsystem);

        driveSubsystem.setDefaultCommand(driveCommand);
        intakeSubsystem.setDefaultCommand(intakeCommand);
        shooterSubsystem.setDefaultCommand(shooterCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link Joystick} or {@link XboxController}), and then passing it to a
     * {@link JoystickButton}.
     */
    private void configureButtonBindings() {
        new JoystickButton(joystick, OIConstants.SHOOTER_BUTTON).whenPressed(() -> shooterCommand.toggleShooter(true))
                .whenReleased(() -> shooterCommand.toggleShooter(false));

        new JoystickButton(joystick, OIConstants.INTAKE_BUTTON).whenPressed(() -> intakeCommand.setIntake(1))
                .whenReleased(() -> intakeCommand.setIntake(0));

        new JoystickButton(joystick, OIConstants.ELEVATOR_BUTTON).whenPressed(() -> intakeCommand.setElevator(1))
                .whenReleased(() -> intakeCommand.setElevator(0));
        new JoystickButton(joystick, OIConstants.ELEVATOR_REVERSE_BUTTON)
                .whenPressed(() -> intakeCommand.setElevator(-1)).whenReleased(() -> intakeCommand.setElevator(0));

        new JoystickButton(joystick, OIConstants.FEEDER_BUTTON).whenPressed(() -> intakeCommand.setFeeder(1))
                .whenReleased(() -> intakeCommand.setFeeder(0));
        new JoystickButton(joystick, OIConstants.FEEDER_REVERSE_BUTTON).whenPressed(() -> intakeCommand.setFeeder(-1))
                .whenReleased(() -> intakeCommand.setFeeder(0));

        new JoystickButton(joystick, OIConstants.SHOOTER_SOLENOID_EXTEND_BUTTON)
                .whenPressed(() -> shooterCommand.toggleSolenoid(1));
        new JoystickButton(joystick, OIConstants.SHOOTER_SOLENOID_RETRACT_BUTTON)
                .whenPressed(() -> shooterCommand.toggleSolenoid(-1));

        new JoystickButton(joystick, OIConstants.TARGETING_START_BUTTON).whenPressed(() -> targetingCommand.schedule());
        new JoystickButton(joystick, OIConstants.TARGETING_END_BUTTON).whenPressed(() -> targetingCommand.cancel());

        new JoystickButton(joystick, OIConstants.PID_DRIVE_START_BUTTON).whenPressed(() -> pidDriveCommand.schedule());
        new JoystickButton(joystick, OIConstants.PID_DRIVE_END_BUTTON).whenPressed(() -> pidDriveCommand.cancel());

        new DPad(joystick, OIConstants.GREEN_ZONE_POSITION).whenActive(() -> shooterCommand.setShooterSpeed(0));
        new DPad(joystick, OIConstants.YELLOW_ZONE_POSITION).whenActive(() -> shooterCommand.setShooterSpeed(1));
        new DPad(joystick, OIConstants.BLUE_ZONE_POSITION).whenActive(() -> shooterCommand.setShooterSpeed(2));
        new DPad(joystick, OIConstants.RED_ZONE_POSITION).whenActive(() -> shooterCommand.setShooterSpeed(3));
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

    public DriveSubsystem getDriveSubsystem() {
        return driveSubsystem;
    }
}
