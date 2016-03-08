package imie.angers.fr.beaconstoreproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Ninde on 07/03/2016.
 */
public class DateConvertor {

    /*************************************************************************************************
     * CONVERTIE LE FORMAT YYYY-MM-DD EN DD/MM/YYYY
     *************************************************************************************************/

    public static String dateConvertor(String date) {

        SimpleDateFormat banniereFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd");

        String newDate = null;
        try {

            newDate = banniereFormat.format(sqliteFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }

    public static String transformeMonth(int month){

        String newMonth;

        newMonth = month < 9 ? "0"+String.valueOf(month + 1) : String.valueOf(month + 1);

        return newMonth;
    }

    public static String transformeDay(int day){

        String newDay;

        newDay = day < 9 ? "0"+String.valueOf(day) : String.valueOf(day);

        return newDay;
    }
}

