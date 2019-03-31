/*
 _______  ___   _______  __   __  _______  ___
|       ||   | |       ||  |_|  ||       ||   |
|    _  ||   | |    ___||       ||    ___||   |
|   |_| ||   | |   | __ |       ||   |___ |   |
|    ___||   | |   ||  | |     | |    ___||   |___
|   |    |   | |   |_| ||   _   ||   |___ |       |
|___|    |___| |_______||__| |__||_______||_______|


Written for the 2018 FIRST Robotics Competition game "POWER UP". All code here is either written by students from
team 1902 or is open source/publicly available to the FIRST community.

Written by:
Ryan S
Varun A
Natalie B
Ruth P
Jeffrey S
Sebastian V
 */

package frc.robot;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.auto254;
import frc.robot.positioning.RobotState;
import frc.robot.positioning.RobotStateGenerator;
import frc.robot.subsystems.DriveSubsystem;

public class Robot extends TimedRobot {

    public static boolean MAIN_ROBOT = true;

    public static DriveSubsystem drive;

    private OI oi;

    public static Compressor compressor;

    public static UsbCamera camera;

    public static SendableChooser<String> autoSelector;

    public Command testauto;

    public RobotStateGenerator stateGenerator;

    public static Robot instance;

    @Override
    public void robotInit() {
        super.robotInit();

        compressor = new Compressor();

        oi = new OI();
        drive = new DriveSubsystem();
        //intake = new IntakeSubsystem();
        //arm = new ArmSubsystem();

        SmartDashboard.putNumber("Autonomous Cubes", 3);

        SmartDashboard.putNumber("kP", 0.0014);
        SmartDashboard.putNumber("kI", 0);
        SmartDashboard.putNumber("kD", 0);

        Robot.drive.gyro.rezero();

        autoSelector = new SendableChooser<>();
        autoSelector.addDefault("LOW Gear 3 Cube Switch Auto (Middle)", "middle_low");
        autoSelector.addObject("Dump If Side (Left)", "left");
        autoSelector.addObject("Dump If Side (Right)", "right");
        SmartDashboard.putData("Auto Selector", autoSelector);

        //testauto = new auto254();
        stateGenerator = RobotStateGenerator.getInstance();

        if (MAIN_ROBOT) {
            Log.i("PIGXEL mode.");
        } else {
            Log.i("Coil Spring Container mode! (PRACTICE BOT)");
        }

        instance = this;
        stateGenerator.start();
    }

    @Override
    public void disabledInit() {
        //stateGenerator.actuallyStop();
    }

    @Override
    public void disabledPeriodic(){
        //Log.d("Left : " + drive.leftDriveEncoder.get() + "Right: " + drive.rightDriveEncoder.get() + "Gyro: " + drive.gyro.getHeading());
        Log.d("Robot X: " + RobotState.getInstance().getLatestFieldToVehicle().getValue().getTranslation().getX());
    }

    @Override
    public void autonomousInit() {
        drive.gyro.rezero();
        drive.leftDriveEncoder.reset();
        drive.rightDriveEncoder.reset();

        OI.runCommand(new auto254());
    }

    @Override
    public void autonomousPeriodic(){
        //Log.d("Left : " + drive.leftDriveEncoder.get() + "Right: " + drive.rightDriveEncoder.get() + "Gyro: " + drive.gyro.getHeading());
    }

    @Override
    public void teleopInit() {
        OI.runCommand(new DriveCommand());
        //OI.runCommand(new IntakeCommand());
        //OI.runCommand(new ArmCommand());

        //arm.armPID.reTune(SmartDashboard.getNumber("kP", 0), SmartDashboard.getNumber("kI", 0),
        //      SmartDashboard.getNumber("kD", 0));

        //arm.armPID.enable();

        //arm.armPID.disable();
    }

    @Override
    public void teleopPeriodic() {
        Log.d("Left : " + drive.leftDriveEncoder.get() + "Right: " + drive.rightDriveEncoder.get() + "Gyro: " + drive.gyro.getHeading());
    }

    @Override
    public void testInit() {
        /*arm.arm.getMotors().get(0).setPower(0.5);
        try {
            Thread.sleep(1000);
        } catch ( Exception e) {

        }
        arm.arm.getMotors().get(0).setPower(0);
        *///arm.arm.testEachWait(0.5, 0.2);
        drive.leftDrive.testEachWait(0.5, 0.5);
        drive.rightDrive.testEachWait(0.5, 0.5);
    }

    @Override
    public void testPeriodic() {
        drive.shift(OI.driver.y.get());
        //arm.arm.setPower(OI.manipulator.getY());
    }
}
