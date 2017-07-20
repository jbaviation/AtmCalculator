package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

/*
 * @author Jon Borman (HX5/Sierra JV TFOME-II at the NASA Glenn Research Center)
 *  NOTE: equations valid between -90 to 90 deg latitude and -5000 to 282,000 ft
 */

public class AtmosphereLookupUS extends AtmosphereUS {

//  Conversion Constants
    static double FT2METERS     = 0.3048;           //mult feet to get meters
    static double KELVIN2RANKINE= 1.8;              //mult Kelvin to get deg Rankine
    static double PSF2NSM       = 47.877874;        //mult lb/ft^2 to get N/m^2
    static double PSI2NSM       = 6894.4138;        //mult lb/in^2 to get N/m^2
    static double SCF2KCM       = 515.379;          //mult slugs/ft^3 to get kg/m^3
    static double KM2NM         = 1.852;            //mult km to get nautical miles
    static double MS2KTS        = 900/463.0;        //mult m/s to get kts
    static double MB2INHG       = 33.8639;          //mult pressure millibars to get inHg
    static double INHG2PSI      = 0.491154;         //mult pressure inHg to get psi
    static double PSI2KPA       = 6.89476;          //mult pressure psi to get kPascals
    static double FT2AU         = 2.03746e-12;      //mult feet to get astronomical units
    
//  Conversion Constants (US)
    static double GC            = 32.174;           //mult slugs to get lbm
    static double NM2FT         = 2315000/381.0;    //mult nautical miles to get feet
    static double MI2NM         = 57875/50292.0;    //mult miles to get nautical miles 
    static double FPS2KTS       = 0.592484;         //mult fps to get kts
    static double F2R           = 459.67;           //add degF to get degR
    static double C2K           = 273.15;           //add degC to get Kelvin

// Physical Constants
    static double TZERO          = 518.67;        // sea level temperature (degR)
    static double PZERO          = 14.696;        // sea-level pressure (psi)
    static double RHOZERO        = 0.0764776;     // sea level density (lbm/ft3)
    static double AZERO          = 661.479;       // sea-level speed of sound (kts)
    static double REARTHEQ       = 3443.91847;    // radius of the Earth at equator (nm)
    static double REARTHPOL      = 3432.37165;    // radius of the Earth at pole (nm)
    static double GMR            = 18.7432953;    // gas constant= g0*M0/r* (degR/ft)
    static double GAM            = 1.4;           // ratio of specific heats of air (dimless)
    static double RAIR           = 53.3533;       // specific gas constant air (ft*lbf/lbm/degR)
    static double SUTHC1         = 7.30240704e-07;// Sutherland constant (lbm/[ft*s*sqrt(degR)])
    static double SUTHTEMP       = 198.72;        // Sutherland temperature (degR)
    static int    NTAB           = 8;             // number of increment changes (elements in xtab)
    
//  Orbital psuedo-constants
    static double PEARTH;                         // period of the Earth around sun (clock days)
    static double APHELIONJULIANDAY;              // Julian day of earth's aphelion
    static double PERIHELIONJULIANDAY;            // Julian day of earth's perihelion
    static double APHELIONDISTANCE;               // ellipical aphelion distance from sun (feet)
    static double PERIHELIONDISTANCE;             // ellipical perihelion distance from sun (feet)
    static double SUMMERJULIANDAY;                // summer solstice Julian day
    static double WINTERJULIANDAY;                // winter solstice Julian day
    
