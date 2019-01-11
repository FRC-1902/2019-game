package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.utils.Utils;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class DriveSubsystem extends Subsystem {
    MotorGroup left, right;

    public Solenoid light;

    WPI_VictorSPX leftDrive1 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_1);
    WPI_VictorSPX leftDrive2 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_2);
    WPI_VictorSPX leftDrive3 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_3);
    WPI_VictorSPX rightDrive1 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_1);
    WPI_VictorSPX rightDrive2 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_2);
    WPI_VictorSPX rightDrive3 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_3);

    public DriveSubsystem() {
        left = new MotorGroup(leftDrive1, leftDrive2, leftDrive3);
        right = new MotorGroup(rightDrive1, rightDrive2, rightDrive3);
        left.setInverted(true);
        light = new Solenoid(1);
    }

    public void tankDrive(double leftPower, double rightPower) {
        left.set(Utils.minMax(leftPower, 0.1, 1));
        right.set(Utils.minMax(rightPower, 0.1, 1));
    }

    public void arcadeDrive(double x, double y) {
        tankDrive(y + x, y - x);
    }

    public void stop() {
        left.set(0);
        right.set(0);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}