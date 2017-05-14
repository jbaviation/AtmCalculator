package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

public class HumidityLookupUS extends AtmosphereLookupUS {
    
    public static double dewPoint(double temperature, double relativeHumidity){
        double dp = 0;
        
// INCOMPLETE
        
        return dp;
    }
    
    public static double relativeHumidity(double temperature, double dewPoint){
        double rh = 0;
        
// INCOMPLETE
        
        return rh;
    }
    
    public static double vaporPressure(double dewPoint){
        double dewPoint_C = f2c(dewPoint);
        double vp = 6.11 * Math.pow(10.0,(7.5*dewPoint_C)/(237.7+dewPoint_C));
        
        return vp;
    }
    
    public static double millibar2inchHg(double pressuremb){
        double inHg = 0;
        
// INCOMPLETE
        
        return inHg;
    }
    
    public static double inchHg2millibar(double pressureInchHg){
        double mb = 0;
        
// INCOMPLETE
        
        return mb;
    }
    
    

    public static double windChill(double temperature, double windSpeed){
        double wc = 0;
        
// INCOMPLETE 
        
        return wc;
    }
    
    public static double heatIndex(double temperature, double dewPoint){
        double hi = 0;
        
// INCOMPLETE
        
        return hi;
    }
    
}
