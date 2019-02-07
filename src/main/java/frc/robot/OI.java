package frc.robot;

import com.explodingbacon.bcnlib.controllers.XboxController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {
    public static XboxController driveController = new XboxController(0);
    public static XboxController manipController = new XboxController(1);

}
