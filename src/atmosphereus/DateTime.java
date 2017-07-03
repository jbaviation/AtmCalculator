package atmosphereus;
import java.util.Calendar;
import java.text.SimpleDateFormat;
// All methods below are checked out (2017-06-10)
//  - Added dayLookup (2017-07-03)

public class DateTime {
    
//***************************************************************************************//
/* JULIAN DAYS                                                                           */
/* Convert between Julian day and Gregorian date                                         */
//***************************************************************************************//
    
//  Convert date (MM-DD), leap year ("yes" or "no"), and time [optional] (HH:MM:SS)
//  to Julian Day
    public static double dateTime2JulianDay(String date, String leapYear, String... militaryTime){
        // Check if leap year (yes/no)
        int leapIO;
        if (leapYear.toLowerCase().contains("y"))
            leapIO = 1;
        else // if leapYear is "no" or anything else
            leapIO = 0;
        // leapIO = 1 for leap year
        //        = 0 for no leap year
        
        // Check that date is in the correct format (MM-DD)
        boolean checkDate = date.contains("/") || date.contains("-");
        if (!checkDate)
            System.err.println("Please enter date as MM-DD");
        
        // Check if time exists and is in the correct format (HH:MM:SS)
        String time;
        if (militaryTime.length >= 1){
            time = militaryTime[0];
        }
        else
            time = "00:00:00";
        boolean checkTime = time.contains(":");
        if (!checkTime)
            System.err.println("Please enter time as HH:MM:SS");
        
        // Prep to parse out the month and day
        String dateNew = date;   // define new date variable to modify
        dateNew = dateNew.replace("/","-"); // convert any slashes to dashes for consistency
        
        int locDash = dateNew.indexOf("-"); // find location of dash (defaults to first dash)
        String monthStr = dateNew.substring(0,locDash); // list month as a string
        String dayStr = dateNew.substring(locDash+1); // list day as a string
        
        if (monthStr.length() > 2 || dayStr.length() > 2) // check for correct number of digits
            System.err.println("Please enter date as MM-DD");
        
        // Parse month
        if (monthStr.substring(0,1).equals("0"))// if leading digit is a zero ignore it
            monthStr = monthStr.substring(1,2);
        int month = Integer.parseInt(monthStr); // month as an integer
        if (month < 1 || month > 12) // check if month is real
            System.err.println(month + " is not a real month");
        
        // Parse day
        if (dayStr.substring(0,1).equals("0"))
            dayStr = dayStr.substring(1,2);
        int day = Integer.parseInt(dayStr);
        // Check that number of days is correct for the provided month and put name to month
        String nameMonth = monthName(month,day);
        int julianDay = julDay(month,day,leapIO);
        
        // Setup error message
        String errorMsg = "Please enter a time between 00:00:00 and 23:59:59";
        
        // Prep to parse out the hours
        String timeNew = time;
        if (!timeNew.contains(":"))
            System.err.println(errorMsg);
        int locColon = time.indexOf(":");
        String hourStr = timeNew.substring(0,locColon);
        if (hourStr.length() > 2)
            System.err.println(errorMsg);
        timeNew = timeNew.substring(locColon+1);  // substring after the hours
        
        // Prep to parse out the minutes
        if (!timeNew.contains(":"))
            System.err.println(errorMsg);
        locColon = timeNew.indexOf(":");
        String minStr = timeNew.substring(0,locColon);
        if (minStr.length() > 2)
            System.err.println(errorMsg);
        timeNew = timeNew.substring(locColon+1);  // substring after the minutes
        
        // Prep to parse out the seconds
        String secStr = timeNew;
        if (secStr.length() > 2)
            System.err.println(errorMsg);        
        
        // Convert time strings to integers
        if (hourStr.substring(0,1).equals("0"))// if leading digit is a zero ignore it
            hourStr = hourStr.substring(1,2);
        int hour = Integer.parseInt(hourStr); // hour as an integer
        
        if (minStr.substring(0,1).equals("0"))
            minStr = minStr.substring(1,2);
        int min = Integer.parseInt(minStr);
        
        if (secStr.substring(0,1).equals("0"))
            secStr = secStr.substring(1,2);
        int sec = Integer.parseInt(secStr);
        
        
        boolean hCheck = hour < 0 || hour > 23;
        boolean mCheck = min < 0  || min > 59;
        boolean sCheck = sec < 0  || sec > 59;
        if (hCheck || mCheck || sCheck) // check if time is real
            System.err.println(time + " is not a real time.");
        
        // Convert hours, minutes and seconds into a day decimal
        double dayDecimal = (hour + min/60.0 + sec/3600.0) / 24.0;
        
        // Combine Julian day with hours, minutes, and seconds
        double julianDayCombined = (double)julianDay + dayDecimal;
        
        return julianDayCombined;
   
    }

//  Lookup date and time from Julian Day and leap year
    public static int[] julianDay2DateTime(double julDay, int leapYear){
        // Define array of number of days in each month
        int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        int yearDays = 365;
        
        // Change array if leap year
        if (leapYear == 1){
            monthDays[1] = 29;
            yearDays = 366;
        }
        
        // Round down the day 
        int julianDay = (int) julDay;
        double dayFraction = julDay - (double)julianDay;
        
        // Determine time in hours, minutes, and seconds
        double timeTracker = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        while (timeTracker < dayFraction){
            timeTracker = timeTracker + 1.0/86400.0;
            sec++;
            if (sec >= 60){
                min++;
                sec = 0;
            }
            if (min >= 60){
                hour++;
                min = 0;
            }
            if (hour >= 24){
                throw new IllegalArgumentException("Time cannot exceed 23:59:59");
            }
        }
        
        // Check if Julian Day is greater than the year length or less than 1
        if (julianDay > yearDays || julianDay <= 0){
            String msg = "Julian day must be between 1 and " + yearDays + "\n";
            throw new IllegalArgumentException(msg);
        }
        
        // Determine month and day
        int month = 1;
        int day = 1;
        int julDayTracker = 1;
        while (julDayTracker < julianDay){
            julDayTracker++;
            day++;
            if (day > monthDays[month-1]){
                day = 1;
                month++;
            }
        }

        // Set output array with month, day, hour, min, sec
        int[] output = {month,day,hour,min,sec};
        return output;

    }

//***************************************************************************************//
/* DAY/TIME REFERENCE METHODS                                                            */
/* Helper methods to convert between Julian day and Gregorian date                       */
//***************************************************************************************//
    
//  Calculate Julian Day
    public static int julDay(int month, int day, int leapYear){
        // Define array of number of days in each month
        int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        
        // Change array if leap year
        if (leapYear == 1)
            monthDays[1] = 29;
        
        int mDays = 0;
        for (int i=0; i<month-1; i++){
            mDays = mDays + monthDays[i];
        }
        int julianDay = mDays + day;
        
        return julianDay;
    }
    
  
//  Name the month and check the day
    public static String monthName(int month, int... day){
        String mName = "";
        int mDays = 0;
        if (month == 1){
            mName = "January";
            mDays = 31;
        }
        else if (month == 2){
            mName = "February";
            mDays = 29; 
        }
        else if (month == 3){
            mName = "March";
            mDays = 31;            
        }
        else if (month == 4){
            mName = "April";
            mDays = 30;            
        } 
        else if (month == 5){
            mName = "May";
            mDays = 31;            
        }
        else if (month == 6){
            mName = "June";
            mDays = 30;           
        }
        else if (month == 7){
            mName = "July";
            mDays = 31;            
        }
        else if (month == 8){
            mName = "August";
            mDays = 31;            
        }
        else if (month == 9){
            mName = "September";
            mDays = 30;            
        }
        else if (month == 10){
            mName = "October";
            mDays = 31;            
        }
        else if (month == 11){
            mName = "November";
            mDays = 30;            
        }
        else if (month == 12){
            mName = "December";
            mDays = 31;           
        }
        else{
            System.err.print(month + " is not a recognizable month.");
            mName = "Unknown";
            mDays = 31;            
        }
        
       // Check day if it exists and makes sense
       int dayVal;
       if (day.length >= 1){  // use first element if it is defined
           if (day[0] > mDays){
               System.err.print("You entered "+mName+" "+day[0]+". The max number of"
                       + " days in "+mName+" is "+mDays);
           }
       }
       // else day was not provided an don't check if it makes sense
       
       return mName;
    }

//  Lookup current year, month, day, and time
    public int[] dayLookup(){
        String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").
                format(Calendar.getInstance().getTime());
        
        String currentYearStr = currentTime.substring(0,4);
        String currentMonthStr= currentTime.substring(4,6);
        String currentDayStr  = currentTime.substring(6,8);
        String currentHourStr = currentTime.substring(9,11);
        String currentMinStr  = currentTime.substring(11,13);
        String currentSecStr  = currentTime.substring(13,15);
        
        int currentYear = Integer.parseInt(currentYearStr);
        int currentMonth= Integer.parseInt(currentMonthStr);
        int currentDay  = Integer.parseInt(currentDayStr);
        int currentHour = Integer.parseInt(currentHourStr);
        int currentMin  = Integer.parseInt(currentMinStr);
        int currentSec  = Integer.parseInt(currentSecStr);
        
        int[] year_month_day_hour_min_sec = new int[6];
        year_month_day_hour_min_sec[0] = currentYear;
        year_month_day_hour_min_sec[1] = currentMonth;
        year_month_day_hour_min_sec[2] = currentDay;
        year_month_day_hour_min_sec[3] = currentHour;
        year_month_day_hour_min_sec[4] = currentMin;
        year_month_day_hour_min_sec[5] = currentSec;
        
        return year_month_day_hour_min_sec;
        
    }


}
