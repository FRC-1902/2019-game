package frc.robot;

import frc.robot.Lesterbrary.*;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.DriveSubsystem;

public class DubinsFollow {
    public DriveSubsystem drive;
    public DriveCommand driveCommand;
    int clicksPerRotation = 5520;
    double wheelDiameter = 4.1;
    double clicksPerInch = clicksPerRotation / (Math.PI * wheelDiameter);

    public DubinsFollow(DriveSubsystem drive, DriveCommand driveCommand){
        this.drive = drive;
        this.driveCommand = driveCommand;
    }

    public void followArc(Arc a){
        double softResetGyro = drive.getHeading();
        double targetAngle = a.getAngle();

        double currentAngleError;
        if(a.getDirection() == Arc.Direction.LEFT){
            currentAngleError = (drive.getHeading() - softResetGyro) - targetAngle;
            driveCommand.leftBrakePID.setTarget(driveCommand.leftBrakePID.getCurrentSourceValue());
            while(currentAngleError > 0){
                currentAngleError = (drive.getHeading() - softResetGyro) - targetAngle;
                drive.left.set(driveCommand.leftBrakePID.getMotorPower());
                drive.right.set(0.5);
            }
            drive.left.set(0);
            drive.right.set(0);
        } else{
            currentAngleError = targetAngle - (drive.getHeading() - softResetGyro);
            driveCommand.rightBrakePID.setTarget(driveCommand.rightBrakePID.getCurrentSourceValue());
            while(currentAngleError > 0){
                currentAngleError = targetAngle - (drive.getHeading() - softResetGyro);
                drive.right.set(driveCommand.rightBrakePID.getMotorPower());
                drive.left.set(0.5);
            }
            drive.left.set(0);
            drive.right.set(0);
        }
    }

    public void followLine(LineSegment l){
        int clicks = (int)(l.getMagnitude() * clicksPerInch);
        int leftStart = drive.leftDriveEncoder.get();
        int rightStart = drive.rightDriveEncoder.get();

        while(((drive.leftDriveEncoder.get() - leftStart) + (drive.rightDriveEncoder.get() - rightStart)) / 2 < clicks){
            drive.left.set(0.5);
            drive.right.set(0.5);
            System.out.print(clicks + " ");
            System.out.println((((drive.leftDriveEncoder.get() - leftStart) + (drive.rightDriveEncoder.get() - rightStart)) / 2 ));
        }
        drive.left.set(0);
        drive.right.set(0);
    }

    public void followDubinsPath(DubinsPath d){
        CCCPath ccc;
        CSCPath csc;
        if(d.getType() == DubinsPath.PATHTYPE.LRL || d.getType() == DubinsPath.PATHTYPE.RLR){
            ccc = (CCCPath) d;
            System.out.println("Following Arc 1");
            followArc(ccc.getA1());
            System.out.println("Following Arc 2");
            followArc(ccc.getA2());
            System.out.println("Following Arc 3");
            followArc(ccc.getA3());
        } else{
            csc = (CSCPath) d;
            System.out.println("Following Arc 1");
            followArc(csc.getA1());
            System.out.println("Following Arc 2");
            followLine(csc.getL2());
            System.out.println("Following Arc 3");
            followArc(csc.getA3());
        }
    }
}
