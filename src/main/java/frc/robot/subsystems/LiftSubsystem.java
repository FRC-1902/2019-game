package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Potentiometer;
import frc.robot.RobotMap;

public class LiftSubsystem extends Subsystem {
    public Victor lift1, lift2, lift3, lift4;
    public PIDController liftPID;
    public MotorGroup lift;
    LiftPosition currentLiftPosition;
    public Potentiometer pot;
    double kP, kI, kD;

    public LiftSubsystem() {
        lift1 = new Victor(RobotMap.LIFT_1);
        lift2 = new Victor(RobotMap.LIFT_2);
        //lift3 = new WPI_VictorSPX(RobotMap.LIFT_3);
        //lift4 = new WPI_VictorSPX(RobotMap.LIFT_4);
        lift = new MotorGroup(lift1, lift2);
        lift.setInverted(true);
        pot = new Potentiometer(RobotMap.LIFT_POTENTIOMETER);

        liftPID = new PIDController(null, pot, 4, 0.07, 4, 0.1, 1);
        liftPID.setGravityMode(true, 1);
        //liftPID.disable();
    }

    public void setPower(double pow) {
        lift.set(pow);
    }

    public void setPosition(LiftPosition pos) {
        liftPID.setTarget(pos.value);
        currentLiftPosition = pos;
    }

    public void nudge(int clicks) {
        liftPID.setTarget(currentLiftPosition.value + clicks);
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }

    public enum LiftPosition {
        GROUND(0.160), CARGO_SHIP(0.322), ROCKET_2(0.498), ROCKET_3(0.775); //0.013 per inch

        double value;

        LiftPosition(double pos) {
            this.value = pos;
        }
    }
}
