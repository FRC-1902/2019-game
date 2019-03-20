package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.OutBallSubsystem;

public class OutBallCommand extends Command {
    OutBallSubsystem outBallSubsystem;

    public OutBallCommand(Robot robot) {
        outBallSubsystem = robot.outBallSubsystem;
    }

    @Override
    public void onInit() {
        outBallSubsystem.set(0);
    }

    @Override
    public void onLoop() {
        outBallSubsystem.set(OI.driveController.getLeftTrigger());
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
