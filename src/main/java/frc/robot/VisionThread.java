package frc.robot;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.vision.*;
import com.sun.javafx.geom.Vec3d;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Method;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
        camera.setResolution(640, 480);
        camera.setExposureHoldCurrent();
        camera.setExposureManual(10);

        //d: 45.5
        //w: 43
        //arctan(0.4725)
        //horizontal angle = 51 deg

        CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Vision", 320, 240);

        Image source = new Image();
        Image output = null;

        //CameraSettings.setExposureAuto(1);
        //CameraSettings.setExposure(VISION_EXPOSURE);

        Log.v("Vision Processing online.");

        while (true) {
            try{
                //Log.v("Vision loop alive and healthy");
                long timeOfGet = System.currentTimeMillis();
                cvSink.grabFrame(source.getMat());

                if (!source.getMat().empty()) {

                    //Log.v("Millis diff: " + (System.currentTimeMillis() - millis));

                    if (output != null) {
                        output.release();
                        output = null;
                    }
                    output = source.copy();
                    source.toHSV();

                    source.inRange(new HSV(hLow, sLow, vLow), new HSV(hHigh, sHigh, vHigh));


                    List<Contour> contours = getContours(source);//source.getContours();
                    List<Contour> goodContours = new ArrayList<>();

                    output.drawContours(contours, Color.RED);

                    RotatedRect temp;
                    for (Contour c : contours) {
                        if (c.getArea() > 250) {
                            boolean tall;
                            temp = c.rotatedRect;
                            double ratio = temp.size.height / temp.size.width;
                            if(ratio < 1) {
                                ratio = 1 / ratio;
                                tall = false;
                            } else tall = true;
                            if(ratio > 2.5){
                                if(tall && Math.abs(Math.abs(temp.angle) - 14.5) < 22.5){
                                    goodContours.add(c);
                                } else if(!tall && Math.abs(Math.abs(temp.angle) - 75.5) < 22.5){
                                    goodContours.add(c);
                                }
                            }
                        }
                    }

                    goodContours.sort(new Comparator<Contour>() {
                        @Override
                            public int compare(Contour o1, Contour o2) {
                                return (int)(o2.getArea() - o1.getArea());
                            }
                        });
                    List<Contour> finalContours = new ArrayList<>();

                    if(goodContours.size() > 2){
                        RotatedRect rr1 = goodContours.get(0).rotatedRect;
                        double targetAngle = rr1.size.height > rr1.size.width ? 75.5 : 14.5;
                        RotatedRect current;
                        int out = 0;
                        for(int i = 1; i < goodContours.size(); i++){
                            current = goodContours.get(i).rotatedRect;
                            if(Math.abs(Math.abs(current.angle) - targetAngle) < 22.5){
                                out = i;
                                break;
                            }
                        }
                        finalContours.add(goodContours.get(0));
                        finalContours.add(goodContours.get(out));
                    } else if(goodContours.size() == 2){
                        finalContours = goodContours;
                    } else{
                        System.out.println("Less than 2 targets seen");
                    }
                    //Imgproc.minAreaRect()

                    //Assume we have only two contours left, the correct targets

                    if (finalContours.size() == 2) {
                        Contour c1 = finalContours.get(0);
                        Contour c2 = finalContours.get(1);

                        if (c1.coords.x > c2.coords.x) {
                            c1 = goodContours.get(1);
                            c2 = goodContours.get(0);
                        }

                        Rectangle r1 = c1.getBoundingBox();
                        Rectangle r2 = c2.getBoundingBox();
                        RotatedRectPoints rr1 = new RotatedRectPoints(c1.rotatedRect);
                        RotatedRectPoints rr2 = new RotatedRectPoints(c2.rotatedRect);


                        //System.out.println("Small Boi Dist: " + ((r2.x - r2.width / 2) - (r1.x + r1.width / 2)));
                        //System.out.println("Big Boi Dist: " + ((r2.x + r2.width / 2) - (r1.x - r1.width / 2)));

                        //System.out.println("Angle 1: " + c1.rotatedRect.angle);
                        //System.out.println("Angle 2: " + c2.rotatedRect.angle);

                        output.drawRectangle(r1, Color.BLUE);
                        output.drawRectangle(r2, Color.BLUE);
                        drawRotatedRect(output,c1.rotatedRect,Color.YELLOW);
                        drawRotatedRect(output,c2.rotatedRect,Color.YELLOW);
                        output.drawCircle((int)rr1.getCorner(0).x, (int)rr1.getCorner(0).y, 5,Color.ORANGE);
                        output.drawCircle((int)rr2.getCorner(0).x, (int)rr2.getCorner(0).y, 5,Color.ORANGE);

                        /*int minH = 255, maxH = 0, current;
                        double[] data;
                        //Scalar dataRGB, dataHSV = new Scalar(0);
                        for(int i = 0; i < c1.getMat().cols(); i++){
                            for(int j = 0; j < c1.getMat().rows(); j++){
                                data = c1.getMat().get(j,i); //new Scalar((int)c1.getMat().get(j,i)[0],(int)c1.getMat().get(j,i)[1],(int)c1.getMat().get(j,i)[2]);
                                current = (int)data[1];
                                if(current < minH) minH = current;
                                else if(current > maxH) maxH = current;
                            }
                        }
                        System.out.println("MinH: " + minH);
                        System.out.println("MaxH: " + maxH);*/
                        double xDiff = Math.abs(rr1.getCorner(0).x - rr2.getCorner(0).x);
                        double theta = (xDiff/640) * 0.8901179;
                        double twoTan = Math.tan(theta/2);
                        double distance = 8/(2*twoTan); /*4/(Math.tan((xDiff * 51)/1280));*/
                        System.out.println("distance: " + distance + " 2Tan: " + twoTan);
                    } else {
                        //Log.v("More than two");
                    }


                    outputStream.putFrame(output.getMat());
                    //output.release();

                    for (Contour co : contours) {
                        //co.release();
                    }
                }
                //source.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private Mat weirdMat = new Mat();

    public void drawRotatedRect(Image img, RotatedRect rect, Color color) {
        Point points[] = new Point[4];
        rect.points(points);
        for(int i=0; i<4; ++i){
            Imgproc.line(img.getMat(), points[i], points[(i+1)%4], new Scalar(color.getRed(), color.getGreen(), color.getBlue()));
        }
    }

    public List<Contour> getContours(Image i) {
        List<Contour> result = new ArrayList();
        List<MatOfPoint> contours = new ArrayList();
        Image copy = i.copy();
        Imgproc.findContours(copy.getMat(), contours, weirdMat, 0, 2);
        Iterator var4 = contours.iterator();

        while(var4.hasNext()) {
            MatOfPoint mop = (MatOfPoint)var4.next();
            result.add(new Contour(mop));
        }

        copy.release();
        return result;
    }
}
