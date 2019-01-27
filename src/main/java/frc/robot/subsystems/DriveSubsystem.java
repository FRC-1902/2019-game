package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.utils.Utils;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class DriveSubsystem extends Subsystem {

    public MotorGroup left, right;

    public Solenoid shift;

    VictorSP leftDrive1;
    VictorSP leftDrive2;
    VictorSP leftDrive3;
    VictorSP rightDrive1;
    VictorSP rightDrive2;
    VictorSP rightDrive3;

    public DriveSubsystem() {
        leftDrive1 = new VictorSP(RobotMap.DRIVE_LEFT_1);
        leftDrive2 = new VictorSP(RobotMap.DRIVE_LEFT_2);
        leftDrive3 = new VictorSP(RobotMap.DRIVE_LEFT_3);
        rightDrive1 = new VictorSP(RobotMap.DRIVE_RIGHT_1);
        rightDrive2 = new VictorSP(RobotMap.DRIVE_RIGHT_2);
        rightDrive3 = new VictorSP(RobotMap.DRIVE_RIGHT_3);
        left = new MotorGroup(leftDrive1, leftDrive2, leftDrive3);
        left.setInverted(true);
        right = new MotorGroup(rightDrive1, rightDrive2, rightDrive3);
        shift = new Solenoid(RobotMap.SHIFTER_SOLENOID);
    }

    public void tankDrive(double leftPower, double rightPower) {
        left.set(leftPower);
        right.set(rightPower);
    }

    public void arcadeDrive(double x, double y) {
        tankDrive(y + x, y - x);
    }

    public void stop() {
        left.set(0);
        right.set(0);
    }

    public void shift(boolean b) {
        shift.set(b);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}