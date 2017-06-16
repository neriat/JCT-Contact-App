package neria.tzidkani.jct_contact;

import android.widget.Toast;

/**
 * Created by Neria Tzidkani on 13/03/2017.
 */

public final class ShowText {

    public static String be_specific ="חפש משהו יותר ספציפי";
    public static String no_result ="החיפוש לא הניב תוצאות";
    public static String author =            "This program was developed by Neria Tzidkani";
    public static String authorHEB ="פותח ע\"י נריה צדקני";
    public static String copied_lecturer(String name){
        return  "כתובת המייל של המרצה " + name + " הועתקה בהצלחה";
    }
    public static String initializationCalculationTime (long msTime){
        return "start after " + msTime + "ms";
    }
    public static String CalculationQueryTime(long msTime){
        return "Query took " + msTime + "ms";
    }


    public static String update = "Please update app, this one is too old";
    public static String GitHub =    "https://github.com/neriat/";
    public static String attempt_email =    "Attempts to send an E-mail";
    public static String attempts_browser=    "Launching Browser";
}
