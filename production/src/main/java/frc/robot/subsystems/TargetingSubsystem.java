// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TargetConstants;

public class TargetingSubsystem extends SubsystemBase {
    private final double xCenter; // x coordinate of center of screen
    private final double yCenter; // y coordinate of center of screen

    /** Creates a new TargetingSubsystem. */
    public TargetingSubsystem() {
        this.xCenter = TargetConstants.X_CENTER;
        this.yCenter = TargetConstants.Y_CENTER;
    }

    /**
     * used to determine whether velocity is positive or negative based on y
     * position
     */
    public double getVelocity(double y, double speed) {
        if (y > yCenter) { // if robot is too close
            return -speed; // velocity is neg
        } else if (y < yCenter) { // if robot is too far away
            return speed; // velocity is pos
        }
        return 0.0; // robot is aligned with center so robot should stop moving forward/back
    }

    public double getAngle(double x) { // angle to be rotated
        return x < 0 ? 0 : x - xCenter;
    }

    /** checks if robot is centered */
    public boolean isCentered(double x, double y, double y_orig) {
        return xCentered(x) && y == y_orig;
    }

    public boolean xCentered(double x) {
        return xCenter == x;
    }
}
