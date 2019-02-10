package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;

import static frc.robot.Robot.panelSubsystem;

public class PanelCommand extends Command {

    @Override
    public void onInit() {
        panelSubsystem.hatchPID.setTarget(panelSubsystem.hatchEncoder.getCurrentPosition());
        panelSubsystem.hatchPID.enable();
    }

    //250 straight up, 3000 straight out

    @Override
    public void onLoop() {
        //panelSubsystem.hatchPID.logVerbose();

        if (OI.driveController.y.get()) {
            panelSubsystem.hatchPID.setTarget(110);
        } else if (OI.driveController.b.get()) {
            panelSubsystem.hatchPID.setTarget(1650);
        }
        if (!OI.driveController.rightBumper.get()) {
            double pow = panelSubsystem.hatchPID.getMotorPower();
            pow = Utils.cap(pow, 0.5);
            panelSubsystem.setHatchArm(pow);
        } else {

            double pow = Utils.deadzone(OI.driveController.getY(), 0.1);
            //pow *= 0.5;
            //System.out.println("Pow: " + pow);
            panelSubsystem.setHatchArm(pow);
        }

        if(OI.driveController.a.get()){
            panelSubsystem.setOuttake(true);
        } else{
            panelSubsystem.setOuttake(false);
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
