package com.asw.mysqlreglogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerConnectionHelper {
    Context context;
    String ip, db, DBUserNameStr, DBPasswordStr;

    public SqlServerConnectionHelper(Context context) {
        this.context = context;
    }

    //----------------------------------SQL server connection------------------------------------

    @SuppressLint("NewApi")
    public Connection connectionclasss() {

        ip = "00.000.000.000"; //Add your DB IP Address
        db = "TEST_DB"; // DB Name
        DBUserNameStr = "TEST_DB"; // DB Name
        DBPasswordStr = "7h!4j1xV"; // DB Password

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";user=" + DBUserNameStr + ";password=" + DBPasswordStr + ";";

            connection = DriverManager.getConnection(ConnectionURL);
        } catch (java.sql.SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
