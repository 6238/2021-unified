package frc.robot.io;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ToggleButton extends Trigger {
    private final SimpleWidget value;
    private final boolean defaultValue;

    public ToggleButton(String buttonName, boolean defaultValue) {
        this.defaultValue = defaultValue;
        value = BoardManager.getManager().getTab().add(buttonName, defaultValue)
                .withWidget(BuiltInWidgets.kToggleButton);
    }

    @Override
    public boolean get() {
        return value.getEntry().getBoolean(defaultValue);
    }

    public void set(boolean input) {
        value.getEntry().setBoolean(input);
    }
}
