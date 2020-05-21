package zzleep.etl;

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
                        .getConnection("Jdbc:postgresql://ec2-176-34-97-213.eu-west-1.compute.amazonaws.com:5432/d2ka0f2unsn83u",
                                "oyggsjqtgcdqlh", "c0b9d98e5e08a21f7c5915443f633865809943e6a1132ffcaa2454d1990ba6b6");
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
