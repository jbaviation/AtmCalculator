package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

public class NonStdAtmosphere extends AtmosphereLookupUS {
    
//***************************************************************************************//
/* TEMPERATURE                                                                           */
/* Compute the air temperature.                                                          */
//***************************************************************************************//
    public static double temperature(double alt, double temp_offset, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double temp  = (TZERO + temp_offset) * theta;
        return temp;
    }

//***************************************************************************************//
/* PRESSURE                                                                              */
/* Compute the air pressure.                                                             */
//***************************************************************************************//
    public static double pressure(double alt, double seaLevelPress, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        
        double seaLevelPress_psi = seaLevelPress * INHG2PSI;
        
     // Comes from https://wahiduddin.net/calc/density_altitude.htm
        double plocal_inHg = Math.pow((Math.pow(seaLevelPress,0.1903) - 1.313e-5*alt),
                5.255);
        
     // Alternate method   
//        double delta = pressureRatio(alt,lat);
//        double pressAlt = seaLevelPress_psi * delta;
//        double pressAlt_inHg = pressAlt / INHG2PSI;
        
     // Select which method to use and convert to PSI
        double press = plocal_inHg * INHG2PSI;
        
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
