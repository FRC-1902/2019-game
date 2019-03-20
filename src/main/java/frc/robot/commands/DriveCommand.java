package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.FakePIDSource;
import frc.robot.OI;
import frc.robot.RevColorDistance;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.vision.VisionThread;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

public class DriveCommand extends Command {
    Robot robot;
    DriveSubsystem driveSubsystem;
    boolean hasVision, shiftToggle = false, isShifted = true;
    VisionThread vision;
    ByteBuffer dist;
    ByteArrayInputStream byteStream;

    RevColorDistance distance;

    public DriveCommand(Robot robot) {
        this.robot = robot;
        hasVision = false;
        distance = new RevColorDistance();
    }

    public DriveCommand(Robot robot, VisionThread vision) {
        this.robot = robot;
        this.vision = vision;
        hasVision = true;
        distance = new RevColorDistance();
    }

    public double getPixelOffset(){
        return 0;
    }

    public boolean isTargetValid(){
        return false;
    }

    @Override
    public void onInit() {
        driveSubsystem = Robot.driveSubsystem;
    }

    @Override
    public void onLoop() {
        //System.out.println("Drivey boi");
        //dist = distance.getDistance();
        //short dShort = dist.getShort();
        //int dInt = Byte.toUnsignedInt(dist.get(1)) * 256 + Byte.toUnsignedInt(dist.get(0));

        //System.out.println("Upper: " + Byte.toUnsignedInt(dist.get(1)) + " Lower: " + Byte.toUnsignedInt(dist.get(0)) + " Distance: " + dInt);

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

        if (OI.driveController.x.get() && hasVision) {
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
           continuousAlign.run();
        } else if (OI.driveController.leftBumper.get()) {
            driveSubsystem.arcadeDrive(0, -0.5);
        } else {
            driveSubsystem.arcadeDrive(x, y);
            //System.out.println("Heading: " + driveSubsystem.getHeading());
        }


    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }

    Runnable autoLock = new Runnable() {
        @Override
        public void run() {
            if (vision.getTargetIsValid()) {
                driveSubsystem.shift(true);
                PIDController turn = new PIDController(null, driveSubsystem.getGyro(), 0.02, 0.005, 0);
                turn.setRotational(true);
                turn.setFinishedTolerance(0.5);
                turn.enable();
                double angleOffset = (vision.getTargetCenter() - 427) * (59.7 / 854);
                turn.setTarget(driveSubsystem.getHeading() + angleOffset);
                long lastTime = System.currentTimeMillis();
                while (!turn.isDone() && OI.driveController.x.get()) { //Math.abs(vision.getTargetCenter() - 320) > 45
                    driveSubsystem.arcadeDrive(turn.getMotorPower(), 0);
                    System.out.println("(Locked) Error: " + turn.getCurrentError() + " Latency : " + (System.currentTimeMillis() - lastTime));
                    lastTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(15);
                    } catch (Exception e) {
                    }
                    //System.out.println("Error: " + turn.getCurrentError() + "Power: " + turn.getMotorPower());
                }
                while (OI.driveController.x.get()) {
                    driveSubsystem.arcadeDrive(turn.getMotorPower(), -OI.driveController.getY());
                    System.out.println("Error: " + turn.getCurrentError() + " Latency : " + (System.currentTimeMillis() - lastTime));
                    lastTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(15);
                    } catch (Exception e) {
                    }

                }
            }
        }
    };

    Runnable continuousAlign = new Runnable() {
        @Override
        public void run() {
            driveSubsystem.shift(false);
            FakePIDSource fakePIDSource = new FakePIDSource();
            PIDController turn = new PIDController(null, fakePIDSource, 0.02, 0.005, 0);
            turn.setRotational(true);
            turn.setFinishedTolerance(0.5);
            turn.enable();
            turn.setTarget(0);
            while (OI.driveController.x.get()) {
                if (isTargetValid()) {
                    fakePIDSource.setCurrent(getPixelOffset());
                    driveSubsystem.arcadeDrive(turn.getMotorPower(), Math.pow(Utils.deadzone(OI.driveController.getY(), 0.1), 2) * Utils.sign(OI.driveController.getY()));
                } else {
                    driveSubsystem.arcadeDrive(Math.pow(Utils.deadzone(OI.driveController.getX2(), 0.1), 2) * Utils.sign(OI.driveController.getX2()), Math.pow(Utils.deadzone(OI.driveController.getY(), 0.1), 2) * Utils.sign(-OI.driveController.getY()));
                }
            }
        }
    };
}
