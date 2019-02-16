package frc.robot;

import com.explodingbacon.bcnlib.framework.PIDSource;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class Potentiometer implements PIDSource {
    AnalogPotentiometer pot;

    public Potentiometer(AnalogPotentiometer pot){
        this.pot = pot;
    }

    public Potentiometer(int port){
        this.pot = new AnalogPotentiometer(port);
    }

    public double getCurrentPosition(){
        return pot.get();
    }

    @Override
    public double getForPID() {
        return pot.get();
    }

    @Override
    public void reset() {

    }
}
