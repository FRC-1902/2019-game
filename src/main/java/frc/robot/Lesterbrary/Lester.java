package frc.robot.Lesterbrary;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lester {

    public static double getDistance(Point a, Point b){
        return Math.sqrt(Math.pow(b.getX()-a.getX(),2) + Math.pow(b.getY() - a.getY(),2));
    }

    public static LineSegment getLeftOuterTangent(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double angle = center.getTheta();
            double radius = a.getRadius();
            Point p = new Point(a.getCenter().getX() - radius * Math.cos(angle), a.getCenter().getY() + radius * Math.sin(angle));
            LineSegment out = new LineSegment(p, center.getI(), center.getJ());
            return out;
        } else{
            return null;
        }
    }

    public static LineSegment getRightOuterTangent(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double angle = center.getTheta();
            double radius = a.getRadius();
            Point p = new Point(a.getCenter().getX() + radius * Math.cos(angle), a.getCenter().getY() - radius * Math.sin(angle));
            LineSegment out = new LineSegment(p, center.getI(), center.getJ());
            return out;
        } else{
            return null;
        }
    }

    public static LineSegment getLeftInnerTangent(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double radius = a.getRadius();
            double distance = center.getMagnitude();
            if(distance > 2 * radius){
                double thetaCenter = center.getTheta();
                double angle = thetaCenter + Math.acos((2*radius)/distance);
                double dX = radius * Math.sin(angle);
                double dY = radius * Math.cos(angle);
                Point t1 = new Point(a.getCenter().getX() + dX, a.getCenter().getY() + dY);
                Point t2 = new Point(b.getCenter().getX() - dX, b.getCenter().getY() - dY);
                LineSegment out = new LineSegment(t1,t2);
                return out;
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    public static LineSegment getRightInnerTangent(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double radius = a.getRadius();
            double distance = center.getMagnitude();
            if(distance > 2 * radius){
                double thetaCenter = center.getTheta();
                double angle = thetaCenter - Math.acos((2*radius)/distance);
                double dX = radius * Math.sin(angle);
                double dY = radius * Math.cos(angle);
                Point t1 = new Point(a.getCenter().getX() + dX, a.getCenter().getY() + dY);
                Point t2 = new Point(b.getCenter().getX() - dX, b.getCenter().getY() - dY);
                LineSegment out = new LineSegment(t1,t2);
                return out;
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    public static Circle getRightTangentCircle(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            double radius = a.getRadius();
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double distance = center.getMagnitude();
            if(distance < 4 * radius){
                double theta = center.getTheta() + Math.acos(distance/(4 * radius));
                double dX = 2 * radius * Math.sin(theta);
                double dY = 2 * radius * Math.cos(theta);
                Point p = new Point(a.getCenter().getX() + dX, a.getCenter().getY() + dY);
                Circle out = new Circle(p, radius);
                return out;
            } else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    public static Circle getLeftTangentCircle(Circle a, Circle b){
        if(a.getRadius() == b.getRadius()){
            double radius = a.getRadius();
            LineSegment center = new LineSegment(a.getCenter(), b.getCenter());
            double distance = center.getMagnitude();
            if(distance < 4 * radius){
                double theta = center.getTheta() - Math.acos(distance/(4 * radius));
                double dX = 2 * radius * Math.sin(theta);
                double dY = 2 * radius * Math.cos(theta);
                Point p = new Point(a.getCenter().getX() + dX, a.getCenter().getY() + dY);
                Circle out = new Circle(p, radius);
                return out;
            } else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    public static CCCPath generateRLRPath(Pose a, Pose b, double radius){
        Circle c = a.getRightCircle(radius);
        Circle d = b.getRightCircle(radius);

        LineSegment center = new LineSegment(c.getCenter(), d.getCenter());
        double distance = center.getMagnitude();
        if(distance < 4 * radius){
            double theta = center.getTheta() - Math.acos(distance/(4 * radius));
            double dX = radius * Math.sin(theta);
            double dY = radius * Math.cos(theta);
            Point p = new Point(c.getCenter().getX() + 2*dX, c.getCenter().getY() + 2*dY);
            Circle e = new Circle(p, radius);

            Point t1 = new Point(c.getCenter().getX() + dX, c.getCenter().getY() + dY);
            theta = center.getTheta() + Math.acos(distance/(4 * radius));
            dX = radius * Math.sin(theta);
            dY = radius * Math.cos(theta);
            Point t2 = new Point(e.getCenter().getX() + dX, e.getCenter().getY() + dY);

            Arc a1 = new Arc(c.getCenter(), radius, a, t1, Arc.Direction.RIGHT);
            Arc a2 = new Arc(e.getCenter(), radius, t1, t2, Arc.Direction.LEFT);
            Arc a3 = new Arc(d.getCenter(), radius, t2, b, Arc.Direction.RIGHT);
            CCCPath out = new CCCPath(a1,a2,a3, DubinsPath.PATHTYPE.RLR);
            return out;
        } else{
            return new CCCPath(false);
        }
    }

    public static CCCPath generateLRLPath(Pose a, Pose b, double radius){
        Circle c = a.getLeftCircle(radius);
        Circle d = b.getLeftCircle(radius);

        LineSegment center = new LineSegment(c.getCenter(), d.getCenter());
        double distance = center.getMagnitude();
        if(distance < 4 * radius){
            double theta = center.getTheta() + Math.acos(distance/(4 * radius));
            double dX = radius * Math.sin(theta);
            double dY = radius * Math.cos(theta);
            Point p = new Point(c.getCenter().getX() + 2*dX, c.getCenter().getY() + 2*dY);
            Circle e = new Circle(p, radius);

            Point t1 = new Point(c.getCenter().getX() + dX, c.getCenter().getY() + dY);
            theta = center.getTheta() - Math.acos(distance/(4 * radius));
            dX = radius * Math.sin(theta);
            dY = radius * Math.cos(theta);
            Point t2 = new Point(e.getCenter().getX() + dX, e.getCenter().getY() + dY);

            Arc a1 = new Arc(c.getCenter(), radius, a, t1, Arc.Direction.LEFT);
            Arc a2 = new Arc(e.getCenter(), radius, t1, t2, Arc.Direction.RIGHT);
            Arc a3 = new Arc(d.getCenter(), radius, t2, b, Arc.Direction.LEFT);
            CCCPath out = new CCCPath(a1,a2,a3, DubinsPath.PATHTYPE.LRL);
            return out;
        } else{
            return new CCCPath(false);
        }
    }

    public static CSCPath generateRSRPath(Pose a, Pose b, double radius){
        Circle c = a.getRightCircle(radius);
        Circle d = b.getRightCircle(radius);
        LineSegment l2 = Lester.getLeftOuterTangent(c,d);

        Arc a1 = new Arc(c.getCenter(), radius, a, l2.getA(), Arc.Direction.RIGHT);
        Arc a3 = new Arc(d.getCenter(), radius, l2.getB(), b, Arc.Direction.RIGHT);
        CSCPath out = new CSCPath(a1, l2, a3, DubinsPath.PATHTYPE.RSR);
        return out;
    }

    public static CSCPath generateLSLPath(Pose a, Pose b, double radius){
        Circle c = a.getLeftCircle(radius);
        Circle d = b.getLeftCircle(radius);
        LineSegment l2 = Lester.getRightOuterTangent(c,d);

        Arc a1 = new Arc(c.getCenter(), radius, a, l2.getA(), Arc.Direction.LEFT);
        Arc a3 = new Arc(d.getCenter(), radius, l2.getB(), b, Arc.Direction.LEFT);
        CSCPath out = new CSCPath(a1, l2, a3, DubinsPath.PATHTYPE.LSL);
        return out;
    }

    public static CSCPath generateRSLPath(Pose a, Pose b, double radius){
        Circle c = a.getRightCircle(radius);
        Circle d = b.getLeftCircle(radius);
        if(Lester.getDistance(c.getCenter(), d.getCenter()) > 2 * radius){
            LineSegment l2 = Lester.getRightInnerTangent(c,d);

            Arc a1 = new Arc(c.getCenter(), radius, a, l2.getA(), Arc.Direction.RIGHT);
            Arc a3 = new Arc(d.getCenter(), radius, l2.getB(), b, Arc.Direction.LEFT);
            CSCPath out = new CSCPath(a1, l2, a3, DubinsPath.PATHTYPE.RSL);
            return out;
        } else{
            return new CSCPath(false);
        }
    }

    public static CSCPath generateLSRPath(Pose a, Pose b, double radius){
        Circle c = a.getLeftCircle(radius);
        Circle d = b.getRightCircle(radius);
        if(Lester.getDistance(c.getCenter(), d.getCenter()) > 2 * radius){
            LineSegment l2 = Lester.getLeftInnerTangent(c,d);

            Arc a1 = new Arc(c.getCenter(), radius, a, l2.getA(), Arc.Direction.LEFT);
            Arc a3 = new Arc(d.getCenter(), radius, l2.getB(), b, Arc.Direction.RIGHT);
            CSCPath out = new CSCPath(a1, l2, a3, DubinsPath.PATHTYPE.LSR);
            return out;
        } else{
            return new CSCPath(false);
        }
    }

    public static DubinsPath generateDubinsPath(Pose a, Pose b, double radius) {
        ArrayList<DubinsPath> paths = new ArrayList<DubinsPath>();

        paths.add(Lester.generateRLRPath(a, b, radius));
        paths.add(Lester.generateLRLPath(a, b, radius));
        paths.add(Lester.generateRSRPath(a, b, radius));
        paths.add(Lester.generateLSLPath(a, b, radius));
        paths.add(Lester.generateRSLPath(a, b, radius));
        paths.add(Lester.generateLSRPath(a, b, radius));

        boolean first = true;
        double minimum = 0;
        DubinsPath out = new CSCPath(false);
        for(DubinsPath p : paths){
            if(p.isValid()){
                if(first){
                    first = false;
                    minimum = p.getLength();
                } else{
                    if(minimum > p.getLength()){
                        minimum = p.getLength();
                        out = p;
                    }
                }
            }
        }
        return out;
    }
}
