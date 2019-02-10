package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class LiftSubsystem extends Subsystem {
    Encoder liftEncoder;
    WPI_VictorSPX lift1, lift2, lift3, lift4;
    PIDController liftPID;
    MotorGroup lift;
    Position currentLiftPosition;

    public LiftSubsystem() {
        lift1 = new WPI_VictorSPX(RobotMap.LIFT_1);
        lift2 = new WPI_VictorSPX(RobotMap.LIFT_2);
        lift3 = new WPI_VictorSPX(RobotMap.LIFT_3);
        lift4 = new WPI_VictorSPX(RobotMap.LIFT_4);
        lift = new MotorGroup(lift1, lift2, lift3, lift4);
        liftEncoder = new Encoder(RobotMap.LIFT_ENCODER_PORT_A, RobotMap.LIFT_ENCODER_PORT_B);
        liftEncoder.setPIDMode(AbstractEncoder.PIDMode.POSITION);
        liftPID = new PIDController(lift, liftEncoder, 0, 0, 0);
    }

    public void set(Position pos) {
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

    public enum Position {
        GROUND(0), SCORE_1(0), SCORE_2(0), SCORE_3(0);

        int value;

        Position(int pos) {
            this.value = pos;
        }
    }
}
