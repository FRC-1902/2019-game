package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.CanEncoder;
import frc.robot.RobotMap;

import static frc.robot.Robot.panelSubsystem;

public class PanelSubsystem extends Subsystem {
    public CanEncoder hatchEncoder;
    public TalonSRX hatchArm;
    public PIDController hatchPID;
    public Solenoid outtake;

    public PanelSubsystem() {
        hatchArm = new TalonSRX(RobotMap.HATCH_ARM);
        hatchArm.setNeutralMode(NeutralMode.Brake);
        hatchArm.setSensorPhase(true);
        hatchEncoder = new CanEncoder(RobotMap.INTAKE_ARM);
        hatchEncoder.reset();

        hatchPID = new PIDController(null, hatchEncoder, 0.001, 0, 0); //0.0003 to 0.000165
        outtake = new Solenoid(RobotMap.OUTTAKE_SOLENOID);
    }

    public void setHatchArm(double pow) {
        hatchArm.set(ControlMode.PercentOutput, -pow);
    }

    public void setOuttake(boolean out) {
        outtake.set(out);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}
