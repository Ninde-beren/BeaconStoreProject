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
}

