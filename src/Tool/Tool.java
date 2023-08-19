package Tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

    public static Double getDirection(Double[] lonLat1, Double[] lonLat2){
        double lon1 = lonLat1[0];
        double lat1 = lonLat1[1];
        double lon2 = lonLat2[0];
        double lat2 = lonLat2[1];
        double deltaLon = lon2 - lon1;
        double deltaLat = lat2 - lat1;
        return Math.atan2(deltaLat, deltaLon)/Math.PI*180;
    }


    public static double getAngle(double direction1, double direction2){
        double angle = Math.abs(direction2 - direction1);
        if(angle > 180) angle = 360 - angle;
        return angle;
    }

    private static double EARTH_RADIUS = 6378.137;
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000) ;
        return s;
    }

    public static double getDistance(Double[] location1, Double[] location2)
    {
        double lat1 = location1[1];
        double lng1 = location1[0];
        double lat2 = location2[1];
        double lng2 = location2[0];
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000) ;
        return s;
    }
    public static double getDistance(double[] location1, double[] location2)
    {
        double lat1 = location1[1];
        double lng1 = location1[0];
        double lat2 = location2[1];
        double lng2 = location2[0];
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000) ;
        return s;
    }

    public static void log(Object s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" +sdf.format(new Date()) + "] " + s);
    }

    public static Process callShell(String[] cmd) {
        return null;
    }
}
