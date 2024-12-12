package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public final class CustomFunctions {
    private CustomFunctions(){

    }
    public static Date convertDate(String birthdate)
    {

        if(!birthdate.matches("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$")){
            return null;
        }


        String birthday =birthdate ;
        SimpleDateFormat convertBirthdate = new SimpleDateFormat("dd-MM-yyyy");
        convertBirthdate.setLenient(false);
        try {
            return convertBirthdate.parse(birthday);
        } catch (ParseException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
    public static boolean sameDate(Date x1, Date x2){
        if(x1.getDate()==x2.getDate() && x1.getMonth()==x2.getMonth() && x1.getYear()==x2.getYear()){
            return true;
        }
        return false;
    }

    public static boolean sameMonth(Date x1, Date x2){
        if(x1.getMonth()==x2.getMonth() && x1.getYear()==x2.getYear()){
            return true;
        }
        return false;
    }
    public static boolean isInSetDaily(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (sameDate(x,d)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInSetMonthly(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (sameMonth(x,d)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInSetYearly(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (x.getYear()==d.getYear()) {
                return true;
            }
        }
        return false;
    }
}
