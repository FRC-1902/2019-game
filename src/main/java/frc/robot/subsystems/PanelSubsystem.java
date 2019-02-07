package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class PanelSubsystem extends Subsystem {
    public WPI_TalonSRX hatchArm;
    public Solenoid flipper;
    public Solenoid outtake;
    Encoder hatchEncoder;
    PIDController hatchPID;

    public PanelSubsystem() {
        hatchArm = new WPI_TalonSRX(RobotMap.HATCH_ARM);
        hatchEncoder = new Encoder(RobotMap.HATCH_ENCODER_PORT_A, RobotMap.HATCH_ENCODER_PORT_B);
        hatchEncoder.setPIDMode(AbstractEncoder.PIDMode.POSITION);
        hatchPID = new PIDController(hatchArm, hatchEncoder, 0, 0, 0);
        flipper = new Solenoid(RobotMap.FLIPPER_SOLENOID);
        outtake = new Solenoid(RobotMap.OUTTAKE_SOLENOID);
    }

    public void setHatchPosition(HatchPosition pos) {
        if (pos.value == 0) {
            flipper.set(true);
        } else {
            flipper.set(false);
        }
        hatchPID.setTarget(pos.value);
    }

    public void outtake() {
        outtake.set(true);
    }


    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void setName(String subsystem, String name) {

    }

    public enum HatchPosition {
        UP(0), DOWN(0);

        int value;

        HatchPosition(int pos) {
            this.value = pos;
        }
    }
}
