package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Potentiometer;
import frc.robot.RobotMap;

public class LiftSubsystem extends Subsystem {
    public WPI_VictorSPX lift1, lift2, lift3, lift4;
    PIDController liftPID;
    MotorGroup lift;
    LiftPosition currentLiftPosition;
    public Potentiometer pot;

    public LiftSubsystem() {
        lift1 = new WPI_VictorSPX(RobotMap.LIFT_1);
        lift2 = new WPI_VictorSPX(RobotMap.LIFT_2);
        lift3 = new WPI_VictorSPX(RobotMap.LIFT_3);
        lift4 = new WPI_VictorSPX(RobotMap.LIFT_4);
        lift = new MotorGroup(lift1, lift2, lift3, lift4);
        pot = new Potentiometer(RobotMap.LIFT_POTENTIOMETER);
        liftPID = new PIDController(lift, pot, 0, 0, 0);
    }

    public void setPower(double pow){
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
        GROUND(0), HATCH_1(0), CARGO_1(0), CARGO_SHIP(0), HATCH_2(0), CARGO_2(0), HATCH_3(0), CARGO_3(0);

        int value;

        LiftPosition(int pos) {
            this.value = pos;
        }
    }
}
