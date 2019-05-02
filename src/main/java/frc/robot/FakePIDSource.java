package frc.robot;

import com.explodingbacon.bcnlib.framework.PIDSource;

public class FakePIDSource implements PIDSource {
    public double current;

    public FakePIDSource() {
        current = 0;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    @Override
    public double getForPID() {
        return current;
    }

    @Override
    public void reset() {

    }
}
