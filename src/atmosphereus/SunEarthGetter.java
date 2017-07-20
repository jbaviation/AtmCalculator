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
    
    public int SUMMERSOLSTICE_MO;
    public int SUMMERSOLSTICE_DY;
    public int SUMMERSOLSTICE_HR;
    public int SUMMERSOLSTICE_MN;
    public int WINTERSOLSTICE_MO;
    public int WINTERSOLSTICE_DY;
    public int WINTERSOLSTICE_HR;
    public int WINTERSOLSTICE_MN;
    
    public int YEAR;  //Current year
    public int MONTH; //Current month
    public int DAY;   //Current day
    public int HOUR;  //Current hour
    public int MIN;   //Current minute

//  Class variables for which line to use
    public boolean GOPREVLINE = false;
    public boolean GONEXTLINE = false;
    public boolean GOPREVLINE2= false;
    public boolean GONEXTLINE2= false;

//****************************************************************************
//  Static methods to pass on the class variables
//****************************************************************************

    public static double summerJulian() throws FileNotFoundException, IOException{
        SunEarthGetter yr = new SunEarthGetter();
        SunEarthGetter.getPresentDay();
        yr.lineReader2();
        String ly = "no";
        if (yr.YEAR % 4 == 0)
            ly = "yes";
        String mo = Integer.toString(summerMonth());
        String dy = Integer.toString(summerDay());
        String hr = Integer.toString(summerHour());
        String mn = Integer.toString(summerMinute());
        String date = mo + "-" + dy;
        String miltime = hr + ":" + mn + ":00";
        double julDay = DateTime.dateTime2JulianDay(date, ly, miltime);
        return julDay;
    }
    public static double winterJulian() throws FileNotFoundException, IOException{
        SunEarthGetter yr = new SunEarthGetter();
        SunEarthGetter.getPresentDay();
        yr.lineReader2();
        String ly = "no";
        if (yr.YEAR % 4 == 0)
            ly = "yes";
        String mo = Integer.toString(winterMonth());
        String dy = Integer.toString(winterDay());
        String hr = Integer.toString(winterHour());
        String mn = Integer.toString(winterMinute());
        String date = mo + "-" + dy;
        String miltime = hr + ":" + mn + ":00";
        double julDay = DateTime.dateTime2JulianDay(date, ly, miltime);
        return julDay;
    }
    public static double aphelionJulian() throws FileNotFoundException, IOException{
        SunEarthGetter.getPresentDay();
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
    public static double perihelionJulian() throws FileNotFoundException, IOException{
        SunEarthGetter yr = new SunEarthGetter();
        SunEarthGetter.getPresentDay();
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
    public static double[] aphelionPerihelionDistance() throws FileNotFoundException, IOException{
        SunEarthGetter apd = new SunEarthGetter();
        SunEarthGetter.getPresentDay();
        apd.lineReader();
        double[] dis = new double[2];
        dis[0] = apd.APHELION_DIS;
        dis[1] = apd.PERIHELION_DIS;
        return dis;
    }
    public static double perihelionInterval() throws FileNotFoundException, IOException{
        SunEarthGetter pi = new SunEarthGetter();
        SunEarthGetter.getPresentDay();
        pi.lineReader();
        return pi.PERIHELION_INTVL;
    }
    
    static void getPresentDay(){
        SunEarthGetter gpd = new SunEarthGetter();
        gpd.presentDay();
    }
    
//****************************************************************************
// Private Static Getter Methods
//****************************************************************************
    
    private static int aphelionMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_MO;
        return aphelionday;
    }
    private static int aphelionDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_DY;
        return aphelionday;
    }
    private static int aphelionHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_HR;
        return aphelionday;
    }
    private static int aphelionMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int aphelionday = ap.APHELION_MN;
        return aphelionday;
    }
    private static int perihelionMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_MO;
        return perihelionday;
    }
    private static int perihelionDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_DY;
        return perihelionday;
    }
    private static int perihelionHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_HR;
        return perihelionday;
    }
    private static int perihelionMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader();
        int perihelionday = ap.PERIHELION_MN;
        return perihelionday;
    }
// Edited
    private static int summerMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.SUMMERSOLSTICE_MO;
        return summerday;
    }
    private static int summerDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.SUMMERSOLSTICE_DY;
        return summerday;
    }
    private static int summerHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.SUMMERSOLSTICE_HR;
        return summerday;
    }
    private static int summerMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.SUMMERSOLSTICE_MN;
        return summerday;
    }
    private static int winterMonth() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.WINTERSOLSTICE_MO;
        return summerday;
    }
    private static int winterDay() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.WINTERSOLSTICE_DY;
        return summerday;
    }
    private static int winterHour() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.WINTERSOLSTICE_HR;
        return summerday;
    }
    private static int winterMinute() throws FileNotFoundException, IOException{
        SunEarthGetter ap = new SunEarthGetter();
        ap.lineReader2();
        int summerday = ap.WINTERSOLSTICE_MN;
        return summerday;
    }
    
//****************************************************************************
// Read EarthSunTime.txt
//****************************************************************************
    public void presentDay(){
     // Get current date and time
        DateTime dt = new DateTime();
        int[] dta = new int[5];
        dta = dt.dayLookup();
        YEAR  = dta[0];
        MONTH = dta[1];
        DAY   = dta[2];
        HOUR  = dta[3];
        MIN   = dta[4];
    }

