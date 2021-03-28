// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TargetConstants;

public class TargetingSubsystem extends SubsystemBase {
  private double x;
  private double y;
  private double z;


  
  private double angle; //horizontal angle value

  private double speed;

  private final double xCenter ; //x coordinate of center of screen
  private final double yCenter; //y coordinate of center of screen
  private final double pixelError; //Margin of Error for pixel

  /** Creates a new TargetingSubsystem. */
  public TargetingSubsystem() {
    speed = 0.0;

    this.xCenter = TargetConstants.xCenter;
    this.yCenter = TargetConstants.yCenter;
    this.pixelError = TargetConstants.pixelError; 

    //values from camera, periodically updated ?
    x = 0; 
    y = 0;
    z = 0;

    angle = 0;

  }

  public double getAngle(double xValue){ //angle to be rotated
    x = xValue;

    if ((x-pixelError) > xCenter) 
      return 1; 
    
    else if ((x+pixelError) < xCenter) 
      return -1; 

    return 0.0;
  }

  public double getSpeed(double yValue, double absSpeed){
    y = yValue;
    if(yCenter < (y-pixelError)){ //robot is too far away 
      speed = -absSpeed;
      return speed;
    } else if(yCenter > (y+pixelError)){ //robot is too close
      speed = absSpeed;
      return speed;
    }
    return 0.0;
  }

  public boolean isCentered(double x, double y){
    return (xCenter < (x+pixelError) && xCenter > (x-pixelError)) && 
           (yCenter < (y+pixelError) && yCenter > (y-pixelError));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
