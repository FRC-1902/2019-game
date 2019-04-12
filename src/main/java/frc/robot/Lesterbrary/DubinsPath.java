package frc.robot.Lesterbrary;

import java.util.Comparator;

public abstract class DubinsPath {
    private PATHTYPE p;
    private boolean isValid;

    protected DubinsPath(PATHTYPE p){
        this.p = p;
        if(p != PATHTYPE.INVALID){
            isValid = true;
        } else{
            isValid = false;
        }
    }

    public enum PATHTYPE {
        RLR,
        LRL,
        RSR,
        LSL,
        RSL,
        LSR,
        INVALID
    }

    public boolean isValid(){
        return isValid;
    }

    public void setType(PATHTYPE p){
        this.p = p;
    }

    public PATHTYPE getType(){
        return p;
    }

    public double getLength(){
        return -1;
    }
}
