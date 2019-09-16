package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class PanelSubsystem2 extends Subsystem {
    public Victor wheelMotor;

    public PanelSubsystem2() {wheelMotor = new Victor(RobotMap.PanelWheel);}
    public void set(double pow){wheelMotor.set(pow);}
    @Override
    protected void initDefaultCommand() {

    }
}
