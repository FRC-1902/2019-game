package frc.robot.commands;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import frc.robot.Robot;
import frc.robot.positioning.PursuitControl;
import frc.robot.utils.DriveSignal;
import frc.robot.utils.Path;
import frc.robot.utils.Translation2d;

import java.util.ArrayList;
import java.util.List;

public class auto254 extends Command {

    Path firstCube;

    Path toCube;
    Path toScore;
    PursuitControl control;

    PIDController rotatePID;
    FakeMotor rotatePIDOutput;

    public auto254() {
        control = new PursuitControl();
        List<Path.Waypoint> path = new ArrayList<>();

        rotatePID = new PIDController(rotatePIDOutput, Robot.instance.drive.gyro, 0.0085, 0.001, 0);
        rotatePID.setTarget(0);
        rotatePID.enable();

        path.add(new Path.Waypoint(new Translation2d(0, 0), 50));
        path.add(new Path.Waypoint(new Translation2d(25, 0), 50));
        path.add(new Path.Waypoint(new Translation2d(60, 0), 50));
        /*path.add(new Path.Waypoint(new Translation2d(83.824, -8.487), 50));
        path.add(new Path.Waypoint(new Translation2d(98.416, -25.816), 50));
        path.add(new Path.Waypoint(new Translation2d(108.863, -48.020), 50));
        path.add(new Path.Waypoint(new Translation2d(116.572, -69.62), 50));
        path.add(new Path.Waypoint(new Translation2d(1122.011, -77.488), 50));
        path.add(new Path.Waypoint(new Translation2d(130, -83.809), 50));*/
        firstCube = new Path(path);

        path.clear();
        //path.add(new Path.Waypoint(new Translation2d(0, 0), 90));
        path.add(new Path.Waypoint(new Translation2d(138, -2), 90));
        path.add(new Path.Waypoint(new Translation2d(113, -15), 40));
        path.add(new Path.Waypoint(new Translation2d(103, -40), 40));
        toCube = new Path(path);

        path.clear();
        path.add(new Path.Waypoint(new Translation2d(90, -40), 90));
        path.add(new Path.Waypoint(new Translation2d(138, -25), 90));
        toScore = new Path(path);
    }

    public void followPath(Path p, boolean reversed) {
        control.followPath(p, reversed);
        while (!control.isDone() && Robot.instance.isAutonomous() && Robot.instance.isEnabled()) {
            DriveSignal o = control.tick();
            Robot.drive.tankDrive(o.leftMotor, o.rightMotor);
            //System.out.println(p.getRemainingLength());
            if (control.isDone()) {
                Log.d("Kinda done?");
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {}
        }
        Robot.drive.tankDrive(0,0);
    }

    @Override
    public void onInit() {
        followPath(firstCube, false);
        rotatePID.setTarget(180);
        /*while(!rotatePID.isDone()){

        }*/
        try {
            Thread.sleep(10);
        } catch (Exception e) {}
        //followPath(toCube, true);
        //followPath(toScore, false);
        try {
            Thread.sleep(10);
        } catch (Exception e) {}
    }

    @Override
    public void onLoop() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
