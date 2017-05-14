package atmosphereus;
import java.io.*;
import java.util.*;
import java.math.*;

public class FlightLookupUS extends AtmosphereLookupUS {

//******************************************************************************
//  Speed Related Calculations
//******************************************************************************
    public static double equivalentAirspeedTAS(double tas, double rho){
        double eas = tas * Math.sqrt(rho/RHOZERO);
        return eas;
    }
    public static double trueAirspeedEAS(double eas, double rho){
        double tas = eas * Math.sqrt(RHOZERO/rho);
        return tas;
    }
    public static double trueAirspeedMach(double Mach, double temp){
        double tas = AZERO * Mach * Math.sqrt(temp/TZERO);
        return tas;
    }
    public static double trueAirspeedIAS(double ias, double MSLaltitude){  // check this to verify the equation is correct
        double tas = ias + (0.02*MSLaltitude/1000);
        return tas;
    }

//******************************************************************************
//  Wind Related Calculations
//******************************************************************************
    public static double[] wind(double groundSpeed, double trueAirspeed, double course, double heading){
        double[] windy = new double[2];
                
        double radCourse = Comp2Trig(course,false) * Math.PI / 180;
        double radHeading = Comp2Trig(heading,false) * Math.PI / 180;
        
        double x = groundSpeed*Math.cos(radCourse) - trueAirspeed*Math.cos(radHeading);
        double y = groundSpeed*Math.sin(radCourse) - trueAirspeed*Math.sin(radHeading);
        
        double windspeed = Math.sqrt(x*x + y*y);

        double trigAngle = windTrigAngle(x,y);
        double winddirection = Trig2Comp(trigAngle,true);
        
        windy[0] = Math.round(winddirection);
        windy[1] = Math.round(windspeed);
        
        return windy;
    }
        
    public static double[] headingGSWCA(double windSpeed,double windDirection,
            double tas, double course){
        double output[] = new double[3];
        double hdg = 0;
        double gs = 0;
        double wca;
        double hdg_deg;
        double wca_deg;

     // Find all degrees and radians for each angle
        double windDirection_deg = windDirection;
        double windDir = windDirection_deg * Math.PI/180;
        double crs_deg = course;
        double crs = crs_deg * Math.PI/180;
        
     // Flip the wind direction for reference
        double windDir180_deg = reciprocal(windDirection_deg);
        
     // Wind to track angle
        double wtAngle = (crs_deg - windDir180_deg) * Math.PI/180;
        double wtAngle_deg = crs_deg - windDir180_deg;
        
     // Wind correction angle
        double sinWCA = windSpeed*Math.sin(wtAngle)/tas;
        wca = Math.asin(sinWCA);
        wca_deg = wca * 180 / Math.PI; 
                
     // Heading
        hdg = course + wca;
        hdg_deg = crs_deg + wca_deg;
        
     // Groundspeed
        gs = tas*Math.cos(wca) + windSpeed*Math.cos(wtAngle);
        
     // Set outputs
        output[0] = hdg_deg;
        output[1] = gs;
        output[2] = wca_deg;
        
        return output;
    }
    
    public static double[] courseGSWCA(double windSpeed,double windDirection,
            double tas, double heading){
        double output[] = new double[3];
        double crs;
        double gs;
        double wca;
        double crs_deg;
        double wca_deg;

     // Find all degrees and radians for each angle 
        double windDirection_deg = windDirection;
        double windDir = windDirection_deg * Math.PI/180;
        double hdg_deg = heading;
        double hdg = hdg_deg * Math.PI/180;
        
     // Flip the wind direction for reference
        double windDir180_deg = reciprocal(windDirection_deg);
        double windDir180 = windDir180_deg * Math.PI/180;
        
     // Heading minus windDirection
        double hdg_wd = hdg - windDir;
        double hdg_wd_deg = hdg_wd * 180/Math.PI;
        
     // Groundspeed
        gs = Math.sqrt(windSpeed*windSpeed + tas*tas - 2*windSpeed*tas*Math.cos(hdg_wd));
        
     // Wind Correction Angle
        wca = Math.atan2(windSpeed*Math.sin(hdg_wd) , tas-windSpeed*Math.cos(hdg_wd));
        wca_deg = wca * 180/Math.PI;
        
     // Flight Course
        crs = (hdg + wca);
        crs_deg = (hdg_deg + wca_deg);
        
     // Outputs
        output[0] = crs_deg;
        output[1] = gs;
        output[2] = wca_deg;

        return output;
    }
    

