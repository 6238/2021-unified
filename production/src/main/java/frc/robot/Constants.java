/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.io.DPad.DPadPosition;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class DriveConstants {
        // motor IDs
        public static final int DRIVE_LEFT_MOTOR_A = 34;
        public static final int DRIVE_LEFT_MOTOR_B = 35;
        public static final int DRIVE_LEFT_MOTOR_C = 36;

        public static final int DRIVE_RIGHT_MOTOR_A = 22;
        public static final int DRIVE_RIGHT_MOTOR_B = 23;
        public static final int DRIVE_RIGHT_MOTOR_C = 24;

        // input thresholds
        public static final double SPEED_THRESHOLD = 0.1;
        public static final double ROTATE_THRESHOLD = 0.05;
 
        public static final double MAX_SPEED = 3400;

        public static final double kP = 0.0000084;
        public static final double kI = 0.0042;
        public static final double kD = 0.0000042;
        /*
         * public static final double kP = 0.0001; public static final double kI = 0.0;
         * public static final double kD = 0.0;
         */

        public static final double kS = 1.06;
        public static final double kV = 0.00377;
        public static final double kA = 0.000218;
    }

    public static final class ShooterConstants {
        // motor IDs
        public static final int SHOOTER_LEFT = 6;
        public static final int SHOOTER_RIGHT = 7;

        public static final int SHOOTER_SOLENOID_FORWARD = 2;
        public static final int SHOOTER_SOLENOID_REVERSE = 3;

        public static final double INITIAL_SHOOTER_SPEED = 0.5;

        public static final double[] SHOOTER_SPEEDS = { 0.36, 0.522, 0.50, 0.51 }; // 0 top, 1 right, 2 bottom, 3 left
    }

    public static final class IntakeConstants {
        // motor IDs
        public static final int ELEVATOR_LEFT = 32;
        public static final int ELEVATOR_RIGHT = 20;
        public static final int FEEDER = 21;
    }

    public static final class TargetConstants {
        public static final double X_CENTER = 0.5;
        public static final double Y_CENTER = 0.5;
    }

    public static final class OIConstants {
        public static final String DEFAULT_TAB = "SmartDashboard";
        public static final int JOYSTICK_A = 0;

        public static final int SHOOTER_BUTTON = 1;
        public static final int INTAKE_BUTTON = 2;
        public static final int ELEVATOR_REVERSE_BUTTON = 3;
        public static final int FEEDER_REVERSE_BUTTON = 4;
        public static final int ELEVATOR_BUTTON = 5;
        public static final int FEEDER_BUTTON = 6;
        public static final int SHOOTER_SOLENOID_EXTEND_BUTTON = 7;
        public static final int SHOOTER_SOLENOID_RETRACT_BUTTON = 8;
        public static final int TARGETING_START_BUTTON = 9;
        public static final int TARGETING_END_BUTTON = 10;
        public static final int PID_DRIVE_START_BUTTON = 11;
        public static final int PID_DRIVE_END_BUTTON = 12;

        public static final DPadPosition GREEN_ZONE_POSITION = DPadPosition.kTop;
        public static final DPadPosition YELLOW_ZONE_POSITION = DPadPosition.kRight;
        public static final DPadPosition BLUE_ZONE_POSITION = DPadPosition.kBottom;
        public static final DPadPosition RED_ZONE_POSITION = DPadPosition.kLeft;
    }

    public static final int TIMEOUT_MS = 0;
    public static final int PID_LOOP_IDX = 0;
}