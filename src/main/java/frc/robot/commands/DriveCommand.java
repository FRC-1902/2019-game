package frc.robot.commands;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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
    boolean hasVision, shiftToggle = false, isShifted = false, isBrake = false, isTracking = false, alignHasRun = false; //TODO: on ham solo shifting may be backwards
    VisionThread vision;
    ByteBuffer dist;
    ByteArrayInputStream byteStream;
    public PIDController leftBrakePID, rightBrakePID;

    RevColorDistance distance;
    NetworkTable table;
    NetworkTableEntry tx, ta, ts, cameraMode, ledMode, camtran;

    DriveCommand instance;
    boolean justLimelighted = false;
    double limelightHoldTarget = 0;

    PIDController turn = new PIDController(null, Robot.driveSubsystem.getGyro(), 0.02, 0, 0);


    public DriveCommand(Robot robot) {
        this.robot = robot;
        hasVision = false;
        distance = new RevColorDistance();
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
        cameraMode = table.getEntry("camMode");
        ledMode = table.getEntry("ledMode");
        camtran = table.getEntry("camtran");
        cameraMode.setNumber(0);
        ledMode.setNumber(0);
    }

    public DriveCommand(Robot robot, VisionThread vision) {
        this.robot = robot;
        this.vision = vision;
        hasVision = true;
        distance = new RevColorDistance();
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
        cameraMode = table.getEntry("pipeline");
        cameraMode.forceSetNumber(0);
    }

    public double getPixelOffset() {
        double offset = tx.getDouble(0);
        return offset;
    }

    public boolean isTargetValid() {
        double area = ta.getDouble(0);
        if (area == 0 || area >= 40)
            return false;
        return true;
    }

    @Override
    public void onInit() {
        driveSubsystem = Robot.driveSubsystem;
        instance = this;
        leftBrakePID = new PIDController(new FakeMotor(), driveSubsystem.leftDriveEncoder, 0.001, 0, 0);
        leftBrakePID.enable();
        rightBrakePID = new PIDController(new FakeMotor(), driveSubsystem.rightDriveEncoder, 0.001, 0, 0);
        rightBrakePID.enable();
    }

    @Override
    public void onLoop() {
        //Log.v("Tx: " + getPixelOffset());
        //System.out.println("Drivey boi");
        //dist = distance.getDistance();
        //short dShort = dist.getShort();
        //int dInt = Byte.toUnsignedInt(dist.get(1)) * 256 + Byte.toUnsignedInt(dist.get(0));

        //System.out.println("Upper: " + Byte.toUnsignedInt(dist.get(1)) + " Lower: " + Byte.toUnsignedInt(dist.get(0)) + " Distance: " + dInt);

        double x = OI.driveController.getY2();
        double y = OI.driveController.getY();

        y = -y;

        x = Math.pow(Utils.deadzone(x, 0.1), 2) * Utils.sign(x);
        y = Math.pow(Utils.deadzone(y, 0.1), 2) * Utils.sign(y);

        if (x != 0 || y != 0) {
            justLimelighted = false;
            turn.disable();
        }

        /*if (OI.driveController.rightTrigger.get()) {
            if (!shiftToggle) {
                shiftToggle = true;
                isShifted = !isShifted;
            }
        } else {
            shiftToggle = false;
        }*/
        if (OI.driveController.rightTrigger.get()) {
            driveSubsystem.shift(false);
        } else {
            driveSubsystem.shift(true);
        }

        if (OI.driveController.leftTrigger.get())
        {
            x = x / 3.0;
            y = y / 3.0;
        }
        //driveSubsystem.shift(isShifted);
        //System.out.println(driveSubsystem.getHeading());

        if (OI.driveController.y.get()) {
            driveSubsystem.resetGyro();
        }

        if (OI.driveVision.get()) {
            justLimelighted = false;
        }
        if (justLimelighted) {

            if (!turn.isEnabled()) {
                turn.enable();
                turn.setTarget(limelightHoldTarget);
            }
            turn.logVerbose();
            driveSubsystem.arcadeDrive(-turn.getMotorPower(), 0);
        } else {
            if (OI.driveVision.get()) {
                driveSubsystem.arcadeDrive(0, 0);
                isTracking = true;
                cameraMode.setNumber(0); //TODO: Set to 0 for sublime align, 3 for solvePNP
                ledMode.setNumber(0);
            /*if(!alignHasRun){
                dubinsPath.run();
            }*/
                sublimeAlign.run();
            } else if (OI.driveController.rightBumper.get()) {
                driveSubsystem.arcadeDrive(0, -0.5);
            } else if (OI.driveController.getDPad().isRight()) {
                driveSubsystem.arcadeDrive(-1, 0.5);
            } else if (OI.driveController.getDPad().isLeft()) {
                driveSubsystem.arcadeDrive(-1, -0.5);
            } else if (!OI.driveController.a.get()) {
                //System.out.println("TX: " + tx.getDouble(0));

                driveSubsystem.arcadeDrive(x, y);
                //System.out.println("Heading: " + driveSubsystem.getHeading());
            }
        }

       /*if(OI.driveController.a.get()){
           if(!isBrake){
               isBrake = true;
               rightBrakePID.setTarget(rightBrakePID.getCurrentSourceValue());
           }
           driveSubsystem.right.set(rightBrakePID.getMotorPower());
           driveSubsystem.left.set(0.5);
       } else{
           isBrake = false;
       }*/

        if (!OI.driveVision.get()) {
            isTracking = false;
            alignHasRun = false;
        }

        if (!isTracking) {
            cameraMode.setNumber(1);
            ledMode.setNumber(1);
        }

       /*if(isTracking && cameraMode.getNumber(0).equals(0)){
           cameraMode.forceSetNumber(1);
       } else if(!isTracking && cameraMode.getNumber(0).equals(1)){
           cameraMode.forceSetNumber(0);
       }*/

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.self.isEnabled();
    }

    Runnable sublimeAlign = new Runnable() {
        @Override
        public void run() {
            driveSubsystem.shift(true);
            FakePIDSource fakePIDSource = new FakePIDSource();
            PIDController turn = new PIDController(null, fakePIDSource, 0.02, 0, 0);
            turn.setRotational(true);
            turn.setFinishedTolerance(0.5);
            turn.enable();
            turn.setTarget(0);
            System.out.println("Running before while");
            while (OI.driveVision.get()) {
                System.out.println("TS: " + ts.getDouble(0));

                double driverX = Math.pow(Utils.deadzone(OI.driveController.getX2(), 0.1), 2) * Utils.sign(OI.driveController.getX2());
                double driverY = Math.pow(Utils.deadzone(OI.driveController.getY(), 0.1), 2) * Utils.sign(-OI.driveController.getY());
                if (OI.driveController.leftTrigger.get())
                {
                    driverX = driverX / 3.0;
                    driverY = driverY / 3.0;
                }
                if (isTargetValid()) {
                    fakePIDSource.setCurrent(getPixelOffset());
                    driveSubsystem.arcadeDrive(-turn.getMotorPower(), driverY);
                } else {
                    driveSubsystem.arcadeDrive(driverX, driverY);
                }
            }
            justLimelighted = true;
            limelightHoldTarget = Robot.driveSubsystem.getGyro().getForPID();
        }
    };
}