    public static double crossWindComponent(double windSpeed, double windAngle){
        double crossWind = windSpeed * Math.sin(windAngle * Math.PI / 180);
        return crossWind;
    }
    public static double headWindComponent(double windSpeed, double windAngle){
        double headWind = windSpeed * Math.cos(windAngle * Math.PI / 180);
        return headWind;
    }
    public static double[] runwayWindComponents(double runwayHeading, double windDirection,
            double windSpeed, double windGust){
        double windy[] = new double[2];
        double totalwind;
        double headwind;
        double crosswind;
        double xwindAngle;

//      Check if windSpeed is not realistic or negative
        if (windSpeed < 0)
            windSpeed = Math.abs(windSpeed);
        else if (windSpeed > 1000)
            System.err.print("Mother Nature has never created a wind that fast.\n"
                    + "Please enter a more realistic wind speed.\n");
        
//      Check if wind gusts are greater than or equal to the windspeed, otherwise
//      ignore it
        double gustFactor = 0;
        if (windSpeed >= windGust)
            windGust = windSpeed;
        else if (windGust > windSpeed){
            gustFactor = (windGust - windSpeed)/2;
        }
        totalwind = windSpeed + gustFactor;

//      Check if the runwayHeading is a proper runway number
        if (runwayHeading>360 || runwayHeading<0){
            System.err.format("%10.0f is an invalid runway number\n", runwayHeading);
            runwayHeading = 0;
        }
//      Check if the windDirection is valid
        if (windDirection>360 || windDirection<0)
            System.err.format("%10.0f is an invalid wind direction\n", windDirection);

//      Determine the crosswind angle
        xwindAngle = Math.abs(runwayHeading - windDirection);
        
//      Determine the headwind and crosswind totals
        headwind = headWindComponent(totalwind,xwindAngle);
        crosswind = crossWindComponent(totalwind,xwindAngle);
        windy[0] = Math.abs(headwind);
        windy[1] = Math.abs(crosswind);
        
        return windy;
        
    }

//******************************************************************************
//  Performance Related Calculations
//******************************************************************************     
    public static double pressureAltitude(double altitude, double seaLevelPressure){
        double palt = 0;
        double slPress_psi = seaLevelPressure * INHG2PSI;
        double stdPress_psi = StdAtmosphere.pressure(altitude);
        
     // Comes from https://wahiduddin.net/calc/density_altitude.htm
//        double plocal_inHg = Math.pow((Math.pow(seaLevelPressure,0.1903) - 1.313e-5*altitude),
//                5.255);
        double palt_ref = (1 - Math.pow(stdPress_psi/slPress_psi, 0.190284)) * 145366.45;
        palt = (altitude - palt_ref) + altitude;
        
        return palt;
    }
    
    public static double densityAltitude(double altitude, double seaLevelPressure,
            double temperature, double... dewPoint){
        double dewPoint_F = dpCheck(dewPoint);
        double dalt = 0;
        double palt = pressureAltitude(altitude,seaLevelPressure);
     
     // Local Pressure at Altitude
        double plocal_inHg = StdAtmosphere.pressure(palt) / INHG2PSI;
        double plocal_psi = StdAtmosphere.pressure(palt);
        double plocal_mb = plocal_inHg / MB2INHG;
//        double plocal_inHg2= Math.pow((Math.pow(seaLevelPressure,0.1903) - 1.313e-5*altitude),
//                5.255);
//        double plocal_psi2 = plocal_inHg2 * INHG2PSI;
//        double plocal_mb2 = plocal_inHg2 / MB2INHG;
        
     // Convert to proper units
        double dewPoint_C = f2c(dewPoint_F);
        double temp_C = f2c(temperature);
        double temp_K = temp_C + 273.15;
        double temp_F = temperature;
        
     // Calculate the virtual temperature
        double vp = HumidityLookupUS.vaporPressure(dewPoint_F); // vapor pressure
        double Tv_K = temp_K/(1-vp/plocal_mb*(1-0.622));
        double Tv_R = Tv_K * 9/5;
        
     // Calculate the density altitude
        double pre = Math.pow(17.326*plocal_inHg/Tv_R,0.235);
        dalt = 145366 * (1 - pre);
      
        return dalt;
    }
    
//******************************************************************************
//  Other Calculations
//******************************************************************************    
    public static double magneticVariation(double latitude, double longitude){
        double variation = 0;
        
//  INCOMPLETE        
        
        return variation;
    }
//******************************************************************************    
//  Recurrent methods
//******************************************************************************

// Calculate the wind direction angle in trig units
    public static double windTrigAngle(double x, double y){
        double xycomp;
        if (x > 0)
            xycomp = Math.atan(y/x) * 180 / Math.PI;
        else if (x<0 && y>=0)
            xycomp = (Math.atan(y/x)/Math.PI + 1) * 180;
        else if (x<0 && y<0)
            xycomp = (Math.atan(y/x)/Math.PI - 1) * 180;
        else if (x==0 && y>0)
            xycomp = 90;
        else if (x==0 && y<0)
            xycomp = 270;
        else
            xycomp = Double.NaN;
        
        return xycomp;
    }

}
