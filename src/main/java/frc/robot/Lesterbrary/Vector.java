package frc.robot.Lesterbrary;

public class Vector {
    private double i, j, magnitude, theta, slope;

    public Vector(double i, double j){
        this.i = i;
        this.j = j;
        this.magnitude = Math.sqrt(Math.pow(i,2) + Math.pow(j,2));
        this.theta = Math.atan2(i,j);
        this.slope = j/i;
    }

    public double getI(){
        return i;
    }

    public double getJ(){
        return j;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public double getTheta(){
        return theta;
    }

    public double getSlope(){
        return slope;
    }
}
