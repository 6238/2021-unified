// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
    }

    public static final class OIConstants {
        public static final String DEFAULT_TAB = "SmartDashboard";
        public static final int JOYSTICK_A = 0;
    }

    public static final int TIMEOUT_MS = 0;
    public static final int PID_LOOP_IDX = 0;

}
