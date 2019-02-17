package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
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
        System.out.println(Utils.roundToDecimals((double)(float)((liftSubsystem.pot.getCurrentPosition())), 5));
        //liftSubsystem.setPower((OI.manipController.getLeftTrigger() * 0.2) + (OI.manipController.getRightTrigger() * -0.2));
        liftSubsystem.lift1.set(OI.manipController.getLeftTrigger());
        liftSubsystem.lift2.set(OI.manipController.getRightTrigger());
        liftSubsystem.lift3.set(OI.manipController.getY());
        liftSubsystem.lift4.set(OI.manipController.getY2());
        System.out.println("Power: " + (OI.manipController.getLeftTrigger() * 0.2) + (OI.manipController.getRightTrigger() * -0.2));
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
