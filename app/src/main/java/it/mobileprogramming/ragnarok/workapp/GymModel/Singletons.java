package it.mobileprogramming.ragnarok.workapp.GymModel;


import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by paride on 18/06/15.
 * class with singletons and static utility methods
 */
public class Singletons {
    public static User currentUser;
    public static final String customString =   "custom";

    /**
     * methods that returns a Date from string in format dd/MM/yyyy
     * @param date date to be converted
     * @return converted date
     */

    public static Date formatFromString(String date){
        java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        Date result;
        try {
            result =  df.parse(date);
        }
        catch (java.text.ParseException e){
            result  =   new Date(); //if wrong return 1/1/1970
        }
        return result;
    }

    /**
     * method returns a String in format dd/MM/yyyy from a date
     * @param aDate date to be converted
     * @return converted date
     */
    public static String getStringFromDate(Date aDate){
        java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy",Locale.ITALY);
        return  df.format(aDate);
    }

    public static int getIntFromBoolean(boolean value){
        if (value==true){
            return 1;
        }
        else return 0;
    }
    public static boolean getBooleanFromInt(int value){
        if (value==1){
            return true;
        }
        else return false;
    }

    public static Date randomDate() {
        int year = randBetween(1800,2100); // generate a year between 1900 and 2010;
        int dayOfYear = randBetween(1,364);// generate a number between 1 and 365 (or 366 if you need to handle leap year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        Date randomDoB = calendar.getTime();
        return randomDoB;
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static boolean checkIfDateInArrayList(Calendar myDate,ArrayList<Calendar>dates){

        boolean result = false;

        for(int i=0;i<dates.size();i++){

            Log.d("CONFRONTO1: ", myDate.toString() + "    CON     " + dates.get(i));

            if (dates.get(i) == null)
                continue;
            if(!dates.get(i).after(myDate) && !dates.get(i).before(myDate))
                result = true;
        }

        return result;
    }

    public static boolean checkIfDateInArray(Calendar myDate,Calendar[] dates, int size){

        boolean result = false;

        for(int i=0;i<size;i++){
            if (dates[i] == null)
                continue;

            Log.d("CONFRONTO2: ", myDate.toString() + "    CON     " + dates[i]);

            if(!dates[i].after(myDate) && !dates[i].before(myDate))
                result = true;
        }

        return result;
    }
}
