/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.explodingbacon.bcnlib.actuators.MotorGroup;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.vision.Vision;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoProperty;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.DriveSubsystem;
import org.opencv.core.Core;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    public static DriveSubsystem driveSubsystem;
    public DriveCommand driveCommand;
    public VisionThread vision;
    UsbCamera camera;
    MjpegServer server;

    WPI_VictorSPX leftDrive1;
    WPI_VictorSPX leftDrive2;
    WPI_VictorSPX leftDrive3;

    MotorGroup test;


    @Override
    public void robotInit() {
        /*camera = new UsbCamera("usb camera", 0);
        camera.setResolution(640,480);
        camera.setFPS(15);
        //CameraServer.getInstance().addCamera(camera);
        server = CameraServer.getInstance().addServer("serve_USB Camera 0",1182);

        server.setSource(camera);
        camera.setFPS(15);

        for(VideoProperty v : camera.enumerateProperties()){
            System.out.println("Property: " + v.getName());
        }
        System.out.println("Default compression: " + server.getProperty("default_compression").get());
        System.out.println("compression min: " + server.getProperty("default_compression").getMin());
        System.out.println("compression max: " + server.getProperty("default_compression").getMax());
        System.out.println("Server fps: " + server.getProperty("fps").get());
        server.getProperty("compression").set(50);
        */


        leftDrive1 = new WPI_VictorSPX(0);
        leftDrive2 = new WPI_VictorSPX(2);
        leftDrive3 = new WPI_VictorSPX(3);

        test = new MotorGroup(leftDrive1, leftDrive2, leftDrive3);


        //driveSubsystem = new DriveSubsystem();
        //driveCommand = new DriveCommand(this);

        //Vision.init();
        //vision = new VisionThread();

        //driveCommand.start();
        //vision.start();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        //System.out.println("Server fps: " + server.getProperty("fps").get());
        //System.out.println("Compression: " + server.getProperty("compression").get());

        //System.out.println("FPS: " + usbCamera.getActualFPS());
        //System.out.println("Data Rate: " + usbCamera.getActualDataRate());
    }


    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopPeriodic() {
        if (OI.driveController.x.get()) {
            //test.set(OI.driveController.getY());
        }
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void teleopInit() {
        //driveSubsystem.light.set(true);
        test.testEachWait(0.5, 0.75);
    }
}
