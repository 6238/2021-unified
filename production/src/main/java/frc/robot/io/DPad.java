package frc.robot.io;

import java.util.Map;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class DPad extends Trigger {
    private final Joystick joystick;
    private int degree = -1;
    private final int position;

    public enum DPadPosition {
        kDefault, kTop, kTopRight, kRight, kBottomRight, kBottom, kBottomLeft, kLeft, kTopLeft
    }

    private final Map<DPadPosition, Integer> positionMap = Map.ofEntries(Map.entry(DPadPosition.kDefault, 0),
            Map.entry(DPadPosition.kTop, 1), Map.entry(DPadPosition.kTopRight, 2), Map.entry(DPadPosition.kRight, 3),
            Map.entry(DPadPosition.kBottomRight, 4), Map.entry(DPadPosition.kBottom, 5),
            Map.entry(DPadPosition.kBottomLeft, 6), Map.entry(DPadPosition.kLeft, 7),
            Map.entry(DPadPosition.kTopLeft, 8));

    public DPad(Joystick joystick, DPadPosition position) {
        this.joystick = joystick;
        this.position = positionMap.get(position);
    }

    @Override
    public boolean get() {
        degree = joystick.getPOV();
        // System.out.println(degree);
        return (degree < 0 && position == 0) || Math.floor((degree - 22) / 45 + 2) % 8 == position;
    }
}
