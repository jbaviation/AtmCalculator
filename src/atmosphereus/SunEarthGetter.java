package atmosphereus;
import java.io.*;
import java.util.Scanner;

// TEST THIS ENTIRE ROUTINE!!!!!!!

public class SunEarthGetter {
//  Declare Class variables available for calls
    public int APHELION_MO;
    public int APHELION_DY;
    public int APHELION_HR;
    public int APHELION_MN;
    public double APHELION_DIS;
    public int PERIHELION_MO;
    public int PERIHELION_DY;
    public int PERIHELION_HR;
    public int PERIHELION_MN;
    public double PERIHELION_DIS;
    public double PERIHELION_INTVL;
    
    public int YEAR;  //Current year
    public int MONTH; //Current month
    public int DAY;   //Current day
    public int HOUR;  //Current hour
    public int MIN;   //Current minute

//  Class variables for which line to use
    public boolean GOPREVLINE = false;
    public boolean GONEXTLINE = false;

//****************************************************************************
//  Static methods to pass on the class variables
//****************************************************************************
    public static double aphelionJulian() throws FileNotFoundException, IOException{
        SunEarthGetter yr = new SunEarthGetter();
        yr.lineReader();
        String ly = "no";
        if (yr.YEAR % 4 == 0)
            ly = "yes";
        String mo = Integer.toString(aphelionMonth());
        String dy = Integer.toString(aphelionDay());
        String hr = Integer.toString(aphelionHour());
        String mn = Integer.toString(aphelionMinute());
        String date = mo + "-" + dy;
        String miltime = hr + ":" + mn + ":00";
        double julDay = DateTime.dateTime2JulianDay(date, ly, miltime);
        return julDay;
    }
    public static int aphelionMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_MO;
        return aphelionday;
    }
    public static int aphelionDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_DY;
        return aphelionday;
    }
    public static int aphelionHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_HR;
        return aphelionday;
    }
    public static int aphelionMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_MN;
        return aphelionday;
    }
    public static double perihelionJulian() throws FileNotFoundException, IOException{
        SunEarthGetter yr = new SunEarthGetter();
        yr.lineReader();
        String ly = "no";
        if (yr.YEAR % 4 == 0)
            ly = "yes";
        String mo = Integer.toString(perihelionMonth());
        String dy = Integer.toString(perihelionDay());
        String hr = Integer.toString(perihelionHour());
        String mn = Integer.toString(perihelionMinute());
        String date = mo + "-" + dy;
        String miltime = hr + ":" + mn + ":00";
        double julDay = DateTime.dateTime2JulianDay(date, ly, miltime);
        return julDay;
    }
    public static int perihelionMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_MO;
        return perihelionday;
    }
    public static int perihelionDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_DY;
        return perihelionday;
    }
    public static int perihelionHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_HR;
        return perihelionday;
    }
    public static int perihelionMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_MN;
        return perihelionday;
    }
    public static double[] aphelionPerihelionDistance() throws FileNotFoundException, IOException{
        SunEarthGetter apd = new SunEarthGetter();
        apd.lineReader();
        double[] dis = new double[2];
        dis[0] = apd.APHELION_DIS;
        dis[1] = apd.PERIHELION_DIS;
        return dis;
    }
    public static double perihelionInterval() throws FileNotFoundException, IOException{
        SunEarthGetter pi = new SunEarthGetter();
        pi.lineReader();
        return pi.PERIHELION_INTVL;
    }
//****************************************************************************
    
//  Method for reading the lines
    public void lineReader() throws FileNotFoundException, IOException{
        
     // Start reading the EarthSunTime.txt file
        FileReader fi = new FileReader("src/atmosphereus/EarthSunTime.txt");

     // Start the buffered reader for reading line by line
        BufferedReader br = new BufferedReader(fi);
        Scanner fr = new Scanner(br);

     // Start while loop that looks for the next line
        boolean forcequit = false;
        String lineText = "";     // Current line
        String prevLine = "";     // Previous line
        while (fr.hasNextLine() && !forcequit){
            if (GONEXTLINE){
                lineText = fr.nextLine();
                analyzeLine(lineText);
                break;
            }
                
            lineText = fr.nextLine();
            analyzeLine(lineText);

            if (GOPREVLINE)
                analyzeLine(prevLine);            
            prevLine = lineText;  // This line must be the last in this while loop
        }
        

    }
    
