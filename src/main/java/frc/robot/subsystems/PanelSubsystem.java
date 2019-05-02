package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.explodingbacon.bcnlib.framework.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.CanEncoder;
import frc.robot.Potentiometer;
import frc.robot.RobotMap;

public class PanelSubsystem extends Subsystem {
    public CanEncoder hatchEncoder;
    public Potentiometer hatchPot;
    public TalonSRX hatchArm;
    public PIDController hatchPID;
    public Solenoid outtake, clamp;

    public PanelSubsystem() {
        hatchArm = new TalonSRX(RobotMap.HATCH_ARM);
        hatchArm.setNeutralMode(NeutralMode.Brake);
        hatchArm.setSensorPhase(true);
        hatchArm.setInverted(true);
        //hatchEncoder = new CanEncoder(RobotMap.HATCH_ARM);
        hatchPot = null;//new Potentiometer(RobotMap.HATCH_POTENTIOMETER);
        //hatchPID = new PIDController(null, hatchEncoder, 0, 0, 0);
        hatchPID = new PIDController(null, hatchPot, 3, 0, 0); //0.001 3
        //hatchEncoder.reset();
        outtake = new Solenoid(RobotMap.OUTTAKE_SOLENOID);
        clamp = new Solenoid(RobotMap.CLAMP_SOLENOID);
    }

    public void setHatchArm(double pow) {
        hatchArm.set(ControlMode.PercentOutput, -pow);
    }

    public void setOuttake(boolean out) {
        outtake.set(out);
    }

    public void setClamp(boolean set) {
        clamp.set(set);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}
