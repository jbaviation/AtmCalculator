package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

public class StdAtmosphere extends AtmosphereLookupUS {
    

//***************************************************************************************//
/* TEMPERATURE                                                                           */
/* Compute the air temperature.                                                          */
//***************************************************************************************//
    public static double temperature(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double temp  = TZERO * theta;
        return temp;
    }

//***************************************************************************************//
/* PRESSURE                                                                              */
/* Compute the air pressure.                                                             */
//***************************************************************************************//
    public static double pressure(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double delta = pressureRatio(alt,lat);
        double press = PZERO * delta;
        
     // Select which conversion method to use
        int option = 1;     // 0 = inHg
                            // 1 = psi
        if (option == 0)
            press = press / INHG2PSI;
        return press;
    }

//***************************************************************************************//
/* DENSITY                                                                               */
/* Compute the air density.                                                              */
//***************************************************************************************//
    public static double density(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double sigma = densityRatio(alt,lat);
        double rho   = RHOZERO * sigma;
        return rho;
    }
    
/*****************************************************************************************/
/* Viscosity Calculation                                                                 */                 
/* Compute the dynamic viscosity using Sutherland's Law and kinematic viscosity.         */
/* All Altitudes.                                                                        */
/*****************************************************************************************/
    public static double dynamicViscosity(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double t=theta*TZERO;                              // temperature in degR
        double mu=SUTHC1*Math.pow(t,1.5)/(t+SUTHTEMP);     // viscosity in lbm/(ft*s)
        return mu;          
    }
    
    public static double kinematicViscosity(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double mu = dynamicViscosity(alt,lat);
        double rho= density(alt,lat);
        double nu = mu / rho;
        return nu;
    }

//***************************************************************************************//
/* SPEED OF SOUND                                                                        */
/* Compute the speed of sound.                                                           */
//***************************************************************************************//
    public static double speedOfSound(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);
        double theta = temperatureRatio(alt,lat);
        double a     = AZERO * Math.sqrt(theta) * FPS2KTS;
        return a;
    }
}
