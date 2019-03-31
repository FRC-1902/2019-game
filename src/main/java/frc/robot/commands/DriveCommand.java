package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;

public class DriveCommand extends Command {

    @Override
    public void onInit() {
        Robot.drive.setPIDEnabled(true);
    }

    long stopStart = 0;
    long noShift = 0;

    public static double currentY = 0;

    @Override
    public void onLoop() {
        double y = Utils.deadzone(OI.driver.getY(), 0.1);
        double x = Utils.deadzone(OI.driver.getX2(), 0.1);

        y=-y;

        currentY = y;
        double nonScaledY = y;

        x = Math.pow(x, 2) * Utils.sign(x);
        y = Math.pow(y, 2) * Utils.sign(y);


        //Robot.drive.tankDrive(y + x, y - x);

        boolean driverShift = OI.driver.isRightTriggerPressed();

        if (driverShift == Robot.drive.getShift()) { //actually unequal
            Log.d("Driver shifting");
            Robot.drive.shift(!driverShift);
            if (driverShift) {
                stopStart = System.currentTimeMillis();
            } else {
                stopStart = 0;
            }
        }

        if (stopStart != 0 && System.currentTimeMillis() - stopStart <= 200) {
            //Log.d("Doin the slow");
            double max = 0.15;
            if (Math.abs(y) > max) {
                y = max * Utils.sign(y);
            }
            if (Math.abs(x) > 0.2) {
                x = max * Utils.sign(x);
            }
        } else {
            stopStart = 0;
        }

        double left = y + x, right = y - x;

        if (false && x == 0/* && y != 0*/) {
            if (!Robot.drive.rotateDrivingPID.isEnabled()) {
                Robot.drive.rotateDrivingPID.enable();
                Robot.drive.rotateDrivingPID.setTarget(Robot.drive.gyro.getForPID());
                Log.d("Enabling drive forward assist");
            }
            double pidOut = Robot.drive.rotateDrivingPIDOutput.get() * 1;
            left += pidOut;
            right -= pidOut;

            double max = Utils.maxDouble(Math.abs(left), Math.abs(right));
            left /= max;
            right /= max;

            left *= Math.abs(y);
            right *= Math.abs(y);
        } else {
            if (Robot.drive.rotateDrivingPID.isEnabled()) {
                Robot.drive.rotateDrivingPID.disable();
                Log.d("Disabled drive forward assist");
            }
        }

        if (OI.positionHoldButton.get()) {
            if (!Robot.drive.positionPID.isEnabled()) {
                Robot.drive.positionPID.setTarget(Robot.drive.driveEncoderAvg.get());
                Robot.drive.positionPID.enable();
                Log.d("enable position hold");
            }
            double out = Robot.drive.positionPIDOutput.get();
            //Log.d("Out: " + out);
            Robot.drive.tankDrive(out, out);
        } else {
            if (Robot.drive.positionPID.isEnabled()) {
                Robot.drive.positionPID.disable();
                Log.d("disable position hold");
            }

            Robot.drive.tankDrive(left, right);
        }

        //Robot.drive.tankDrive(left, right);


        //Robot.compressor.setClosedLoopControl(!OI.driver.isLeftTriggerPressed());
        //Robot.drive.shift.set(!OI.driver.isRightTriggerPressed());

    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.instance.isEnabled() || !Robot.instance.isOperatorControl();
    }
}
