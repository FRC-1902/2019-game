package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.OutBallSubsystem;

import java.nio.ByteBuffer;

public class IntakeCommand extends Command {
    IntakeSubsystem intakeSubsystem;
    ByteBuffer dist;
    double pidOutput;
    OutBallSubsystem outBallSubsystem;
    boolean isDelay = false;

    public IntakeCommand(Robot robot){
        intakeSubsystem = robot.intakeSubsystem;
        outBallSubsystem = robot.outBallSubsystem;
        intakeSubsystem.intakePID.setTarget(intakeSubsystem.intakeEncoder.getCurrentPosition());
        intakeSubsystem.intakePID.enable();
    }

    @Override
    public void onInit() {
        intakeSubsystem.setArmPower(0);
        intakeSubsystem.setIntakePower(0); //up: 0 down 1060
        intakeSubsystem.setConveyorPower(0);
    }

    @Override
    public void onLoop() {

        //intakeSubsystem.setArmPower(OI.manipController.getY() * 0.5);
        //System.out.println("Encoder: " + intakeSubsystem.intakeEncoder.getCurrentPosition() + " PID: " + intakeSubsystem.intakePID.getMotorPower() + " Arm Power: " + intakeSubsystem.intakeArm.getMotorOutputPercent());
        //intakeSubsystem.intakePID.logVerbose();

        if(OI.manipController.x.get()){
            intakeSubsystem.intakePID.setTarget(1000);
        } else{
            intakeSubsystem.intakePID.setTarget(0);
        }

            pidOutput = intakeSubsystem.intakePID.getMotorPower();
            pidOutput = Utils.cap(pidOutput,0.5);
            intakeSubsystem.setArmPower(pidOutput);

        dist = intakeSubsystem.distance.getDistance();
        int dInt = Byte.toUnsignedInt(dist.get(1)) * 256 + Byte.toUnsignedInt(dist.get(0));

        if(OI.manipController.getDPad().isDown() && dInt < 1000){
            intakeSubsystem.setIntakePower(1);
            intakeSubsystem.setConveyorPower(-1);
            outBallSubsystem.set(0.2);
            isDelay = true;
        } else{
            if(isDelay){
                intakeSubsystem.setIntakePower(1);
                intakeSubsystem.setConveyorPower(-1);
                outBallSubsystem.set(0.2);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isDelay = false;
            }
            intakeSubsystem.setIntakePower(0);
            intakeSubsystem.setConveyorPower(-OI.manipController.getRightTrigger());
            outBallSubsystem.set(OI.manipController.getLeftTrigger());
        }

        //System.out.println("Upper: " + Byte.toUnsignedInt(dist.get(1)) + " Lower: " + Byte.toUnsignedInt(dist.get(0)) + " Distance: " + dInt);
    }

    @Override
    public void onStop() {
        intakeSubsystem.setArmPower(0);
        intakeSubsystem.setIntakePower(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
