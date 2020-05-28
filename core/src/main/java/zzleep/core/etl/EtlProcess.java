package zzleep.core.etl;

import java.sql.SQLException;
import java.util.Calendar;

public class EtlProcess implements Runnable {

    @Override
    public void run() {
        int year, month, day, hour, minute, second;
        String queryValues, tableName = "etl";
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
                System.out.println(" -- Triggering ETL");
                connection.insert(tableName, queryValues);
                System.out.println(" -- ETL Triggered");
            }
            catch (SQLException e)
            {
                System.out.println(" -- Failed to trigger ETL");
                e.printStackTrace();
            }

            try {
                Thread.sleep(30000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