//  Find the year and line in reference
    private void analyzeLine(String lineText){
     // Get current date and time
        DateTime dt = new DateTime();
        int[] dta = new int[5];
        dta = dt.dayLookup();
        YEAR  = dta[0];
        MONTH = dta[1];
        DAY   = dta[2];
        HOUR  = dta[3];
        MIN   = dta[4];
        String yr = Integer.toString(dta[0]);
        String mo = Integer.toString(dta[1]);
        String dy = Integer.toString(dta[2]);
        
        
     // Look for current year
        boolean yearCheck = lineText.substring(0,4).equals(yr);
        if (yearCheck){
            
            dataSearch(lineText);
            
            // Make sure we got the correct data for perihelion
            if (dta[1] > 7)
                GONEXTLINE = true; // do not use current line's perihelion data
            else if (dta[1] == 7 && dta[2] > APHELION_DY)  
                GONEXTLINE = true; // do not use current line's perihelion data
            
            // Make sure we got the correct data for aphelion
            if (dta[1] == 1 && dta[2] < PERIHELION_DY)
                GOPREVLINE = true; // do not use current line's aphelion data
            
        }
        else if (GONEXTLINE){  // change aphelion data
            dataSearch(lineText);
            GONEXTLINE = false;
        }
        else if (GOPREVLINE){  // change perihelion data
            dataSearch(lineText);
            GOPREVLINE = false;
        }
    }
    
    private void dataSearch(String lineText){
        // First guess at month, day and time of aphelion
        String a_mo = "7";
        String a_dy = lineText.substring(58,60);
        String a_hr = lineText.substring(62,64);
        String a_mn = lineText.substring(65,67);
        String a_dis= lineText.substring(70,79);
        
        a_dy =  a_dy.replace(" ","");
        a_hr =  a_hr.replace(" ","");
        a_mn =  a_mn.replace(" ","");
        a_dis= a_dis.replace(" ","");
        
        // First guess at month, day and time of perihelion
        String p_mo = "1";
        String p_dy = lineText.substring(13,15);
        String p_hr = lineText.substring(17,19);
        String p_mn = lineText.substring(21,23);
        String p_dis= lineText.substring(25,34);
        String p_int= lineText.substring(98,104);
        
        p_dy =  p_dy.replace(" ","");
        p_hr =  p_hr.replace(" ","");
        p_mn =  p_mn.replace(" ","");
        p_dis= p_dis.replace(" ","");
        p_int= p_int.replace(" ","");
        
        // Adjust month, day, and time if leading digit is zero
        boolean ady_check = a_dy.substring(0,1).equals("0");
        boolean ahr_check = a_hr.substring(0,1).equals("0");
        boolean amn_check = a_mn.substring(0,1).equals("0");
        boolean pdy_check = p_dy.substring(0,1).equals("0");
        boolean phr_check = p_hr.substring(0,1).equals("0");
        boolean pmn_check = p_mn.substring(0,1).equals("0");
        
        if (ady_check)
            a_dy = a_dy.substring(1);
        if (ahr_check)
            a_hr = a_hr.substring(1);
        if (amn_check)
            a_mn = a_mn.substring(1);
        if (pdy_check)
            p_dy = p_dy.substring(1);
        if (phr_check)
            p_hr = p_hr.substring(1);
        if (pmn_check)
            p_mn = p_mn.substring(1);
        
        
        // Convert aphelion and perihelion data to numbers
        if (GONEXTLINE || GOPREVLINE){
            if (GOPREVLINE){
                APHELION_MO = Integer.parseInt(a_mo);
                APHELION_DY = Integer.parseInt(a_dy);
                APHELION_HR = Integer.parseInt(a_hr);
                APHELION_MN = Integer.parseInt(a_mn);
                APHELION_DIS= Double.parseDouble(a_dis);           
            }
            if (GONEXTLINE){
                PERIHELION_MO = Integer.parseInt(p_mo);
                PERIHELION_DY = Integer.parseInt(p_dy);
                PERIHELION_HR = Integer.parseInt(p_hr);
                PERIHELION_MN = Integer.parseInt(p_mn);
                PERIHELION_DIS= Double.parseDouble(p_dis);
                PERIHELION_INTVL=Double.parseDouble(p_int);            
            }
        }
        else{
            APHELION_MO = Integer.parseInt(a_mo);
            APHELION_DY = Integer.parseInt(a_dy);
            APHELION_HR = Integer.parseInt(a_hr);
            APHELION_MN = Integer.parseInt(a_mn);
            APHELION_DIS= Double.parseDouble(a_dis);
            PERIHELION_MO = Integer.parseInt(p_mo);
            PERIHELION_DY = Integer.parseInt(p_dy);
            PERIHELION_HR = Integer.parseInt(p_hr);
            PERIHELION_MN = Integer.parseInt(p_mn);
            PERIHELION_DIS= Double.parseDouble(p_dis);
            PERIHELION_INTVL=Double.parseDouble(p_int);
        }
    }
}
