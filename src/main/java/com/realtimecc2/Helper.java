/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2;

import com.realtimecc2.model.Zeiten;
import java.sql.Date;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;


/**
 * helper class for calculations
 * @author amederake
 */
public class Helper 
{
    /**
     * count working days for given month
     * @param d date value
     * @return working days
     */
    public static int getWorkingDays(LocalDate d)
    {
       int days = 0;
       LocalDate a = LocalDate.of(d.getYear(), d.getMonthValue(), 1);
       Calendar start = Calendar.getInstance();
       Calendar end = Calendar.getInstance();
       start.setTime(Date.valueOf(a));
       end.setTime(Date.valueOf(a.plusDays(a.lengthOfMonth()-1)));
       
       while(!start.after(end))
       {
           int day = start.get(Calendar.DAY_OF_WEEK);
	    if (day != Calendar.SATURDAY || day != Calendar.SUNDAY)
	    {
		days++;
	    }
            start.add(Calendar.DAY_OF_MONTH, 1);
       }
       return days;
    }
    
   /**
    * get working days for given data span
    * @param start start date
    * @param end end date
    * @return working days
    */
    public static int getWDFrom(LocalDate start, LocalDate end)
   {
	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	from.setTime(Date.valueOf(start));
	to.setTime(Date.valueOf(end));
	int wd = 0;
	while (!from.after(to))
	{
	    int day = from.get(Calendar.DAY_OF_WEEK);
	    if (day != Calendar.SATURDAY || day != Calendar.SUNDAY)
	    {
		wd++;
	    }
	    from.add(Calendar.DAY_OF_MONTH, 1);
	}
	return wd;
   }
    
    /**
     * calculate working time of given timesheet
     * @param zt Timesheet
     * @return houres without breakfast and lunch
     */
    public static double getTimeForDay(Zeiten zt)
    {
        long minutes = 0;
        if (zt != null)
        {
            LocalTime von = zt.getKommen().toLocalTime();
            LocalTime bis = zt.getGehen().toLocalTime();
            
            minutes = ChronoUnit.MINUTES.between(von, bis);
            // subtract breakefast time
            if (von.isBefore(LocalTime.parse("10:00")) && bis.isAfter(LocalTime.parse("11:00")))
            {
                minutes -= 15;
            }
            // subtract lunch time
            if (von.isBefore(LocalTime.parse("11:00")) && bis.isAfter(LocalTime.parse("13:00")))
            {
                minutes -= 30;
            }
            
            return minutes / 60.0;
        }
        return minutes;
    }
    
}
