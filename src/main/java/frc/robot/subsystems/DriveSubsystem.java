package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.FakeMotor;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.sensors.AbstractEncoder;
import com.explodingbacon.bcnlib.sensors.BNOGyro;
import com.explodingbacon.bcnlib.sensors.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Map;
import frc.robot.Robot;

import java.util.List;

public class DriveSubsystem extends Subsystem {

    public MotorGroup leftDrive, rightDrive;
    public PIDController positionPID;
    public PIDController rotatePID, rotateInPlacePID, rotateDrivingPID;
    public Encoder rightDriveEncoder;
    public Encoder leftDriveEncoder;
    public AbstractEncoder driveEncoderAvg;
    public BNOGyro gyro;

    private Solenoid shift, light;

    public FakeMotor positionPIDOutput, rotatePIDOutput, rotateDrivingPIDOutput, rotateInPlacePIDOutput;

    public DriveSubsystem() {

        gyro = new BNOGyro(true);

        shift = new Solenoid(Map.SHIFT);
        light = new Solenoid(1);

        positionPIDOutput = new FakeMotor();
        rotatePIDOutput = new FakeMotor();
        rotateDrivingPIDOutput = new FakeMotor();
        rotateInPlacePIDOutput = new FakeMotor();

        rotatePID = new PIDController(rotatePIDOutput, gyro, .0085,0.001,0);
        rotatePID.setRotational(true);


        rotateInPlacePID = new PIDController(rotateInPlacePIDOutput, gyro, .012,0.001,0); //.0105 > .0115
        rotateInPlacePID.setRotational(true);

        rotateDrivingPID = new PIDController(rotateDrivingPIDOutput, gyro, .0085,0,0);
        rotateDrivingPID.setRotational(true);

        rightDrive = new MotorGroup(new WPI_VictorSPX(Map.RIGHT_DRIVE_A), new WPI_VictorSPX(Map.RIGHT_DRIVE_B),
                new WPI_VictorSPX(Map.RIGHT_DRIVE_C));

        rightDrive.setInverted(true);


        leftDrive = new MotorGroup(new WPI_VictorSPX(Map.LEFT_DRIVE_A), new WPI_VictorSPX(Map.LEFT_DRIVE_B),
                new WPI_VictorSPX(Map.LEFT_DRIVE_C));


        rightDriveEncoder = new Encoder(Map.DRIVE_RIGHT_ENCODER_A, Map.DRIVE_RIGHT_ENCODER_B);
        leftDriveEncoder = new Encoder(Map.DRIVE_LEFT_ENCODER_A, Map.DRIVE_LEFT_ENCODER_B);

        rightDriveEncoder.setReversed(true);
        leftDriveEncoder.setReversed(false);

        driveEncoderAvg = new AbstractEncoder() {
            @Override
            public double getRate() {
                return (leftDriveEncoder.getRate() + rightDriveEncoder.getRate())/2;
            }

            @Override
            public int get() {
                return (leftDriveEncoder.get() + rightDriveEncoder.get())/2;
            }

            @Override
            public void reset() {
                leftDriveEncoder.reset();
                rightDriveEncoder.reset();
            }
        };

        positionPID = new PIDController(positionPIDOutput,driveEncoderAvg, 0.002, 0, 0);


        positionPID.setTarget(leftDriveEncoder.get() + inchesToClicks(100));
    }

    @Override
    protected void initDefaultCommand() {

    }

    public double getRate() {
        return (Math.abs(rightDriveEncoder.getRate() + leftDriveEncoder.getRate()))/2;
    }

    public double getRateNotAbs() {
        return (rightDriveEncoder.getRate() + leftDriveEncoder.getRate())/2;
    }

    public void tankDrive(double left, double right) {
        leftDrive.set(left);
        rightDrive.set(right);
    }

    public void shift(boolean b) {
        b = !b;
        shift.set(b);
    }

    public boolean getShift() {
        boolean b = shift.get();
        b = !b;
        return b;
    }

    public static double inchesTORotations(){
        return -1;
    }

    public double getRightVelocityInchesPerSec(){
        return -1;
    }

    /**
     * Converts inches to strafe encoder clicks.
     *
     * @param inches The inches to be converted.
     * @return The encoder clicks equivalent to the inches provided.
     */
    public static double inchesToClicks(double inches) {
        return inchesToRotations(inches) * 360;
    }

    public static double inchesPerSecondToRpm(double inches_per_second) {
        return inchesToRotations(inches_per_second) * 60;
    }

    public static double inchesToRotations(double inches) {
        return inches / (Math.PI * 6);
    }

    public static double clicksToInches(double clicks) {
        return rotationsToInches(clicks / 360);
    }

    private static double rpmToInchesPerSecond(double rpm) {
        return rotationsToInches(rpm) / 60;
    }

    public static double rotationsToInches(double rotations) {
        return rotations * (Math.PI * 6);
    }

    public void setPIDEnabled(boolean state){
        if(state){
            rotatePID.enable();
        } else{
            rotatePID.disable();
        }
    }
}
