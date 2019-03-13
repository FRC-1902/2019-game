package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;

import static frc.robot.Robot.panelSubsystem;

public class PanelCommand extends Command {
    boolean hasReZeroed = false;
    @Override
    public void onInit() {
        panelSubsystem.hatchPID.setTarget(panelSubsystem.hatchEncoder.getCurrentPosition());
        panelSubsystem.hatchPID.enable(); //67.375 to 137.5 : 2.041
    }

    //250 straight up, 3000 straight out

    @Override
    public void onLoop() {
        //panelSubsystem.hatchPID.logVerbose();
        System.out.println("Encoder: " + panelSubsystem.hatchEncoder.getCurrentPosition());

        if (OI.manipController.y.get()) {
            panelSubsystem.hatchPID.setTarget((50) + (hasReZeroed ? 130 : 0)); //110
        } else if (OI.manipController.b.get()) {
            panelSubsystem.hatchPID.setTarget((Robot.OutBall ? 1300 : 1450) + + (hasReZeroed ? 130 : 0)); //1650 outball: 1300
        } else if(OI.manipController.x.get()){
            panelSubsystem.hatchPID.setTarget((Robot.OutBall ? 745 : 950) + (hasReZeroed ? 130 : 0)); //outball: 745
        } else if (OI.driveController.leftBumper.get()){
            panelSubsystem.hatchPID.setTarget(50 + (hasReZeroed ? 130 : 0)); //negative for main robot
        } else {
           if(Robot.OutBall && panelSubsystem.hatchPID.getTarget() == 1500){
               panelSubsystem.hatchPID.setTarget(1650 + (hasReZeroed ? 130 : 0));
           } else if(!Robot.OutBall && panelSubsystem.hatchPID.getTarget() == 1500){
               panelSubsystem.hatchPID.setTarget(1550 + (hasReZeroed ? 130 : 0));
           }
           /*if(OI.manipController.a.get()){
               panelSubsystem.hatchPID.setTarget(1650);
           }*/
        }  //out ball at 1000 clicks

        if (!OI.manipController.rightBumper.get()) {
            double pow = panelSubsystem.hatchPID.getMotorPower();
            pow = Utils.cap(pow, 0.5);
            panelSubsystem.setHatchArm(pow);
        } else {

            double pow = Utils.deadzone(OI.manipController.getY(), 0.1);
            pow *= 0.5;
            System.out.println("Pow: " + pow);
            panelSubsystem.setHatchArm(pow);
            if(OI.manipController.leftJoyButton.get()){
                panelSubsystem.hatchEncoder.reset();
                hasReZeroed = true;
            }
        }

        if(OI.manipController.leftTrigger.get()){
            panelSubsystem.setOuttake(true);
            panelSubsystem.hatchPID.setTarget(Robot.OutBall ? 1500 : 1500); //outball: 1300
        } else{
            panelSubsystem.setOuttake(false);
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }
}
