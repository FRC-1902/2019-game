package frc.robot;

import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.vision.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.vision.RotatedRectPoints;
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
    private double distance, offset, target, targetCenter;
    private final int fieldWidth = 854;
    private boolean targetIsValid;

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
        camera.setResolution(854, 480);
        camera.setExposureHoldCurrent();
        camera.setExposureManual(10);

        //d: 45.5
        //w: 43
        //arctan(0.4725)
        //horizontal angle = 51 deg

        CvSink cvSink = CameraServer.getInstance().getVideo();
        //CvSource outputStream = CameraServer.getInstance().putVideo("Vision", 854, 480);

        Image source = new Image();
        Image output = null;

        Log.v("Vision Processing online.");

        while (true) {
            targetIsValid = false;
            try{
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
                            if(ratio > 1.5){
                                if(tall && Math.abs(Math.abs(temp.angle) - 14.5) < 22.5){
                                    goodContours.add(c);
                                } else if(!tall && Math.abs(Math.abs(temp.angle) - 75.5) < 22.5){
                                    goodContours.add(c);
                                }
                            }
                        }
                    }

                    goodContours.sort((o1, o2) -> (int)(o2.getArea() - o1.getArea()));
                    List<Contour> finalContours = new ArrayList<>();

                    if(goodContours.size() > 2){
                        // Filter for a pair of targets with angles of rotation roughly matching the goal
                        RotatedRect rr1 = goodContours.get(0).rotatedRect;
                        double targetAngle = rr1.size.height > rr1.size.width ? 75.5 : 14.5;
                        RotatedRect current;
                        int out = 0;
                        for(int i = 1; i < goodContours.size(); i++){
                            current = goodContours.get(i).rotatedRect;
                            if(Math.abs(Math.abs(current.angle) - targetAngle) < 22.5){
                                if(rr1.size.height > rr1.size.width && current.center.x < rr1.center.x){
                                    out = i;
                                    //System.out.println("Found tall");
                                    break;
                                } else if(rr1.size.height < rr1.size.width && current.center.x > rr1.center.x){
                                    out = i;
                                    //System.out.println("Found short");
                                    break;
                                }
                            }
                        }
                        finalContours.add(goodContours.get(0));
                        finalContours.add(goodContours.get(out));
                    } else if(goodContours.size() == 2){
                        // If there is only two targets, assume they're the correct two targets. jk dont
                        RotatedRect rr1 = goodContours.get(0).rotatedRect;
                        RotatedRect current = goodContours.get(1).rotatedRect;

                        if(rr1.size.height > rr1.size.width && current.center.x < rr1.center.x){
                            finalContours = goodContours;
                        } else if(rr1.size.height < rr1.size.width && current.center.x > rr1.center.x){
                            finalContours = goodContours;
                        }
                    } else {
                        //Log.e("Less than 2 potential vision targets seen");
                    }

                    //We have only two contours left, the correct targets
                    if (finalContours.size() == 2) {
                        targetIsValid = true;
                        Contour c1 = finalContours.get(0);
                        Contour c2 = finalContours.get(1);

                        output.drawRectangle(c1.getBoundingBox(), Color.ORANGE);
                        output.drawRectangle(c2.getBoundingBox(), Color.BLUE);

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
                        double aLength = (rr1.isTall) ? rr1.inst.size.width : rr1.inst.size.height;
                        double bLength = (rr2.isTall) ? rr2.inst.size.width : rr2.inst.size.height;
                        double avgLength = (aLength + bLength) / 2;
                        double ratio = aLength/bLength;
                        if(ratio > 1) ratio = 1/ratio;
                        ratio = clamp(ratio,0.75,1);
                        //double targetOffset;

                        if(rr1.inst.center.x < rr2.inst.center.x){
                            if(aLength < bLength){
                                target = (rr1.inst.center.x + rr2.inst.center.x)/2 - (((1/ratio) - 1) * (320*2));
                            } else{
                                target = (rr1.inst.center.x + rr2.inst.center.x)/2 + (((1/ratio) - 1) * (320*2));
                            }
                        } else{
                            if(aLength < bLength){
                                target = (rr1.inst.center.x + rr2.inst.center.x)/2 + (((1/ratio) - 1) * (320*2));
                            } else{
                                target = (rr1.inst.center.x + rr2.inst.center.x)/2 - (((1/ratio) - 1) * (320*2));
                            }
                        }
                        /*double theta = (xDiff/640) * 0.8901179; //0.8901179
                        double twoTan = Math.tan(theta/2);
                        double distance = 8/(2*twoTan); /*4/(Math.tan((xDiff * 51)/1280));*/

                        double theta = (avgLength/fieldWidth) * Math.toRadians(59.7); //0.8901179
                        double twoTan = Math.tan(theta/2);
                        distance = 2/(2*twoTan);
                        offset = target - 427;//((rr1.inst.center.x + rr2.inst.center.x)/2) - target;
                        output.drawLine(427, Color.GREEN);
                        targetCenter = (rr1.getCorner(0).x + rr2.getCorner(0).x)/2;
                        output.drawLine((int)targetCenter, Color.ORANGE);
                        output.drawLine((int)target, Color.RED);
                        //System.out.println("distance: " + distance + " 2Tan: " + twoTan);
                        //System.out.println("rr1: " + aLength + "rr2: " + bLength + "ratio: " + ratio);
                    } else {
                        //Log.v("More than two");
                    }


                    //outputStream.putFrame(output.getMat());
                    //output.release();

                    for (Contour co : contours) {
                        co.release();
                    }
                }
                //source.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private Mat weirdMat = new Mat();

    public double getDistance(){
        return distance;
    }

    public double getOffset(){
        return offset;
    }

    public boolean getTargetIsValid(){return targetIsValid;}

    public double getTargetCenter(){return targetCenter;}

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

    public double clamp(double val, double min, double max){
        if(val < min) return min;
        else if(val > max) return max;
        else return val;
    }
}
