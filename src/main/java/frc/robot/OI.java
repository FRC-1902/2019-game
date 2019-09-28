package frc.robot;

import com.explodingbacon.bcnlib.controllers.Button;
import com.explodingbacon.bcnlib.controllers.XboxController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {
    public static XboxController driveController = new XboxController(0);
    public static XboxController manipController = new XboxController(1);


    public static Button driveVision = driveController.leftBumper;
    public static Button driverHeck = driveController.y;


    public static final boolean ARCADE_DRIVE = true; // Arcade Drive is true
}
