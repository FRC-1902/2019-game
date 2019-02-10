package frc.robot;

public class RobotMap {

    public static final int LIFT_1 = Robot.OutBall ? 6 : -1;
    public static final int LIFT_2 = Robot.OutBall ? 7 : -1;
    public static final int LIFT_3 = Robot.OutBall ? 8 : -1;
    public static final int LIFT_4 = Robot.OutBall ? 9 : -1;
    public static final int LIFT_ENCODER_PORT_A = Robot.OutBall ? 0 : -1;
    public static final int LIFT_ENCODER_PORT_B = Robot.OutBall ? 1 : -1;

    public static final int HATCH_ENCODER_PORT_A = Robot.OutBall ? 0 : -1;
    public static final int HATCH_ENCODER_PORT_B = Robot.OutBall ? 1 : -1;

    // CAN IDs

    public static final int DRIVE_LEFT_1 = Robot.OutBall ? 1 : 1;
    public static final int DRIVE_LEFT_2 = Robot.OutBall ? 2 : 3;
    public static final int DRIVE_LEFT_3 = Robot.OutBall ? 5 : 5;
    public static final int DRIVE_RIGHT_1 = Robot.OutBall ? 0 : 6;
    public static final int DRIVE_RIGHT_2 = Robot.OutBall ? 3 : 2;
    public static final int DRIVE_RIGHT_3 = Robot.OutBall ? 4 : 4;

    public static final int HATCH_ARM = Robot.OutBall ? 7 : -1;

    public static final int SHIFTER_SOLENOID = Robot.OutBall ? 0 : 0;
    public static final int OUTTAKE_SOLENOID = Robot.OutBall ? 2 : 2;
}