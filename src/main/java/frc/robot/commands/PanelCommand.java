package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;

import static frc.robot.Robot.panelSubsystem;

public class PanelCommand extends Command {
    boolean hasReZeroed = false, didOuttake = false, actionToggle = false;

    boolean outtakePrevious = false, clampPrevious = false;

    @Override
    public void onInit() {
        //panelSubsystem.hatchPID.setTarget(panelSubsystem.hatchPID.getCurrentSourceValue());
        //panelSubsystem.hatchPID.enable(); //67.375 to 137.5 : 2.041
        panelSubsystem.setClamp(false);
        panelSubsystem.setOuttake(false);
    }

    //250 straight up, 3000 straight out

    @Override
    public void onLoop() {
        /*if(OI.manipController.getDPad().isUp()){
            if(!actionToggle){
                actionToggle = true;
                panelSubsystem.setOuttake(true);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                panelSubsystem.setClamp(true);
            }
        } else if(OI.manipController.getDPad().isDown()){
            if(!actionToggle){
                actionToggle = true;
                panelSubsystem.setOuttake(true);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                panelSubsystem.setClamp(false);
            }
        } else{
            panelSubsystem.setOuttake(false);
            actionToggle = false;
        }*/

        if (OI.driverHeck.get()) {
            panelSubsystem.setOuttake(true);
            panelSubsystem.setClamp(false);
        }

        boolean outtake = true;
        boolean clamp = false;
        if(OI.driveController.a.get()){
            panelSubsystem.setOuttake(true);
            panelSubsystem.setClamp(true);
        } else{
            outtake = OI.manipController.leftBumper.get();
            if(outtake && (!outtakePrevious)){
                panelSubsystem.setOuttake(false);
            } else if (!outtake) {
                panelSubsystem.setOuttake(true);
            }

            clamp = OI.manipController.rightBumper.get();
            if(clamp && !clampPrevious){
                panelSubsystem.setClamp(true);
            } else if (!clamp){
                panelSubsystem.setClamp(false);
            }
        }

        outtakePrevious = outtake;
        clampPrevious = clamp;
        //panelSubsystem.hatchPID.logVerbose();
        //System.out.println("Potentiometer: " + panelSubsystem.hatchPot.getCurrentPosition());

        /*if (OI.manipController.getDPad().isLeft()) {
            panelSubsystem.hatchPID.setTarget((50) + (hasReZeroed ? 130 : 0)); //110
        } else if (OI.manipController.getDPad().isRight()) {
            panelSubsystem.hatchPID.setTarget((Robot.OutBall ? 1300 : 1450) + + (hasReZeroed ? 130 : 0)); //1650 outball: 1300
        } else if(OI.manipController.x.get()){
            panelSubsystem.hatchPID.setTarget((Robot.OutBall ? 745 : 950) + (hasReZeroed ? 130 : 0)); //outball: 745
        } else if (OI.driveController.b.get()){
            panelSubsystem.hatchPID.setTarget(50 + (hasReZeroed ? 130 : 0)); //negative for main robot
        } else {
           if(Robot.OutBall && panelSubsystem.hatchPID.getTarget() == 1500){
               panelSubsystem.hatchPID.setTarget(1650 + (hasReZeroed ? 130 : 0));
           } else if(!Robot.OutBall && panelSubsystem.hatchPID.getTarget() == 1500){
               panelSubsystem.hatchPID.setTarget(1550 + (hasReZeroed ? 130 : 0));
           }
           if(OI.manipController.a.get()){
               panelSubsystem.hatchPID.setTarget(1650);
           }
        }*/  //out ball at 1000 clicks

        /*if(OI.driveController.b.get()){
            panelSubsystem.hatchPID.setTarget(0.05); //up 0.05
        } else if (OI.manipController.getDPad().isRight()) {
            panelSubsystem.hatchPID.setTarget(0.435); // middle 0.477
        } else if (OI.manipController.getDPad().isDown()) {
            panelSubsystem.hatchPID.setTarget(0.750);//down 0.649
        } else if (OI.manipController.getDPad().isLeft()) {
            panelSubsystem.hatchPID.setTarget(0.05); //up 0.05
        } else if(OI.manipController.x.get()){
            panelSubsystem.hatchPID.setTarget(0.27);// 0.157
        }*/

        /*if (OI.manipController.getDPad().isUp()) {
            panelSubsystem.setOuttake(true);
            panelSubsystem.hatchPID.setTarget(0.435); //score // 0.477
            didOuttake = true;
        } else {
            panelSubsystem.setOuttake(false);
            if(didOuttake){
                didOuttake = false;
                panelSubsystem.hatchPID.setTarget(0.5); //0.5
            }
        }*/

        /*if (Math.abs(OI.manipController.getY()) < 0.1) {
            double pow = panelSubsystem.hatchPID.getMotorPower();
            pow = Utils.cap(pow, 0.5);
            panelSubsystem.setHatchArm(pow);
        } else {
            double pow = Utils.deadzone(OI.manipController.getY(), 0.1);
            pow *= 0.5;
            panelSubsystem.setHatchArm(pow);
            panelSubsystem.hatchPID.setTarget(panelSubsystem.hatchPID.getCurrentSourceValue());
        }*/

        /*if(OI.manipController.leftJoyButton.get()){
            panelSubsystem.hatchEncoder.reset();
            hasReZeroed = true;
        }*/
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }
}
