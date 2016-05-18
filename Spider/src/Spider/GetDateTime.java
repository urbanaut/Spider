package Spider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bill.witt on 5/18/2016.
 */
class GetDateTime {

    static String currentDateTime()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return dateFormat.format(date);
    }
}
