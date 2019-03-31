package frc.robot.Lesterbrary;

public class Circle {
    private double radius, circumference;
    private Point center;

    public Circle(Point center, double radius){
        this.center = center;
        this.radius = radius;
        this.circumference = 2 * Math.PI * radius;
    }

    public double getRadius(){
        return radius;
    }

    public Point getCenter(){
        return center;
    }

    public double getCircumference(){
        return circumference;
    }

    public String getEquation(){
        return "(x - " + center.getX() + ")^2 + (y - " + center.getY() + ")^2 = " + radius +"^2";
    }
}
