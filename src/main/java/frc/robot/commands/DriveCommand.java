package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.vision.VisionThread;

public class DriveCommand extends Command {
    Robot robot;
    DriveSubsystem driveSubsystem;
    boolean hasVision, shiftToggle = false, isShifted = false;
    VisionThread vision;
    Runnable autoLock = new Runnable() {
        @Override
        public void run() {
            if (vision.getTargetIsValid()) {
                driveSubsystem.shift(false);
                PIDController turn = new PIDController(null, driveSubsystem.getGyro(), 0.04, 0.001, 0);
                turn.setRotational(true);
                turn.setFinishedTolerance(2);
                turn.enable();
                double angleOffset = (vision.getTargetCenter() - 320) * (51.0 / 640);
                turn.setTarget(driveSubsystem.getHeading() + angleOffset);
                while (!turn.isDone() && OI.driveController.x.get()) { //Math.abs(vision.getTargetCenter() - 320) > 45
                    driveSubsystem.arcadeDrive(turn.getMotorPower(), 0);
                    System.out.println("Error: " + turn.getCurrentError() + "Power: " + turn.getMotorPower());
                }
                while (OI.driveController.x.get()) {
                    driveSubsystem.arcadeDrive(turn.getMotorPower(), -OI.driveController.getY());
                }
            }
        }
    };

    public DriveCommand(Robot robot) {
        this.robot = robot;
        hasVision = false;
    }

    public DriveCommand(Robot robot, VisionThread vision) {
        this.robot = robot;
        this.vision = vision;
        hasVision = true;
    }

    @Override
    public void onInit() {
        driveSubsystem = Robot.driveSubsystem;
    }

    @Override
    public void onLoop() {
        double x = OI.driveController.getX2();
        double y = OI.driveController.getY();

        y = -y;

        x = Math.pow(Utils.deadzone(x, 0.1), 2) * Utils.sign(x);
        y = Math.pow(Utils.deadzone(y, 0.1), 2) * Utils.sign(y);

        if (OI.driveController.rightTrigger.get()) {
            if (!shiftToggle) {
                shiftToggle = true;
                isShifted = !isShifted;
            }
        } else {
            shiftToggle = false;
        }

        driveSubsystem.shift(isShifted);
        //System.out.println(driveSubsystem.getHeading());

        if (OI.driveController.y.get()) {
            driveSubsystem.resetGyro();
        }

        if (OI.driveController.a.get()) {
            driveSubsystem.setLeft(0.75);
            driveSubsystem.setRight(0);
        } else if (OI.driveController.b.get()) {
            driveSubsystem.setRight(0.75);
            driveSubsystem.setLeft(0);
        } else if (OI.driveController.x.get() && hasVision) {
            /*try{
                double distance = vision.getDistance();
                if(distance > 35 && vision.getTargetIsValid()){
                    double kP = 0.001;
                    driveSubsystem.arcadeDrive(vision.getOffset() * kP, 0.2);
                    Log.e("Offset: " + vision.getOffset());
                } else{
                    driveSubsystem.arcadeDrive(0,0);
                }
            } catch(Exception e){
                e.printStackTrace();
            }*/
            driveSubsystem.arcadeDrive(0, 0);
            autoLock.run();
        } else {
            driveSubsystem.arcadeDrive(x, y);
        }


    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !robot.isEnabled();
    }
}
