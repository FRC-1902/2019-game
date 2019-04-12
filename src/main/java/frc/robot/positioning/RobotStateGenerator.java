package frc.robot.positioning;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.utils.RigidTransform2d;
import frc.robot.utils.Rotation2d;

import static frc.robot.Robot.drive;

/**
 * Periodically estimates the state of the robot using the robot's distance
 * traveled (compares two waypoints), gyroscope orientation, and velocity, among
 * various other factors. Similar to a car's odometer.
 */

public class RobotStateGenerator extends Thread  {

    static RobotStateGenerator instance_ = new RobotStateGenerator();
    boolean stop = false;

    public static RobotStateGenerator getInstance() {
        return instance_;
    }

    RobotStateGenerator() {
        robot_state_ = RobotState.getInstance();
    }

    RobotState robot_state_;
    Robot robot;
    double left_encoder_prev_distance_ = 0;
    double right_encoder_prev_distance_ = 0;

    long lastLoop = 0;


    @Override
    public void start() {
        super.start();
        left_encoder_prev_distance_ = DriveSubsystem.clicksToInches(drive.leftDriveEncoder.get());
        right_encoder_prev_distance_ = DriveSubsystem.clicksToInches(drive.rightDriveEncoder.get());
    }

    @Override
    public void run() {
        while (!stop) {
            double time = Timer.getFPGATimestamp();
            double left_distance = DriveSubsystem.clicksToInches(drive.leftDriveEncoder.get());
            double right_distance = DriveSubsystem.clicksToInches(drive.rightDriveEncoder.get());
            Rotation2d gyro_angle = Rotation2d.fromDegrees(-drive.gyro.getForPID());

            RigidTransform2d odometry = robot_state_.generateOdometryFromSensors(left_distance - left_encoder_prev_distance_,
                    right_distance - right_encoder_prev_distance_, gyro_angle);

            RigidTransform2d.Delta velocity = Kinematics.forwardKinematics(
                    DriveSubsystem.clicksToInches(drive.leftDriveEncoder.getRate()),
                    DriveSubsystem.clicksToInches(drive.rightDriveEncoder.getRate()));

            robot_state_.addObservations(time, odometry, velocity);
            left_encoder_prev_distance_ = left_distance;
            right_encoder_prev_distance_ = right_distance;

            //lastLoop = Timer.getFPGATimestamp();
        }
    }

    public void actuallyStop() {
        stop = true;
    }
}