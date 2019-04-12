package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RevColorDistance;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class IntakeSubsystem extends Subsystem {
    //public CanEncoder intakeEncoder;
    //public TalonSRX intakeArm;
    //public PIDController intakePID;
    public Talon intake;
    public Victor conveyor;
    public Solenoid intakeArm;
    public RevColorDistance distance;

    public IntakeSubsystem() {
        //intakeArm = new TalonSRX(RobotMap.INTAKE_ARM);
        intakeArm = new Solenoid(RobotMap.INTAKE_SOLENOID);
        intake = new Talon(RobotMap.INTAKE);
        if(Robot.OutBall) intake.setInverted(true);
        conveyor = new Victor(RobotMap.CONVEYOR);
        //intakeArm.setNeutralMode(NeutralMode.Brake);
        //intakeArm.setSensorPhase(true);
        //intakeEncoder = new CanEncoder(intakeArm); //switched on practice robot
        distance = new RevColorDistance();
        //intakeEncoder.reset();

        //intakePID = new PIDController(null, intakeEncoder, 0.001, 0.00002, 0); //0.0005 i:00001
        //intakePID.setGravityMode(true, -1);
    }

    /*public void setArmPower(double pow) {
        intakeArm.set(ControlMode.PercentOutput, -pow);
    }*/

    public void setIntakePower(double pow) {
        intake.set(pow);
    }

    public void setConveyorPower(double pow) {
        conveyor.set(pow);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }
}
