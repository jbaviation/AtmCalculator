package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

public class NonStdAtmosphere extends AtmosphereLookupUS {
    
//***************************************************************************************//
/* TEMPERATURE                                                                           */
/* Compute the air temperature and temperature offset based on altitude                                                         */
//***************************************************************************************//
// Initial Test works! (2017-05-29)    
    public static double temperature(double alt, double temp_offset, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double temp  = (TZERO) * theta + temp_offset;
        return temp;
    }

// Initial Test works! (2017-05-29)
    public static double tempOffset(double alt, double temperature, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double toff = (temperature + F2R) - TZERO*theta;
        return toff;
    }
//***************************************************************************************//
/* PRESSURE                                                                              */
/* Compute the air pressure.                                                             */
//***************************************************************************************//
    public static double pressure(double alt, double seaLevelPress, double temp, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        
        double seaLevelPress_psi = seaLevelPress * INHG2PSI;

//**************************************************************************************//        
     // Comes from https://wahiduddin.net/calc/density_altitude.htm
//          These methods are based on the way that NOAA NWS calcualtes the altimeter 
//          setting for a given station location.  Therefore no temperature component
//          is necessary.

//        double plocal_inHg = Math.pow((Math.pow(seaLevelPress,0.1903) - 1.313e-5*alt),5.255);
        double plocal_inHg = Math.pow(Math.pow(seaLevelPress,1.0/5.255) - 
                1.313e-5*alt,1.0/0.1903);
//**************************************************************************************//       
     // Alternate method   
//          Engineering method of calculating absolute pressure based on temperature.


// Select which conversion method to use
        double press = plocal_inHg;
        int option = 1;     // 0 = inHg
                            // 1 = psi
        if (option == 1)
            press = press * INHG2PSI;
        
        return press;
    }

//***************************************************************************************//
/* DENSITY                                                                               */
/* Compute the air density.                                                              */
//***************************************************************************************//
    public static double density(double alt, double temp_offset, double seaLevelPressure, double... latitude){
        double lat = latCheck(latitude);
        double temp = temperature(alt,temp_offset,lat);
        double press= pressure(alt,seaLevelPressure,lat);
        
        double rho   = press / (RAIR * (temp+F2R));
        return rho;
    }
    
/*****************************************************************************************/
/* Viscosity Calculation                                                                 */                 
/* Compute the dynamic viscosity using Sutherland's Law and kinematic viscosity.         */
/* All Altitudes.                                                                        */
/*****************************************************************************************/
    public static double dynamicViscosity(double alt, double temp_offset, double... latitude){
        double lat = latCheck(latitude);
        double t=temperature(alt,temp_offset,lat);                              // temperature in degR
        double mu=SUTHC1*Math.pow(t,1.5)/(t+SUTHTEMP);     // viscosity in lbm/(ft*s)
        return mu;          
    }
    
    public static double kinematicViscosity(double alt, double temp_offset, double seaLevelPressure, double... latitude){
        double lat = latCheck(latitude);
        double mu = dynamicViscosity(alt,temp_offset,lat);
        double rho= density(alt,temp_offset,seaLevelPressure,lat);
        double nu = mu / rho;
        return nu;
    }

//***************************************************************************************//
/* SPEED OF SOUND                                                                        */
/* Compute the speed of sound.                                                           */
//***************************************************************************************//
    public static double speedOfSound(double alt, double temp_offset, double... latitude){
        double lat = latCheck(latitude);
        
        double temp = temperature(alt,temp_offset,lat);
        double a    = Math.sqrt(GAM * RAIR * temp) * FPS2KTS;
        return a;
    }

}
