package frc.robot.Lesterbrary;

public class Pose extends Point {
    private double theta;

    public Pose(double x, double y, double theta) {
        super(x, y);
        this.theta = theta;
    }

    public double getTheta(){
        return theta;
    }

    public String getEquation(){
        double slope = Math.tan(Math.PI/2 - getTheta());
        double intercept = this.getY() - this.getX() * slope;
        return "y = " + slope + "x + " + intercept;
    }

    public Circle getLeftCircle(double radius){
        double dX = radius * Math.cos(theta);
        double dY = radius * Math.sin(theta);
        return new Circle(new Point(this.getX() - dX, this.getY() + dY), radius);
    }

    public Circle getRightCircle(double radius){
        double dX = radius * Math.cos(theta);
        double dY = radius * Math.sin(theta);
        return new Circle(new Point(this.getX() + dX, this.getY() - dY), radius);
    }

    public Circle[] getTangentCircles(double radius){
        double dX = radius * Math.cos(theta);
        double dY = radius * Math.sin(theta);
        Circle[] out = new Circle[2];

        out[0] = new Circle(new Point(this.getX() - dX, this.getY() + dY), radius);
        out[1] = new Circle(new Point(this.getX() + dX, this.getY() - dY), radius);
        return out;
    }
}
