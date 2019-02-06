package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;

public class HatchTestCommand extends Command {

    Robot r;
    boolean flipRelease = true;
    boolean outtakeRelease = true;

    public HatchTestCommand(Robot r) {
        this.r = r;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {
        if (OI.manipController.x.get()) {
            if (flipRelease) {
                Robot.panelSubsystem.flipper.set(!Robot.panelSubsystem.flipper.get());
                flipRelease = false;
            }
        } else {
            flipRelease = true;
        }

        if (OI.manipController.a.get()) {
            if (outtakeRelease) {
                Robot.panelSubsystem.outtake.set(!Robot.panelSubsystem.outtake.get());
                outtakeRelease = false;
            }
        } else {
            outtakeRelease = true;
        }

        double val = Utils.deadzone(OI.manipController.getY(), 0.1);
        val *= 0.5;
        Robot.panelSubsystem.hatchArm.set(val);
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !r.isEnabled();
    }
}
