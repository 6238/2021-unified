// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TargetingSubsystem extends SubsystemBase {
  private double x;
  private double y;
  private double z;

  private double angle; //horizontal angle value

  private double speed;

  private final double xCenter; //x coordinate of center of screen
  private final double yCenter;

  /** Creates a new TargetingSubsystem. */
  public TargetingSubsystem(Factory f) {
    speed = 0.0;

    xCenter = 0.5;
    yCenter = 0.5;

    //values from camera, periodically updated ?
    x = 0; 
    y = 0;
    z = 0;

    angle = 0;

  }

  public double getAngle(double xValue){ //angle to be rotated
    x = xValue;
    if(x != xCenter){   //or if(x < (xCenter-a) || x > (xCenter+a)) where a is some value that allows for a bit more leeway
      angle = x - xCenter; //not sure abt angle and calculations
      return angle;
    }
    return 0.0;
  }

  public double getSpeed(double yValue, double absSpeed){
    y = yValue;
    if(y > yCenter){ //robot is too far away 
      speed = absSpeed;
      return speed;
    } else if(y < yCenter){ //robot is too close
      speed = -absSpeed;
      return speed;
    }
    return 0.0;
  }

  public boolean isCentered(double x, double y){
    if(x == xCenter && y == yCenter){
      return true;
    }
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
