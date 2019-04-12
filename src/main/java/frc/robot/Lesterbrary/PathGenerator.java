package frc.robot.Lesterbrary;

import frc.robot.utils.Path;
import frc.robot.utils.Translation2d;

import java.util.ArrayList;

public class PathGenerator {
   /* public static Path generatePath(Pose a, Pose b, double radius, int resolution){
        DubinsPath d = Lester.generateDubinsPath(a,b,radius);
        DubinsPath.PATHTYPE type = d.getType();
        CCCPath ccc;
        CSCPath csc;
        boolean isCSC;

        if(type == DubinsPath.PATHTYPE.LRL || type == DubinsPath.PATHTYPE.RLR){
            ccc = (CCCPath)d;
            isCSC = false;
        } else{
            csc = (CSCPath)d;
            isCSC = true;
        }

        ArrayList<Path.Waypoint> path = new ArrayList<Path.Waypoint>();
        double segLength = d.getLength()/resolution;
        double currentLength = 0;

        if(isCSC){

            for(int i = 0; i < resolution; i++){
                try{
                    if(currentLength + segLength < csc.getA1().getLength()){

                    }
                }
            }
        }
    }*/

   /* public Path.Waypoint poseToWaypoint(Pose p){
        double x = p.getY();
        double y = -p.getX();
    }*/
}
