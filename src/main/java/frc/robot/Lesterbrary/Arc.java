package frc.robot.Lesterbrary;

public class Arc extends Circle {
    private double length, angle;
    private Direction d;
    private Point a, b;
    private Pose p1, p2;
    private LineSegment l1, l2;

    public enum Direction{
        LEFT,
        RIGHT
    }

    public Arc(Point center, double radius, Point a, Point b, Direction d) {
        super(center, radius);
        this.a = a;
        this.b = b;
        this.d = d;
        l1 = new LineSegment(center, a);
        l2 = new LineSegment(center, b);
        this.angle = calculateAngle();
        this.length = calculateLength();
        this.p1 = calculatePose(a, l1.getTheta());
        this.p2 = calculatePose(b, l2.getTheta());
    }

    public double calculateAngle(){
        double theta = (d == Direction.LEFT) ? l1.getTheta() - l2.getTheta() : l2.getTheta() - l1.getTheta();
        if(theta < 0) theta += 2 * Math.PI;
        return theta;
    }

    public double calculateLength(){
        double proportion = angle/(2 * Math.PI);
        return this.getCircumference() * proportion;
    }

    public Pose calculatePose(Point p, double theta){
        Pose out;
        if(d == Direction.LEFT){
            out = new Pose(p.getX(), p.getY(), theta - Math.PI/2);
        } else{
            out = new Pose(p.getX(), p.getY(), theta + Math.PI/2);
        }
        return out;
    }

    public double getAngle(){
        return angle;
    }

    public double getLength(){
        return length;
    }

    public Pose getPose1(){
        return p1;
    }

    public Pose getPose2(){
        return p2;
    }

    public LineSegment getL1() {return l1;}

    public LineSegment getL2() {return l2;}

    public Direction getDirection() {return d;}
}
