// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.io.BoardManager;
import edu.wpi.first.networktables.NetworkTableEntry;

public class PdpSubsystem extends SubsystemBase {
  private final PowerDistributionPanel pdp;
  private final NetworkTableEntry voltageEntry;
  private final NetworkTableEntry temperatureEntry;
  private final NetworkTableEntry totalCurrentEntry;
  private final NetworkTableEntry totalEnergyEntry;

  public PdpSubsystem() {
    pdp = new PowerDistributionPanel();
    voltageEntry = BoardManager.getManager().getTab().add("voltage", 0).getEntry();
    temperatureEntry = BoardManager.getManager().getTab().add("temperature", 0).getEntry();
    totalCurrentEntry =  BoardManager.getManager().getTab().add("totalCurrent", 0).getEntry();
    totalEnergyEntry = BoardManager.getManager().getTab().add("totalEnergy", 0).getEntry();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    voltageEntry.setNumber(pdp.getVoltage());
    temperatureEntry.setNumber(pdp.getTemperature());
    totalCurrentEntry.setNumber(pdp.getTotalCurrent());
    totalEnergyEntry.setNumber(pdp.getTotalEnergy());
  }
}
