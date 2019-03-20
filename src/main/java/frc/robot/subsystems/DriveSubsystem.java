package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.sensors.BNOGyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class DriveSubsystem extends Subsystem {

    public MotorGroup left, right;

    public Solenoid shift;

    WPI_VictorSPX leftDrive1;
    WPI_VictorSPX leftDrive2;
    WPI_VictorSPX leftDrive3;
    WPI_VictorSPX rightDrive1;
    WPI_VictorSPX rightDrive2;
    WPI_VictorSPX rightDrive3;
    //CanEncoder currentEncoder;
    BNOGyro gyro;

    public DriveSubsystem() {
        leftDrive1 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_1);
        leftDrive2 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_2);
        leftDrive3 = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_3);
        rightDrive1 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_1);
        rightDrive2 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_2);
        rightDrive3 = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_3);
        left = new MotorGroup(leftDrive1, leftDrive2, leftDrive3);
        left.setInverted(true);
        right = new MotorGroup(rightDrive1, rightDrive2, rightDrive3);
        shift = new Solenoid(RobotMap.SHIFTER_SOLENOID);
        gyro = new BNOGyro(true);
        gyro.rezero();
    }

    public void tankDrive(double leftPower, double rightPower) {
        /*
        left.set(leftPower);
        right.set(rightPower);
        */
    }

    public void arcadeDrive(double x, double y) {
        tankDrive(y + x, y - x);
    }

    public void stop() {
        left.set(0);
        right.set(0);
    }

    public void setLeft(double set) {
        left.set(set);
    }

    public void setRight(double set) {
        right.set(set);
    }

    public void shift(boolean b) {
        shift.set(b);
    }

    public double getHeading() {
        return gyro.getHeading();
    }

    public void resetGyro() {
        gyro.rezero();
    }

    public BNOGyro getGyro() {
        return gyro;
    }

    public int getEncoderReading() {
        return -1;
        //return currentEncoder.getCurrentPosition();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}