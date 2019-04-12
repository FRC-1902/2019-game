package frc.robot.positioning;

public class Constants {

    //Wheel Stuff
    public static double wheelDiameterInches = 7.14; //6

    public static double kTrackLengthInches = 27;
    public static double kTrackWidthInches = 33;
    public static double kTrackEffectiveDiameter = (kTrackWidthInches * kTrackWidthInches + kTrackLengthInches * kTrackLengthInches) / kTrackWidthInches;
    public static double kTrackScrubFactor = 1; //0.5

    public static double pathFollowLookahead = 24.0; // inches
    public static double pathFollowMaxVel = 278; // inches/sec //120 for low, 278 for high
    public static double pathFollowMaxAccel = 80.0; // inches/sec^2

    public static double kLooperDt = 0.01;

}
