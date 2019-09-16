package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.PanelSubsystem2;

public class PanelWheelyCommand extends Command {
    PanelSubsystem2 panelSubsystem2;
    public PanelWheelyCommand(Robot robot) {
        panelSubsystem2 = robot.PanelWheelySubsystem;
    }


    @Override
    public void onInit() {
       panelSubsystem2.set(0);
    }

    @Override
    public void onLoop() {
        if (OI.manipController.getDPad().isRight()) {
            panelSubsystem2.set(0.5);
        } else {
            panelSubsystem2.set(0);
        }
    }

    @Override
    public void onStop() {
        panelSubsystem2.set(0);
    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }
}
