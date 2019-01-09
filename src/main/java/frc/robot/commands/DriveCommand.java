package frc.robot.commands;

import com.explodingbacon.bcnlib.framework.Command;
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
        driveSubsystem.arcadeDrive(OI.driveController.getX2(), OI.driveController.getY());
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
