package atmosphereus;
public class SunMoonPosition extends AtmosphereLookupUS {
    
/******************************************************************************/    
/*  Calculate lunar information (DOES NOT WORK 7/24/17)                       */
/******************************************************************************/

//  Calculate phase of the moon (0=new moon, 15=full moon)   
    public static double moonPhaseDay(double julianDay){
	double thisJD = julianDay;
	double degToRad = Math.PI / 180;
	double K0, T, T2, T3, J0, F0, M0, M1, B1;
	K0 = Math.floor((PRESENTYEAR-1900)*12.3685);
	T = (PRESENTYEAR-1899.5) / 100;
	T2 = T*T; T3 = T*T*T;
	J0 = 2415020 + 29*K0;
	F0 = 0.0001178*T2 - 0.000000155*T3 + (0.75933 + 0.53058868*K0) - (0.000837*T + 0.000335*T2);
	M0 = 360*(GetFrac(K0*0.08084821133)) + 359.2242 - 0.0000333*T2 - 0.00000347*T3;
	M1 = 360*(GetFrac(K0*0.07171366128)) + 306.0253 + 0.0107306*T2 + 0.00001236*T3;
	B1 = 360*(GetFrac(K0*0.08519585128)) + 21.2964 - (0.0016528*T2) - (0.00000239*T3);
	double phase = 0;
	double jday = 0;
        double oldJ = 0;
	while (jday < thisJD) {
		double F = F0 + 1.530588*phase;
		double M5 = (M0 + phase*29.10535608)*degToRad;
		double M6 = (M1 + phase*385.81691806)*degToRad;
		double B6 = (B1 + phase*390.67050646)*degToRad;
		F -= 0.4068*Math.sin(M6) + (0.1734 - 0.000393*T)*Math.sin(M5);
		F += 0.0161*Math.sin(2*M6) + 0.0104*Math.sin(2*B6);
		F -= 0.0074*Math.sin(M5 - M6) - 0.0051*Math.sin(M5 + M6);
		F += 0.0021*Math.sin(2*M5) + 0.0010*Math.sin(2*B6-M6);
		F += 0.5 / 1440; 
		oldJ=jday;
		jday = J0 + 28*phase + Math.floor(F); 
		phase++;
	}
	return (thisJD-oldJ)%30;
    }

//  Interpret moon phase
    public static String moonPhase(double julianDay){
        String output = "";
        
        // Calculate moon phase day
        double mPhase = julianDay;

        // Calculate max and min for given Julian Day
        double mPhase1 = moonPhaseDay(Math.floor(julianDay));
        double mPhase2 = moonPhaseDay(Math.floor(julianDay) + 0.9999);
        
        boolean newMoon = (mPhase2 >= 0 && mPhase2 < 3) && (mPhase1 > 28);
        boolean waxingCrescent = (mPhase2 < 7.5) && (mPhase1 > 0);
        boolean firstQuarter = (mPhase2 >= 7.5) && (mPhase1 <= 7.5);
        boolean waxingGibbous = (mPhase2 < 15) && (mPhase1 > 7.5);
        boolean fullMoon = (mPhase2 >= 15) && (mPhase1 <= 15);
        boolean waningGibbous = (mPhase2 < 22.5) && (mPhase1 > 15);
        boolean lastQuarter = (mPhase2 >= 22.5) && (mPhase1 <= 22.5);
        boolean waningCrescent = (mPhase2 > 22.5) && (mPhase1 > 22.5);
        
        if (newMoon)
            output = "New Moon";
        else if (waxingCrescent)
            output = "Waxing Crescent";
        else if (firstQuarter)
            output = "First Quarter";
        else if (waxingGibbous)
            output = "Waxing Gibbous";
        else if (fullMoon)
            output = "Full Moon";
        else if (waningGibbous)
            output = "Waning Gibbous";
        else if (lastQuarter)
            output = "Last Quarter";
        else if (waningCrescent)
            output = "Waning Crescent";
        else
            output = mPhase+" does not exist for the given month.";
        
        return output;
        
    }
    
//  Reference function from within moonPhase method
    public static double GetFrac(double fr) {return (fr - Math.floor(fr));}

/******************************************************************************/    
/*  Calculate solar information                                               */
/******************************************************************************/

//  Solar Declination Angle (degrees) (Checked out 7/29/17)
    public static double solarDeclination(double julianDay){
        double delta;
        double jd = julianDay;
        double pi = Math.PI;
//        delta = 23.45*pi/180*Math.sin(2*pi*(284+julianDay)/36.25);
        
        double c1 = 0.39779;
        double c2 = 0.98565 * pi/180;
        double c3 = 1.914 * pi/180;
        double c4 = 0.98565 * pi/180;
        delta = -Math.asin(c1*Math.cos(c2*(jd+10) + c3*Math.sin(c4*(jd-2))));
        return delta * 180/pi;
    }
    
//  Solar Elevation Angle (degrees) (Does NOT Work)
    public static double solarElevation(double julianDay, double latitude, double longitude){
        double jd = julianDay;
        double se = 0;
        
        
        
        return se;
    }

// calculate the Equation of Time from 'day of year' (Does NOT Work)
    public static double equationnOfTime(double julianDay) {
        double pi = Math.atan(1.0)*4;       // tan(pi/4) = 1 (45 degrees)
        double lambda = 23.44 * pi / 180;   // Earth's inclination in radians
        double omega = 2*pi/PEARTH;         // Angular velocity of annual revolution (radians/day)
        
        // the result is in seconds
        // add to the Mean Solar Time  (UTC + Longitude*240 sec.) to get Apparent Solar Time
        // D                                                          day of the year
        // A = W*(D+10)                                               angle of revolution (circular)
        // B = A + 0.0333*sin(W*(D-2))                                angle of revolution (elliptical)
        // C = ( A - atan[  tan(B) / cos (inclination) ] ) / 'pi'     angular correction
        // EOT = 43200 × (C − int(C+0.5))                             Equation of Time in seconds
        
        double delta = julianDay;
        double alpha = omega*((delta+10)%365.24);                              // angle in (mean) circular orbit, solar year starts 21. Dec
        double beta = alpha + 0.0333*Math.sin(omega*((delta-2)%365.24));            // angle in elliptical orbit, from perigee 3. Jan (radians)
        double gamma = (alpha - Math.atan(Math.tan(beta) / Math.cos(lambda)))/pi;      // angular correction
        double eot = 43200.*(gamma - (gamma+0.5));
        return eot;   // equation of time in seconds
    }
    
    
}