//  Method for reading the lines
    public void lineReader() throws FileNotFoundException, IOException{
//        SunEarthGetter gpd = new SunEarthGetter();
//        gpd.presentDay();
        
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
    
//****************************************************************************
// Read SolsticesEquinoxes.txt
//****************************************************************************
//  Method for reading the lines
    public void lineReader2() throws FileNotFoundException, IOException{
        
     // Start reading the SolstricesEquinoxes.txt file
        FileReader fi = new FileReader("src/atmosphereus/SolsticesEquinoxes.txt");

     // Start the buffered reader for reading line by line
        BufferedReader br = new BufferedReader(fi);
        Scanner fr = new Scanner(br);

     // Start while loop that looks for the next line
        boolean forcequit = false;
        String lineText = "";     // Current line
        String prevLine = "";     // Previous line
        while (fr.hasNextLine() && !forcequit){
            if (GONEXTLINE2){
                lineText = fr.nextLine();
                analyzeLine2(lineText);
                break;
            }
                
            lineText = fr.nextLine();
            analyzeLine2(lineText);

            if (GOPREVLINE2)
                analyzeLine2(prevLine);            
            prevLine = lineText;  // This line must be the last in this while loop
        }

    }
    
//  Find the year and line in reference
    private void analyzeLine2(String lineText){
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
        boolean yearCheck = lineText.substring(1,5).equals(yr);
        if (yearCheck){
            
            dataSearch2(lineText);
            
            // Make sure we got the correct data for solstice
            if (MONTH < 6)
                GOPREVLINE2 = true; // do not use current line's solstice data
            else if (MONTH == 6 && DAY < SUMMERSOLSTICE_DY)  
                GOPREVLINE2 = true; // do not use current line's solstice data
            
            // Make sure we got the correct data for solstice
            if (MONTH == 12 && DAY > WINTERSOLSTICE_DY)
                GONEXTLINE2 = true; // do not use current line's summer solstice data
            
        }
        else if (GONEXTLINE2){  // change summer solstice
            dataSearch2(lineText);
            GONEXTLINE2 = false;
        }
        else if (GOPREVLINE2){  // change winter solstice
            dataSearch2(lineText);
            GOPREVLINE2 = false;
        }
    }
    
    private void dataSearch2(String lineText){
        // First guess at month, day and time of summer solstice
        String ss_mo = "6";
        String ss_dy = lineText.substring(33,35);
        String ss_hr = lineText.substring(37,39);
        String ss_mn = lineText.substring(40,42);
        
        ss_dy =  ss_dy.replace(" ","");
        ss_hr =  ss_hr.replace(" ","");
        ss_mn =  ss_mn.replace(" ","");
        
        // First guess at month, day and time of winter solstice
        String ws_mo = "12";
        String ws_dy = lineText.substring(69,71);
        String ws_hr = lineText.substring(73,75);
        String ws_mn = lineText.substring(76,78);
        
        ws_dy =  ws_dy.replace(" ","");
        ws_hr =  ws_hr.replace(" ","");
        ws_mn =  ws_mn.replace(" ","");
        
        // Adjust month, day, and time if leading digit is zero
        boolean ssdy_check = ss_dy.substring(0,1).equals("0");
        boolean sshr_check = ss_hr.substring(0,1).equals("0");
        boolean ssmn_check = ss_mn.substring(0,1).equals("0");
        boolean wsdy_check = ws_dy.substring(0,1).equals("0");
        boolean wshr_check = ws_hr.substring(0,1).equals("0");
        boolean wsmn_check = ws_mn.substring(0,1).equals("0");
        
        if (ssdy_check)
            ss_dy = ss_dy.substring(1);
        if (sshr_check)
            ss_hr = ss_hr.substring(1);
        if (ssmn_check)
            ss_mn = ss_mn.substring(1);
        if (wsdy_check)
            ws_dy = ws_dy.substring(1);
        if (wshr_check)
            ws_hr = ws_hr.substring(1);
        if (wsmn_check)
            ws_mn = ws_mn.substring(1);
        
        
        // Convert string dates to numbers
        if (GONEXTLINE2 || GOPREVLINE2){
            if (GONEXTLINE2){
                SUMMERSOLSTICE_MO = Integer.parseInt(ss_mo);
                SUMMERSOLSTICE_DY = Integer.parseInt(ss_dy);
                SUMMERSOLSTICE_HR = Integer.parseInt(ss_hr);
                SUMMERSOLSTICE_MN = Integer.parseInt(ss_mn);
            }
            if (GOPREVLINE2){
                WINTERSOLSTICE_MO = Integer.parseInt(ws_mo);
                WINTERSOLSTICE_DY = Integer.parseInt(ws_dy);
                WINTERSOLSTICE_HR = Integer.parseInt(ws_hr);
                WINTERSOLSTICE_MN = Integer.parseInt(ws_mn);
            }
        }
        else{
            SUMMERSOLSTICE_MO = Integer.parseInt(ss_mo);
            SUMMERSOLSTICE_DY = Integer.parseInt(ss_dy);
            SUMMERSOLSTICE_HR = Integer.parseInt(ss_hr);
            SUMMERSOLSTICE_MN = Integer.parseInt(ss_mn);
            WINTERSOLSTICE_MO = Integer.parseInt(ws_mo);
            WINTERSOLSTICE_DY = Integer.parseInt(ws_dy);
            WINTERSOLSTICE_HR = Integer.parseInt(ws_hr);
            WINTERSOLSTICE_MN = Integer.parseInt(ws_mn);
        }
    }
}
