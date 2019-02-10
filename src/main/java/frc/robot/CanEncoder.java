package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.explodingbacon.bcnlib.framework.PIDSource;

public class CanEncoder implements PIDSource {
    int zero = 0;
    TalonSRX talon;

    public CanEncoder(TalonSRX talon){
        this.talon = talon;
    }

    public CanEncoder(int port){
        talon = new TalonSRX(port);
    }

    public int getCurrentPosition(){
        return talon.getSelectedSensorPosition() - zero;
    }

    public int getCurrentVelocity(){
        return talon.getSelectedSensorVelocity();
    }

    @Override
    public double getForPID() {
        return talon.getSelectedSensorPosition();
    }

    @Override
    public void reset() {
        //zero = talon.getSelectedSensorPosition();
        //System.out.println("Zero: " + zero);
        talon.setSelectedSensorPosition(0);
    }
}
