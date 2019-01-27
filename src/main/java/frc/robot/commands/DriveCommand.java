package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;

public class DriveCommand extends Command {
    Robot robot;
    DriveSubsystem driveSubsystem;

    public DriveCommand(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void onInit() {
        driveSubsystem = Robot.driveSubsystem;
    }

    @Override
    public void onLoop() {
        double x = OI.driveController.getX2();
        double y = OI.driveController.getY();

        y = -y;

        x = Math.pow(Utils.deadzone(x, 0.1), 2) * Utils.sign(x);
        y = Math.pow(Utils.deadzone(y, 0.1), 2) * Utils.sign(y);

        driveSubsystem.shift(OI.driveController.rightTrigger.get());
        driveSubsystem.arcadeDrive(x, y);
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !robot.isEnabled();
    }
}
