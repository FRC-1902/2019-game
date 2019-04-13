package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class OutBallSubsystem extends Subsystem {
    public Victor outMotor;

    public OutBallSubsystem() {
        outMotor = new Victor(RobotMap.OUTTAKE);
    }

    public void set(double pow) {
        outMotor.set(pow);
    }

    @Override
    protected void initDefaultCommand() {

    }
}