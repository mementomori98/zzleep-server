package zzleep.etl;

import java.sql.SQLException;
import java.util.Calendar;

public class Etl implements Runnable {

    @Override
    public void run() {
        int year, month, day, hour, minute, second;
        String queryValues, tableName = "zzleep.etl.Etl";
        Jdbc connection = Jdbc.getInstance();
        while(true)
        {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR);
            minute = cal.get(Calendar.MINUTE);
            second = cal.get(Calendar.SECOND);

            queryValues = "'" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "'";

            try
            {
                connection.insert(tableName, queryValues);
                System.out.println("posted");
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }

            try {
                Thread.sleep(43200000);
            }
            catch (InterruptedException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
