package frc.robot.Lesterbrary;

public class LineSegment extends Vector {
    private Point a,b;
    private double intercept;

    public LineSegment(Point a, double i, double j) {
        super(i, j);
        this.a = a;
        this.b = new Point(a.getX() + i, a.getY() + j);
        this.intercept = a.getY() - (this.getSlope() * a.getX());
    }

    public LineSegment(Point a, Point b){
        super(b.getX() - a.getX(), b.getY() - a.getY());
        this.a = a;
        this.b = b;
        this.intercept = a.getY() - (this.getSlope() * a.getX());
    }

    public Point getA(){
        return a;
    }

    public Point getB(){
        return b;
    }

    public double getIntercept(){
        return intercept;
    }

    public String getEquation(){
        return "y = " + this.getSlope() + "x + " + this.getIntercept();
    }
}
