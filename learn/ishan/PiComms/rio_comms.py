"""
connect to network tables server
generate random numbers
time module for delay
"""
from networktables import NetworkTables
from random import random
import time

# As a client to connect to a robot
NetworkTables.initialize(server='roborio-6238-frc.local')

sd = NetworkTables.getTable('SmartDashboard')

while True:
    x = random() * 1280 - 640
    y = random() * 720 - 360

    sd.putNumber('x', x)
    sd.putNumber('y', y)

    time.sleep(1)
