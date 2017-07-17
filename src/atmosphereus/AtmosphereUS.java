package atmosphereus;

/**
 *
 * @author jborman
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
public class AtmosphereUS {
    public static String DEGREE = "\u00b0";
    public static int WARNLAT = 0;   // track if latitude warning has been used so message doesn't appear multiple times

    
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
/****** DO NOT REMOVE, PREREQUISITE FOR ESTABLISHING CERTAIN CONSTANTS ********/
        AtmosphereLookupUS.getConstants();
/******************************************************************************/
        
//  CHECK ALL NONSTDATMOSPHERE CALCULATIONS

// Checkout horizon calculator
        
        System.out.println("Height\tHorizon Distance");
        for (int i=1; i <= 10; i++){
            double x = (double) i;
            double h = 1.0 * Math.pow(10, x);
            double dis = AtmosphereLookupUS.horizonDistance(h);
            System.out.format("%12.0f\t%12.2f\n", h, dis/5280);
        }

// Checkout sun earth distance
//        double nu = AtmosphereLookupUS.trueAnomaly(115);
//        double dis= AtmosphereLookupUS.sunEarthDistance(nu);
//        System.out.format("true anomaly:\t%5.2f deg\ndistance:\t%6.2f Gft\n", nu, dis*1e-9);

// Checkout SunEarthGetter
        
//        double julianAH = SunEarthGetter.aphelionJulian();
//        double julianPH = SunEarthGetter.perihelionJulian();
//        System.out.format("aphelion = %7.3f\nperihelion = %7.3f\n\n", julianAH, julianPH);

// Check Julian Day
////        String date = "01-01";
//        String time = "18:00:00";
//        String leap = "no";
//        double julDay = 366.99998;
//        int[] moDay = new int[5];
//        moDay = DateTime.monthDay(julDay,1);
//        Date date = new Date();
//        
//        System.out.format("Julian day %5.2f is %d-%d at %d:%d:%d\n", julDay,moDay[0],moDay[1],
//                moDay[2],moDay[3],moDay[4]);

// Temp Offset (Not done yet)
//        double lat = 45;
//        double alt = 1000;
//        double slp = 29.62;
//        double press = StdAtmosphere.pressure(alt, lat);
//        double nonpress = NonStdAtmosphere.pressure(alt, slp, lat);
//        
//        System.out.format("At %7.0f feet\nStandard Pressure = %5.2f\nNon-Std Pressure = %5.2f\n",alt,press,nonpress);

// Pressure and Density Altitude (re-check; DOES NOT WORK)
//      Request latitude (in degrees N or S)
//        double lat = 45;
//        double alt = 50 *1000;
//        double slp = 29.92;
//        double temp = 59;
//        double dewpt = 59;
//        double palt = FlightLookupUS.pressureAltitude(alt,slp);
//        double press= NonStdAtmosphere.pressure(alt, slp, lat);
//        double dalt = FlightLookupUS.densityAltitude(alt,slp,temp,dewpt);
//        
//        System.out.format("pressure altitude\t = %7.1f\n", palt);
//        System.out.format("density altitude\t = %7.1f\n", dalt); // issue comes from vapor pressure calculation (2017-06-11)

// Heading, course, wind corrections
//        double[] hdggswca = new double[3];
//        double[] crsgswca = new double[3];
//        double windsp = 50;
//        double windir = 350;
//        double tas = 250;
//        double crs = 180;
//        double hdg = 180;
//        
//        hdggswca = FlightLookupUS.headingGSWCA(windsp,windir,tas,crs);
//        crsgswca = FlightLookupUS.courseGSWCA(windsp,windir,tas,hdg);
//        
//        System.out.format("headingGSWCA(%3.0f,%3.0f,%3.0f,%3.0f) = \n",windsp,windir,tas,crs);
//        System.out.format("\tHDG = \t%3.0f\n\tGS = \t%3.0f\n\tWCA = \t%3.0f\n",
//                hdggswca[0],hdggswca[1],hdggswca[2]);
//        
//        System.out.format("courseGSWCA(%3.0f,%3.0f,%3.0f,%3.0f) = \n",windsp,windir,tas,hdg);
//        System.out.format("\tCRS = \t%3.0f\n\tGS = \t%3.0f\n\tWCA = \t%3.0f\n",
//                crsgswca[0],crsgswca[1],crsgswca[2]);
// END Heading, course, wind corrections

        
        
        
        
//        double dir = 63;
//        double latitude = 45;
//        double[] wind = new double[2];
//        wind = FlightLookupUS.wind(200,150,200,20);
//        double windDir = wind[0];
//        double windSp = wind[1];
//        double xwind = FlightLookupUS.crossWindComponent(22.5, 30);
//        double hwind = FlightLookupUS.headWindComponent(25, 30);
//        System.out.format("Wind is: %3.0f%s at %5.1f knots\n",windDir,DEGREE,windSp);
//        System.out.format("Headwind component = %5.1f knots\nCrosswind component = %5.1f knots\n", hwind,xwind);
//        System.out.format("Reciprocal of %3.0f%s is %3.0f%s\n",dir,DEGREE,FlightLookupUS.reciprocal(dir),DEGREE);

//      Request input for altitude (in feet)
//        double alt_ft1 = -6000;
//        double alt_ft2 = 100000;
//        double increment = 1000;
//
//        
////      Convert altitude range
////        double[] altRange = new double[2]; 
////        altRange[0] = alt_ft1;
////        altRange[1] = alt_ft2;
//                
////      Print header
//        System.out.println(" alt\t\tsigma\tdelta\ttheta\ttemp\tpress\tdens\t\ta\tmu\t\tnu ");
//        System.out.println(" kft\t\t\t\t\tdegR\tpsi\tlbm/ft^3\t\tkts\tlbm/ft-s\t\tft^2/s");
//        String sep="----------------------------------------------------------------------"+
//                "-------------------------------------";
//        System.out.println(sep);
//
////      Initialize local variables
//        double sigma,delta,theta;
//        double t,p,rho,a;
//        double mu,nu;
//        
////      Calculate all values of interest
//        for (double alt=alt_ft1; alt<=alt_ft2; alt=alt+increment){
//            theta = AtmosphereLookupUS.temperatureRatio(alt,latitude);
//            delta = AtmosphereLookupUS.pressureRatio(alt,latitude);
//            sigma = AtmosphereLookupUS.densityRatio(alt,latitude);
//            t     = AtmosphereLookupUS.TZERO * theta;
//            p     = AtmosphereLookupUS.PZERO * delta;
//            rho   = AtmosphereLookupUS.RHOZERO * sigma;
//            a     = AtmosphereLookupUS.AZERO * Math.sqrt(theta)* AtmosphereLookupUS.FPS2KTS;
//            mu    = AtmosphereLookupUS.dynamicViscosity(alt,latitude);
//            nu    = mu/rho;
//
//           
////          Print values of interest
//            System.out.format("%8.1f\t%4.3f\t%4.3f\t%4.3f\t%5.2f\t%5.2f\t%5.3e\t"+
//                    "%5.1f\t%5.2e\t%5.2e%n",alt,sigma,delta,theta,t,p,rho,a,mu,nu);
//
//        }

        
    }
    
}