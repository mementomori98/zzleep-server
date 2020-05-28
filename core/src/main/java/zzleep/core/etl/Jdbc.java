package zzleep.core.etl;

import zzleep.core.settings.DataSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc {
        private Connection c;
        private Statement st;
        private static Jdbc instance;

        private Jdbc() {
            c = null;
            try {
                Class.forName("org.postgresql.Driver");

                c = DriverManager
                        .getConnection(DataSettings.getUrl(),
                                DataSettings.getUser(), DataSettings.getPassword());
            }
            catch (SQLException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        public static Jdbc getInstance()
        {
            if(instance == null)
                instance = new Jdbc();
            return instance;
        }

        public void insert(String tableName, String values) throws SQLException
        {
            String com = "insert into dataModels." + tableName + " values (" + values + ");";
            st = c.createStatement();
            st.executeUpdate(com);

        }
}
