package imie.angers.fr.beaconstoreproject.utils;

import android.database.DatabaseUtils;

/**
 * Created by Anne on 29/02/2016.
 */
public class DbUtil {

    /**
     * This method acts as an alias for the sqlEscapeString(str) method in
     * DatabaseUtils.
     *
     * @param str
     * @return
     */
    public static String sant(String str) {
        return DatabaseUtils.sqlEscapeString(str);
    }
}
