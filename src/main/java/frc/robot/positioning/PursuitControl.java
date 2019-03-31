package frc.robot.positioning;

import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.framework.PIDController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.utils.AdaptivePurePursuitController;
import frc.robot.utils.DriveSignal;
import frc.robot.utils.Path;
import frc.robot.utils.RigidTransform2d;

public class PursuitControl {

    FakeMotor leftOutput, rightOutput;
    public AdaptivePurePursuitController pathFollower;
    PIDController left, right;

    final double kP = 0.01, kI = 0, kD = 0; //0.01 0.004

    public PursuitControl() {
        leftOutput = new FakeMotor();
        rightOutput = new FakeMotor();
        left = new PIDController(leftOutput, Robot.drive.leftDriveEncoder, kP, kI, kD);
        right = new PIDController(rightOutput, Robot.drive.rightDriveEncoder, kP, kI, kD);
    }

    public void followPath(Path path, boolean reversed) {
        pathFollower = new AdaptivePurePursuitController(Constants.pathFollowLookahead,
                Constants.pathFollowMaxAccel, Constants.kLooperDt, path, reversed, 0.1); //.25
    }

    public boolean isDone() {
        return pathFollower.isDone();
    }

    public DriveSignal tick() {
        if (pathFollower != null && !pathFollower.isDone()) {
            if (!left.isEnabled()) left.enable();
            if (!right.isEnabled()) right.enable();
            RigidTransform2d robot_pose = RobotState.getInstance().getLatestFieldToVehicle().getValue();
            RigidTransform2d.Delta command = pathFollower.update(robot_pose, Timer.getFPGATimestamp());
            System.out.println("Current X: " + robot_pose.getTranslation().getX() + "Current Y: " + robot_pose.getTranslation().getY());
            Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);

            // Scale the commands to respect the max velocity limits
            double max_vel = 0;
            max_vel = Math.max(max_vel, Math.abs(setpoint.left));
            max_vel = Math.max(max_vel, Math.abs(setpoint.right));
            if (max_vel > Constants.pathFollowMaxVel) {
                double scaling = Constants.pathFollowMaxVel / max_vel;
                setpoint = new Kinematics.DriveVelocity(setpoint.left * scaling, setpoint.right * scaling);
            }

            //Utils.log(setpoint.left + ", " + setpoint.right);
            //Utils.log(Utils.roundToPlace((float)setpoint.left, 5) + " / " + Utils.roundToPlace((float)setpoint.right, 2));

            //TODO: velocity PID for left and right wheels receive the setpoint.left and setpoint.right values

            left.setTarget(left.getCurrentSourceValue() + setpoint.left);
            right.setTarget(right.getCurrentSourceValue() + setpoint.right);
        } else {
            left.disable();
            right.disable();
            leftOutput.set(0);
            rightOutput.set(0);
        }
        return new DriveSignal(leftOutput.get(), rightOutput.get());
    }
}