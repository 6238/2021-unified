/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    // Drive Motors
    public static final int DRIVE_LEFT_MOTOR_A = 34;
    public static final int DRIVE_LEFT_MOTOR_B = 35;
    public static final int DRIVE_LEFT_MOTOR_C = 36;

    public static final int DRIVE_RIGHT_MOTOR_A = 22;
    public static final int DRIVE_RIGHT_MOTOR_B = 23;
    public static final int DRIVE_RIGHT_MOTOR_C = 24;

    // Drive

    public static final double ROTATE_THRESHOLD = 0.1;

    // Intake ID's
    public static final int THROAT_FRONT = 20;
    public static final int THROAT_BACK = 30;
    public static final int ELEVATOR_FRONT = 32;
    public static final int ELEVATOR_BACK = 33;
    public static final int INTAKE_SOLENOID = 10;
    public static final int FEEDER = 21;

    // Shooter ID's
    public static final int SHOOTER_LEFT = 6;
    public static final int SHOOTER_RIGHT = 7;

    // Shooters
    public static final double INITIAL_SHOOTER = 0.0;

    // ShuffleBoard
    public static final String DEFAULT_TAB = "SmartDashboard";

    // Joysticks
    public static final int JOYSTICK_A = 0;

    // Joystick buttons
    public static final int ELEVATOR_BUTTON = 3;
    public static final int FEEDER_REVERSE_BUTTON = 4;
    public static final int ELEVATOR_REVERSE_BUTTON = 5;
    public static final int FEEDER_BUTTON = 6;
}
