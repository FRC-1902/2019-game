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
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.CanEncoder;
import frc.robot.RevColorDistance;
import frc.robot.Robot;
import frc.robot.RobotMap;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import static frc.robot.Robot.panelSubsystem;

public class IntakeSubsystem extends Subsystem {
    public CanEncoder intakeEncoder;
    public TalonSRX intakeArm;
    public PIDController intakePID;
    public Talon intake;
    public Victor conveyor;

    public RevColorDistance distance;

    public IntakeSubsystem() {
        intakeArm = new TalonSRX(RobotMap.INTAKE_ARM);
        intake = new Talon(RobotMap.INTAKE);
        conveyor = new Victor(RobotMap.CONVEYOR);
        intakeArm.setNeutralMode(NeutralMode.Brake);
        intakeArm.setSensorPhase(true);
        intakeEncoder = new CanEncoder(RobotMap.HATCH_ARM);
        distance = new RevColorDistance();
        intakeEncoder.reset();

        intakePID = new PIDController(null, intakeEncoder, 0.0005, 0, 0);
    }

    public void setArmPower(double pow) {
        intakeArm.set(ControlMode.PercentOutput, -pow);
    }

    public void setIntakePower(double pow) {
        intake.set(pow);
    }

    public void setConveyorPower(double pow){
        conveyor.set(pow);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}
