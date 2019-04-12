package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.LiftSubsystem;

public class LiftCommand extends Command {
    LiftSubsystem liftSubsystem;
    double pow;
    boolean pidEnabeld = false;
    Long fallStop = System.currentTimeMillis();

    public LiftCommand(Robot robot) {
        this.liftSubsystem = robot.liftSubsystem;
    }

    @Override
    public void onInit() {
        liftSubsystem.setPower(0);
        liftSubsystem.liftPID.enable();
        liftSubsystem.setPosition(LiftSubsystem.LiftPosition.GROUND);
    }

    @Override
    public void onLoop() {
        //liftSubsystem.lift.set(OI.manipController.getLeftTrigger());

        if (OI.manipController.a.get()) liftSubsystem.setPosition(LiftSubsystem.LiftPosition.CARGO_SHIP);
        if (OI.manipController.b.get()) liftSubsystem.setPosition(LiftSubsystem.LiftPosition.ROCKET_2);
        if (OI.manipController.y.get()) liftSubsystem.setPosition(LiftSubsystem.LiftPosition.ROCKET_3);

        if (OI.manipController.a.get() || OI.manipController.b.get() || OI.manipController.y.get()) {
            if(!pidEnabeld) liftSubsystem.liftPID.enable();

            pow = liftSubsystem.liftPID.getMotorPower();
            liftSubsystem.lift.set(pow > 0 ? pow : 0);
        } else if (OI.manipController.getRightTrigger() > 0.2) {
            liftSubsystem.liftPID.disable();
            liftSubsystem.lift.set(-OI.manipController.getRightTrigger() * 0.3);
        } else if (OI.manipController.getLeftTrigger() > 0.2) {
            liftSubsystem.liftPID.disable();
            liftSubsystem.lift.set(OI.manipController.getLeftTrigger() * 0.8);
        } else if (Robot.OutBall) {
            liftSubsystem.liftPID.disable();
            liftSubsystem.lift.set(liftSubsystem.pot.getForPID() < LiftSubsystem.LiftPosition.CARGO_SHIP.value ?
                    liftSubsystem.downPID.getMotorPower() : 0);
        } else {
            liftSubsystem.liftPID.disable();
            liftSubsystem.lift.set(0);
        }

        pidEnabeld = liftSubsystem.liftPID.isEnabled();
    }

    @Override
    public void onStop() {
        liftSubsystem.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }
}
