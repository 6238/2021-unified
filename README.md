# 2021-unified
 One combined repository for Popcorn Penguins 6238 in the 2021 Infinite Recharge Season
## Table of Contents
1. [Installations](#installations)
2. [Sample Repositories](#sample-repositories)
3. [Using WPILib and Visual Studio Code](#using-wpilib-and-visual-studio-code)


## Installations
Install tools in this order:
1. [WPILib](#wpilib) 
2. [LabVIEW](#labview) 
3. [FRC Game Tools](#frc-game-tools) 
4. [FRC Radio Configuration](#frc-radio-configuration-2000) 
5. [3rd Party Libraries](#3rd-party-libraries) 
6. [REV Hardware Client](#rev-hardware-client) 

### WPILib
#### (macOS/Windows/Linux, required)
Necessary to write & deploy code.  
https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html

### LabVIEW
#### (Windows only, not recommended)
Necessary to do LabVIEW coding. Completely unnecessary for the Popcorn Penguins. If you decide to do this, you must do it **before** installing the FRC Game Tools and the 3rd Party Libraries.
https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/labview-setup.html

### FRC Game Tools
#### (Windows only, optional)
Necessary if you want to *control* the robot.
https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/frc-game-tools.html

### FRC Radio Configuration 20.0.0
#### (Windows only, optional)
Necessary to program the robot radios.
https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/radio-programming.html

### 3rd Party Libraries
#### (macOS/Windows/Linux, required)
- Analog Devices ADIS16470 IMU
  - https://github.com/juchong/ADIS16470-RoboRIO-Driver/releases/latest
  - Go to releases tab
  - Download the latest version (unless it says otherwise)
- CTRE Phoenix Toolsuite 
  - https://github.com/CrossTheRoadElec/Phoenix-Releases/releases
  - Download the latest version
- REV Robotics Color Sensor v3
  - https://www.revrobotics.com/rev-31-1557/
  - Scroll down to "Software Libraries"
  - "Java/C++ SDK Direct Download"
- REV Robotics Spark MAX
  - https://docs.revrobotics.com/sparkmax/software-resources/spark-max-api-information#java-api
  - "Download Latest Java API"
- **How to install:** https://docs.wpilib.org/en/stable/docs/software/vscode-overview/3rd-party-libraries.html#the-mechanism-java-c

Offline installers are preferred (allows you to install once for all the robot projects you create)  
https://docs.wpilib.org/en/stable/docs/software/vscode-overview/3rd-party-libraries.html#libraries

### REV Hardware Client
#### (Windows only, optional)
Used to configure the Spark MAX's and other REV hardware, this can also be achieved through code.
https://docs.revrobotics.com/sparkmax/rev-hardware-client/getting-started-with-the-rev-hardware-client

## Sample Repositories
List of repositories with example code:
- http://github.com/wpilibsuite/frc-docs
- http://github.com/wpilibsuite/allwpilib
- http://github.com/REVrobotics/SPARK-MAX-Examples
- http://github.com/REVrobotics/Color-Sensor-v3-Examples
- http://github.com/CrossTheRoadElec/Phoenix-Examples-Languages

## Using WPILib and Visual Studio Code

### Adding libraries to a WPILib project
- Open an existing WPILib project or create a new one
- Open the Command Palette (Cmd + Shift + P on macOS, Ctrl + Shift + P on Windows/Linux, or click the hexagon "W" icon in the top right)
- Search for and run "WPILib: Manage Vendor Libraries"
- Click "Install new libraries (Offline)"
- Check the boxes next to the libraries you want to install (ex. "CTRE-Phoenix")
- Click the blue "OK" button at the top right of the command palette dialog
- Click "Yes" at the dialog asking to build the project
- **WARNING:** In 2020 and beyond, the WPILib-New-Commands library is added to command-based projects by default. Do not install both the WPILib-New-Commands and WPILib-Old-Commands libraries in the same project.
