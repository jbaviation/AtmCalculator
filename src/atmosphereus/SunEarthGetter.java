package atmosphereus;
import java.util.Calendar;
import java.text.SimpleDateFormat;
public class SunEarthGetter {
    public static double[] dayLookup(){
        String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").
                format(Calendar.getInstance().getTime());
        
        String currentYearStr = currentTime.substring(0,4);
        String currentMonthStr= currentTime.substring(4,6);
        String currentDayStr  = currentTime.substring(6,8);
        
        int currentYear = Integer.parseInt(currentYearStr);
        int currentMonth= Integer.parseInt(currentMonthStr);
        int currentDay  = Integer.parseInt(currentDayStr);
        
        
        
    }
}
