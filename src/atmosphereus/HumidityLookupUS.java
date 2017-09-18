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
    
    public static double vaporPressure(double dewPoint_C){
        double vp = saturationVaporPressure(dewPoint_C, false); // vapor Pressure in kPa
//        vp = vp / PSI2KPA;  // vapor pressure in psi
        return vp;  // units are kPa
    }
    
    public static double saturationVaporPressure(double temperature_C, boolean... overIce){
        // Check if overIce is defined
        boolean iceSFC;
        if (overIce.length > 0)
            iceSFC = overIce[0];
        else
            iceSFC = false;
        
        double L;
        if (iceSFC)
            L = 2.83e6;         // Latent heat of deposition over ice (J/kg)
        else
            L = 2.5e6;          // Latent heat of vaporization over water (J/kg)
        
        double e0 = 0.6108;    // kPa
        double Rv = 461;        // J/K/kg
        double T = 273.15;      // K
        double T0 = temperature_C + C2K;
        
//        double es = e0*Math.exp(L/Rv * (1/T0 - 1/T));   // SVP in kPa
        double es = e0*Math.exp(17.27*temperature_C/(temperature_C+237.3));
        
        return es;  // units are kPa
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
    
    public static double discomfortIndex(double temperature, double dewPoint){
        // temperature and dewPoint (degF)
        // Defined as a function of wet bulb temp DI = 0.4 (td + tw) + 15
        double DI = 0.55*temperature + 0.2*dewPoint + 17.5;
        return DI;
    }
    
}
