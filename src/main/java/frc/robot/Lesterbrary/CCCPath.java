package frc.robot.Lesterbrary;

public class CCCPath extends DubinsPath{
    private Arc a1, a2, a3;
    private double length;

    public CCCPath(Arc a1, Arc a2, Arc a3, PATHTYPE p){
        super(p);
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.length = a1.getLength() + a2.getLength() + a3.getLength();
    }

    public CCCPath(boolean isValid){
        super(PATHTYPE.INVALID);
    }

    @Override
    public double getLength(){
        return length;
    }

    public Arc getA1(){
        return a1;
    }

    public Arc getA2(){
        return a2;
    }

    public Arc getA3(){
        return a3;
    }

    public Pose getPose1(){
        return a1.getPose1();
    }

    public Pose getPose2(){
        return a1.getPose2();
    }

    public Pose getPose3(){
        return a2.getPose2();
    }

    public Pose getPose4(){
        return a3.getPose2();
    }

    public String getEquations(){
        String out = this.getPose1().getEquation();
        out += "\n" + this.getPose4().getEquation();
        out += "\n" + this.a1.getEquation();
        out += "\n" + this.a3.getEquation();
        out += "\n" + this.a2.getEquation();
        return out;
    }
}
