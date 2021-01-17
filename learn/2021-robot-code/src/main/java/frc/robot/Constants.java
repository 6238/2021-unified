/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class DriveConstants {
        // motor IDs
        public static final int DRIVE_LEFT_MOTOR_A = 1;
        public static final int DRIVE_LEFT_MOTOR_B = 2;
        public static final int DRIVE_LEFT_MOTOR_C = 3;

        public static final int DRIVE_RIGHT_MOTOR_A = 4;
        public static final int DRIVE_RIGHT_MOTOR_B = 5;
        public static final int DRIVE_RIGHT_MOTOR_C = 6;

        // input thresholds
        public static final double SPEED_THRESHOLD = 0.1;
        public static final double ROTATE_THRESHOLD = 0.05;
    }

    public static final class ShooterConstants {
        // motor IDs
        public static final int SHOOTER_LEFT = 7;
        public static final int SHOOTER_RIGHT = 8;

        public static final double INITIAL_SHOOTER_SPEED = 0.0;
    }

    public static final class IntakeConstants {
        // motor IDs
        public static final int THROAT_FRONT = 9;
        public static final int THROAT_BACK = 10;
        public static final int ELEVATOR_FRONT = 11;
        public static final int ELEVATOR_BACK = 12;
        public static final int FEEDER = 13;
    }

    public static final class OIConstants {
        public static final String DEFAULT_TAB = "SmartDashboard";
        public static final int JOYSTICK_A = 0;

        public static final int SHOOTER_BUTTON = 1;
        public static final int THROAT_BUTTON = 2;
        public static final int ELEVATOR_BUTTON = 3;
        public static final int FEEDER_REVERSE_BUTTON = 4;
        public static final int ELEVATOR_REVERSE_BUTTON = 5;
        public static final int FEEDER_BUTTON = 6;
    }
}