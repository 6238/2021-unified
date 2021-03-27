// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TargetingSubsystem extends SubsystemBase {
  
  private double angle; //horizontal angle value

  //coordinates for center of screen
  private final double xCenter;
  private final double yCenter;

  private double margin; //margin of error value to allow for more leeway

  /** Creates a new TargetingSubsystem. */
  public TargetingSubsystem() {
    xCenter = 0.5; //halfway betw 0-1
    yCenter = 0.5;

    angle = 0;
    margin = 0.0;
  }

  public double getAngle(double x){ //get angle from center
    if(x != xCenter + margin){   //or if(x < (xCenter-a) || x > (xCenter+a)) where a is some value that allows for a bit more leeway
      angle = x - xCenter; //not sure abt angle and calculations
      return angle;
    }
    return 0.0;
  }

  /**used to determine whether velocity is positive or negative based on y position*/
  public double getVelocity(double y, double speed){ 
    if(y > yCenter + margin){ // if robot is too far away 
      return speed; // velocity is positive
    } else if(y < yCenter - margin){ //if robot is too close
      return -speed; //velocity is negative
    }
    return 0.0; //robot is aligned with center so robot should stop moving forward/back
  }

  /** checks if robot is centered */
  public boolean isCentered(double x, double y){
    if(x == xCenter && y == yCenter){
      return true;
    }
    return false;
  }

  public void updateMargin(double m){
    margin = m;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
