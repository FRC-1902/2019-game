package frc.robot;

import com.explodingbacon.bcnlib.controllers.Button;
import com.explodingbacon.bcnlib.controllers.LogitechController;
import com.explodingbacon.bcnlib.controllers.XboxController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {

    public static XboxController driver;
    public static LogitechController manipulator;
    public static Button positionHoldButton;
    public static Button climberOutButton;
    public static Button climberUpButton;
    public static Button climberWinchButton;
    public static Button armPositionOne;
    public static Button armPositionTwo;
    public static Button armPositionThree;
    public static Button armPositionFour;
    public static Button intakeInButton;
    public static Button intakeOutButton;
    public static Button intakePassiveButton;
    public static Button climberPistonButton;

    public OI() {
        driver = new XboxController(0);
        manipulator = new LogitechController(1);

        positionHoldButton = driver.y;

        climberOutButton = manipulator.select;
        climberUpButton = manipulator.start;
        climberPistonButton = driver.a;
        climberWinchButton = driver.y;

        armPositionOne = manipulator.four;
        armPositionTwo = manipulator.one;
        armPositionThree = manipulator.two;
        armPositionFour = manipulator.three;

        intakeInButton = manipulator.rightBumper;
        intakeOutButton = manipulator.leftBumper;
        intakePassiveButton = manipulator.one;
    }
}
