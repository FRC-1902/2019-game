package frc.robot;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.vision.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import java.util.ArrayList;
import java.util.List;

public class VisionThread implements Runnable {

    public final int hLow = 60, sLow = 150, vLow = 50;
    public final int hHigh = 110, sHigh = 255, vHigh = 255;

    Thread thread;


    public VisionThread() {
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setFPS(5); //60
        camera.setResolution(320, 240);

        CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Vision", 320, 240);

        Image source = new Image();
        Image output;

        //CameraSettings.setExposureAuto(1);
        //CameraSettings.setExposure(VISION_EXPOSURE);

        Log.v("Vision Processing online.");
        while (true) {
            try{
                Log.v("Vision loop alive and healthy");
                long timeOfGet = System.currentTimeMillis();
                cvSink.grabFrame(source.getMat());

                if (!source.getMat().empty()) {

                    //Log.v("Millis diff: " + (System.currentTimeMillis() - millis));

                    output = source.copy();
                    source.toHSV();

                    source.inRange(new HSV(hLow, sLow, vLow), new HSV(hHigh, sHigh, vHigh));


                    List<Contour> contours = source.getContours();
                    List<Contour> goodContours = new ArrayList<>();

                    output.drawContours(contours, Color.RED);

                    for (Contour c : contours) {
                        if (c.getArea() > 50) {
                            goodContours.add(c);
                        }
                    }

                    //Imgproc.minAreaRect()

                    //Assume we have only two contours left, the correct targets

                    if (goodContours.size() == 2) {
                        Contour c1 = goodContours.get(0);
                        Contour c2 = goodContours.get(1);

                        if (c1.coords.x > c2.coords.x) {
                            c1 = goodContours.get(1);
                            c2 = goodContours.get(0);
                        }

                        Rectangle r1 = c1.getBoundingBox();
                        Rectangle r2 = c2.getBoundingBox();

                        System.out.println("Small Boi Dist: " + ((r2.x - r2.width / 2) - (r1.x + r1.width / 2)));
                        System.out.println("Big Boi Dist: " + ((r2.x + r2.width / 2) - (r1.x - r1.width / 2)));

                        output.drawRectangle(r1, Color.BLUE);
                        output.drawRectangle(r2, Color.BLUE);
                    } else {
                        Log.v("More than two");
                    }


                    outputStream.putFrame(output.getMat());
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
