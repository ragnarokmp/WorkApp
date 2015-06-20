package it.mobileprogramming.ragnarok.workapp.GymModel;

import android.net.ParseException;

import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by paride on 18/06/15.
 */
public class Singletons {
    public static User currentUser;
    public static Date formatFromString(String date){
        java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
        Date result =   null;
        try {
            result =  df.parse(date);
        }
        catch (java.text.ParseException e){
            result  =   new Date(); //if wrong return 1/1/1970
        }
        return result;
    }
    public static String getStringFromDate(Date aDate){
        java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return  df.format(aDate);
    }
}
