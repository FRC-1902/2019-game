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
import frc.robot.Robot;
import frc.robot.RobotMap;

public class LiftSubsystem extends Subsystem {
    public Victor lift1, lift2, lift3, lift4;
    public PIDController liftPID, downPID;
    public MotorGroup lift;
    LiftPosition currentLiftPosition;
    public Potentiometer pot;
    double kP, kI, kD;

    public LiftSubsystem() {
        lift1 = new Victor(RobotMap.LIFT_1);
        lift2 = new Victor(RobotMap.LIFT_2);
        if( Robot.OutBall){
            lift3 = new Victor(RobotMap.LIFT_3);
            lift4 = new Victor(RobotMap.LIFT_4);
            lift = new MotorGroup(lift1, lift2, lift3, lift4);
            lift.setInverted(true);
        } else {
            lift = new MotorGroup(lift1, lift2);
            lift.setInverted(true);
        }
        pot = new Potentiometer(RobotMap.LIFT_POTENTIOMETER);

        if(Robot.OutBall){
            liftPID = new PIDController(null, pot, 6, 0.1, 0, 0.1, 1);//1);
        } else{
            liftPID = new PIDController(null, pot, 4, 0.07, 4, 0.1, 1);//1);
        }
        liftPID.setGravityMode(true, 1);

        downPID = new PIDController(null, pot, 0, 0, 15, 0.35, 0.5);
        if(Robot.OutBall) downPID.enable();
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


    private static final double GROUND_CONSTANT = Robot.OutBall ? 0.148: 0.160;
    public enum LiftPosition {
        GROUND(0), CARGO_SHIP(Robot.OutBall ? (0.322 - 0.160) * 1.6 : (0.322 - 0.160)), ROCKET_2(0.498 - 0.160), ROCKET_3(Robot.OutBall ? 0.775 - 0.175 : 0.775 - 0.160); //0.013 per inch

        public double value;

        LiftPosition(double pos) {
            this.value = pos;
            value += GROUND_CONSTANT;
        }
    }
}
