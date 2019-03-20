package frc.robot;

import edu.wpi.first.wpilibj.I2C;

import java.nio.ByteBuffer;

public class RevColorDistance {
    I2C sensor;
    final int CMD_REGISTER = 0x80;
    final int MULTI_BIT_REGISTER = 0x20;

    public RevColorDistance() {
        sensor = new I2C(I2C.Port.kOnboard, 0x39);
        sensor.write(CMD_REGISTER | 0x00, 0b00000101);
        sensor.write(CMD_REGISTER | 0x0E, 0b1111);
    }

    public ByteBuffer getDistance() {
        ByteBuffer lower = ByteBuffer.allocate(2);
        byte[] upper = new byte[2];
        sensor.read(CMD_REGISTER | 0x1c, 2, lower);
        return lower;
        /*ByteBuffer upper = ByteBuffer.allocate(2);
        sensor.read(0x12,1,upper);
        return upper;*/
    }
}