    static int PRESENTYEAR;                       // present year
    static double PRESENTJULIANDAY;               // present Julian day

//  Height, temperature, pressure, and lapse rate discontinunities
    static double htab[] = {0.0,36089.24,65616.80,104986.88,154199.48,167322.83,232939.63,278385.83}; // height (ft)
    static double ttab[] = {518.67,389.97,389.97,411.57,487.17,487.17,386.37,336.5028};  // temp (degR)
    static double ptab[] = {1.0,2.2336110E-1,5.4032950E-2,8.5666784E-3,1.0945601E-3,6.6063531E-4,3.9046834E-5,3.68501E-6}; // press (ratio to sl)
    static double gtab[] = {-3.56616,0.0,0.54864,1.536192,0.0,-1.536192,-1.09728,0.0}; // temp lapse rate (degR/kft)

//***************************************************************************************//
/* EARTH RADIUS                                                                          */
/* Calculate earth radius at the desired latitude.                                       */
//***************************************************************************************//
    public static double radiusEarth(double... latitude){
        double lat = latCheck(latitude);
        Check(0, lat);
        
        double latrad = lat * Math.PI / 180.0;
        
        double num1 = Math.pow(Math.pow(REARTHEQ,2)*Math.cos(latrad),2);
        double num2 = Math.pow(Math.pow(REARTHPOL,2)*Math.sin(latrad),2);
        double den1 = Math.pow(REARTHEQ*Math.cos(latrad),2);
        double den2 = Math.pow(REARTHPOL*Math.sin(latrad),2); 
        
        double rnm=Math.sqrt((num1 + num2)/(den1 + den2));  // Earth's radius corrected for latitude (nm)
        double rft=rnm*NM2FT;   // Earth's radius corrected for latitude (ft)
        return rft;
    }

//***************************************************************************************//
/* ORBITAL PARAMETERS                                                                    */
/* Calculate the true anomaly, exact distance between the sun and earth,                 */
/* horizon distance calculator, and horizon curvature                                    */
//***************************************************************************************//

//  Calculate the true anomaly
    public static double trueAnomaly(double julianDay){
        double tao = PERIHELIONJULIANDAY;   // Perihelion Julian Day
        
        // Calculate mean anomaly first, M
        double M = 2.0*Math.PI * (julianDay-tao)/PEARTH;
        
        // Determine true anomaly, nu
        double nu = M + 0.0333988*Math.sin(M) + 0.0003486*Math.sin(2*M) +
                      0.000005*Math.sin(3*M);
        
        return nu * 180 / Math.PI;  // Return true anomaly in degrees
    }
    
//  Calculate the distance between the sun and earth-moon barycenter
    public static double sunEarthDistance(double trueAnomaly_degrees){
        double a = 1/FT2AU;     // Astronomical unit; length of the semi-major axis (m)
        double e = (APHELIONDISTANCE-PERIHELIONDISTANCE)/
                   (APHELIONDISTANCE+PERIHELIONDISTANCE); // Earth's eccentricity
        double nu= trueAnomaly_degrees * Math.PI / 180;
        double R = a*(1.0-e*e)/(1.0+e*Math.cos(nu));
        
        return R;
    }
    
//  Calculate distance to the horizon
    public static double horizonDistance(double alt_agl, double... latitude){
        double lat = latCheck(latitude);
        Check(alt_agl, lat);
        
        // Young's method of horizon calculator
        double Rprime = 7/6 * radiusEarth(lat);
        double d = Math.sqrt(2*Rprime*alt_agl);
        return d;
    }
    
//  Calculate earth's horizon curvature from left to right
    public static double horizonCurvature(double alt_agl, double... latitude){
        double lat = latCheck(latitude);
        Check(alt_agl, lat);
        
        // Geometric relationship for curvature
        double R = radiusEarth(lat);
        double kappa = Math.sqrt(Math.pow(1+alt_agl/R,2) - 1.0);
        double kappa_d = kappa * 180/Math.PI;
        return kappa_d;
    }
    
    
//***************************************************************************************//
/* LOCAL GRAVITY                                                                         */
/* Calculate local gravitational acceleration due to latitude and altitude.              */
//***************************************************************************************//
    public static double gravityAcceleration(double alt, double... latitude){
        double lat = latCheck(latitude);
        Check(alt, lat);       
        
        double latrad = lat * Math.PI / 180.0;
        
        // Calculate gravity based on latitude (IGF 1967)
        double gLat;
        double c1 = 9.7803270;
        double c2 = 0.0053024;
        double c3 = 0.0000058;
        gLat=c1*(1 + c2*Math.pow(Math.sin(latrad),2) - c3*Math.pow(Math.sin(2*latrad),2));
                
        // Calculate gravity based on altitude (IGF 1967 FAC)
        double gAlt;
        c1 = 3.086e-6;
        gAlt = c1*alt*FT2METERS;
        
        
        // Calculate gravity based on both
        double g = (gLat - gAlt) / FT2METERS;
        
        return g;
        
    }
    
//***************************************************************************************//
/* GEOPOTENTIAL ALTITUDE                                                                 */
/* Convert geometric altitude to geopotential altitude                                   */
//***************************************************************************************//
    public static double geoPotential(double alt, double... latitude) {
     // Use first element of lat
        double lat = latCheck(latitude);
        Check(alt, lat);
        
        double r = radiusEarth(lat);
        double h=alt*r / (alt+r);
        return h;
    }

//***************************************************************************************//
/* TEMPERATURE RATIO                                                                     */
/* Compute the ratio of temperature to sea-level temperature in the standard             */
/* atmosphere. Correct to 86 km.  Only approximate thereafter.                           */
//***************************************************************************************//
    public static double temperatureRatio(double alt, double... latitude) { // geometric altitude (ft)
        // Use first element of lat
        double lat = latCheck(latitude);
        Check(alt, lat);
        
        int i,j,k;

        double h=geoPotential(alt,lat);         // geometric to geopotential altitude

        i=0; j=NTAB-1;  // starting values for binary search
        for (i=0, j=NTAB-1; j > i+1; ){
            k=(i+j)/2;
            if (h < htab[k]) 
                j=k;
            else 
                i=k;
        }

        double tgrad=gtab[i];                      // temp. gradient of local layer
        double tbase=ttab[i];                      // base temp. of local layer
        double deltah=h-htab[i];                   // height above local base
        double tlocal=tbase+tgrad*deltah/1000;     // local temperature
        double theta=tlocal/ttab[0];               // temperature ratio

        return theta;
    } 

//***************************************************************************************//
/* PRESSURE RATIO                                                                        */
/* Compute the ratio of pressure to sea-level temperature in the standard                */
/* atmosphere. Correct to 86 km.  Only approximate thereafter.                           */
//***************************************************************************************//
    public static double pressureRatio(double  alt, double... latitude) {     // geometric altitude (ft)
        double lat = latCheck(latitude);
        Check(alt, lat);
        
        double delta;
        int i,j,k;

        double h=geoPotential(alt,lat);         // geometric to geopotential altitude

        i=0; j=NTAB-1;  // starting values for binary search
        for (i=0, j=NTAB-1; j > i+1; ){
            k=(i+j)/2;
            if (h < htab[k]) 
                j=k; 
            else 
                i=k;
        } 

        double tgrad=gtab[i];                      // temp gradient of local layer
        double tbase=ttab[i];                      // base temp of local layer
        double deltah=h-htab[i];                   // height above local base
        double tlocal=tbase+tgrad*deltah/1000;     // local temperature

        if (tgrad == 0.0)                          // pressure ratio
            delta=ptab[i]*Math.exp(-GMR*deltah/tbase/1000);
        else
            delta=ptab[i]*Math.pow(tbase/tlocal, GMR/tgrad);

        return delta;
    }
    
//***************************************************************************************//
/* DENSITY RATIO                                                                         */
/* Compute the ratio of density to sea-level temperature in the standard                 */
/* atmosphere. Correct to 86 km.  Only approximate thereafter.                           */
//***************************************************************************************//
    public static double densityRatio(double alt, double... latitude) {
        double lat = latCheck(latitude);
        Check(alt, lat);
        double sigma = pressureRatio(alt,lat)/temperatureRatio(alt,lat);
        return sigma;
    } 

    

//***************************************************************************************//
/* RETRIEVE PSUEDO-CONSTANTS                                                             */
//***************************************************************************************//
    public static void getConstants() throws FileNotFoundException, IOException{
    //  Get current date/time from DateTime.java
        SunEarthGetter.getPresentDay();
        int[] current = new int[6];
        DateTime dy = new DateTime();
        current = dy.dayLookup();
        PRESENTYEAR = current[0];
        String year  = Integer.toString(current[0]).substring(2);
        String month = Integer.toString(current[1]);
        String day   = Integer.toString(current[2]);
        String hour  = Integer.toString(current[3]);
        String min   = Integer.toString(current[4]);
        String sec   = Integer.toString(current[5]);
        
    //  Evaluate if leap year
        String leapYear = "no";
        if (PRESENTYEAR % 4 == 0)
            leapYear = "yes";
        
    //  Combine date/time
        String date = DateTime.convertDateTime(month, day, year, "date");
        date = date.substring(0,5);
        String time = DateTime.convertDateTime(hour, min, sec, "time");
    
    //  Calculate current Julian day
        PRESENTJULIANDAY = DateTime.dateTime2JulianDay(date, leapYear, time);
        
    //  Get time around sun (days) and aphelion/perihelion Julian day
        PEARTH = SunEarthGetter.perihelionInterval();
        APHELIONJULIANDAY = SunEarthGetter.aphelionJulian();
        PERIHELIONJULIANDAY = SunEarthGetter.perihelionJulian();
        
    //  Get solstice days
        SUMMERJULIANDAY = SunEarthGetter.summerJulian();
        WINTERJULIANDAY = SunEarthGetter.winterJulian();
        
    //  Get aphelion and perihelion distances (ft)
        double[] dis = new double[2];
        dis = SunEarthGetter.aphelionPerihelionDistance();
        APHELIONDISTANCE = dis[0] / FT2AU;
        PERIHELIONDISTANCE = dis[1] / FT2AU;
        
       
    }
    
//***************************************************************************************//
/* ADMINISTRATIVE CHECKS                                                                 */
//***************************************************************************************//

//  Convert between trigonometric degrees and compass degrees
    public static double Trig2Comp(double trigDegrees, boolean calculatingWind){
        double compDegrees;

        // Check if the trig angle is greater than 360 or less than 0
        if (trigDegrees > 360 || trigDegrees < 0)
            trigDegrees = trigDegrees % 360;

        // If calculating wind than use the formula from http://wx.gmu.edu/dev/clim301/lectures/wind/wind-uv.html
        if (calculatingWind){
            compDegrees = 270 - trigDegrees;
            if (compDegrees < 0)
                compDegrees = compDegrees + 360;
            else if (compDegrees > 360)
                compDegrees = compDegrees - 360;
        }
        // Else calculating non-wind direction
        else{  
            if (trigDegrees >= 0 && trigDegrees <= 90)
                compDegrees = 90 - trigDegrees;
            else if (trigDegrees > 90 && trigDegrees <= 360)
                compDegrees = 450 - trigDegrees;
            else
                compDegrees = trigDegrees;
        }

        return compDegrees; 
    }
    
//  Convert between compass degrees and trig degrees
    public static double Comp2Trig(double compDegrees, boolean calculatingWind){
        double trigDegrees;
        // Check if the compass angle is greater than 360 or less than 0
        if (compDegrees > 360 || compDegrees < 0)
            compDegrees = compDegrees % 360;
        
        // If calculating wind than use the formula from http://wx.gmu.edu/dev/clim301/lectures/wind/wind-uv.html
        if (calculatingWind){
            trigDegrees = 270 - compDegrees;
        }
        else{
            if (compDegrees >= 0 && compDegrees <= 90)
                trigDegrees = 90 - compDegrees;
            else if (compDegrees > 90 && compDegrees <= 360)
                trigDegrees = 450 - compDegrees;
            else
                trigDegrees = compDegrees;
        }
        
        return trigDegrees;
    }
    
    
// Reciprical Direction
    public static double reciprocal(double degrees){
        double recip;
        if (degrees >= 0 && degrees <= 180)
            recip = degrees + 180;
        else if (degrees > 180 && degrees < 360)
            recip = degrees - 180;
        else{
            recip = degrees % 360;
            if (recip >= 0 && recip <= 180)
                recip = recip + 180;
            else if (recip > 180 && recip < 360)
                recip = recip - 180;
        }
        return recip;
    }

//  Check for a valid altitude and latitude range
    public static void Check(double alt, double lat){

        
        boolean check1 = (alt < -5000) || (alt > 282000);
        boolean check2 = (lat < -90)   || (lat > 90);
        
        if (check1) 
            System.err.println("Results are not valid at altitudes <-5000 ft or >282,000 ft");
        else if (check2)
            System.err.println("Please enter your latitude in degrees between -90° and 90°");
    }

//  Accept the first latitude input
    public static double latCheck(double[] latitude){
    // Use first element of lat
        double lat = 45;        // Use 45 degree latitude if no lat is provided
        if (latitude.length > 0)
            lat = latitude[0];
        else if ((latitude.length > 0) && (WARNLAT == 0)){
            System.out.println("WARNING: No latitude was provided (defaults to 45\u00b0)");
            WARNLAT = 1;
        }
        return lat;
    }
    
//  Accept the first "other" input
    public static double dpCheck(double[] optionalInput){
        double output = -459.67;  // assume dry air
        if (optionalInput.length > 0)
            output = optionalInput[0];
        return output;
    }
    
//  Fahrenheit to Celsius
    public static double f2c(double fahrenheit){
        double celsius = 5.0/9.0 * (fahrenheit - 32.0);
        return celsius;
    }
//  Celsius to Fahrenheit
    public static double c2f(double celsius){
        double fahr = 9.0/5.0*celsius + 32.0;
        return fahr;
    }
    
//  MPH to Knots
    public static double mph2knots(double mph){
        double knots = mph * MI2NM;
        return knots;
    }
//  Knots to MPH
    public static double knots2mph(double knots){
        double mph = knots / MI2NM;
        return mph;
    }
}

