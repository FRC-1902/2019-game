package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.VisionThread;
import frc.robot.subsystems.DriveSubsystem;

public class DriveCommand extends Command {
    Robot robot;
    DriveSubsystem driveSubsystem;
    boolean hasVision;
    VisionThread vision;

    public DriveCommand(Robot robot) {
        this.robot = robot;
        hasVision = false;
    }

    public DriveCommand(Robot robot, VisionThread vision){
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

        driveSubsystem.shift(OI.driveController.rightTrigger.get());

        System.out.println(driveSubsystem.getHeading());

        if(OI.driveController.y.get()){
            driveSubsystem.resetGyro();
        }

        if(OI.driveController.a.get()) {
            driveSubsystem.setLeft(0.75);
            driveSubsystem.setRight(0);
        } else if(OI.driveController.b.get()) {
            driveSubsystem.setRight(0.75);
            driveSubsystem.setLeft(0);
        } else if(OI.driveController.x.get() && hasVision){
            try{
                double distance = vision.getDistance();
                if(distance > 35){
                    double kP = 0.001;
                    driveSubsystem.arcadeDrive(vision.getOffset() * kP, 0.2);
                    Log.e("Offset: " + vision.getOffset());
                } else{
                    driveSubsystem.arcadeDrive(0,0);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else{
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
