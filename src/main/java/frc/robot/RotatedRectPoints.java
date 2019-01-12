package frc.robot;

import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

public class RotatedRectPoints {
    RotatedRect inst;
    boolean isTall;
    Point[] points = new Point[4], corners = new Point[4];

    //0 is centermost point, then highest point, etc
    public RotatedRectPoints(RotatedRect rect){
        this.inst = rect;
        isTall = (inst.size.height > inst.size.width);
        inst.points(points);
        if(isTall){
            corners[0] = points[1];
            corners[1] = points[2];
            corners[2] = points[3];
            corners[3] = points[0];
        } else{
            corners[0] = points[3];
            corners[1] = points[2];
            corners[2] = points[1];
            corners[3] = points[0];
        }
    }

    public Point getCorner(int corner){
        return corners[corner];
    }
}
