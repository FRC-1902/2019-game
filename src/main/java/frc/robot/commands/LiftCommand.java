package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.LiftSubsystem;

public class LiftCommand extends Command {
    LiftSubsystem liftSubsystem;

    public LiftCommand(Robot robot){
        this.liftSubsystem = robot.liftSubsystem;
    }

    @Override
    public void onInit() {
        liftSubsystem.setPower(0);
    }

    @Override
    public void onLoop() {
        liftSubsystem.setPower((OI.driveController.getLeftTrigger() * 0.2) + (OI.driveController.getRightTrigger() * -0.2));
        System.out.println("Power: " + (OI.driveController.getLeftTrigger() * 0.2) + (OI.driveController.getRightTrigger() * -0.2));
    }

    @Override
    public void onStop() {
        liftSubsystem.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
