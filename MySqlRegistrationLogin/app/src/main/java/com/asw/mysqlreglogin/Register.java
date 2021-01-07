package com.asw.mysqlreglogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Register extends AppCompatActivity {

    EditText mobileEditText, nameEditText, stateEditText, cityEditText, pincodeEditText, passwordEditText;
    Button registerButton;
    TextView loginButton;
    SqlServerConnectionHelper dbHelper;
    private boolean success = false;
    SharedPreference session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new SqlServerConnectionHelper(this);
        session = new SharedPreference(this);

        if (!isNetworkAvailable()) {

            AlertDialog.Builder a_builder1 = new AlertDialog.Builder(Register.this);
            a_builder1.setMessage("Check your internet connection and try again.")
                    .setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), Register.class);
                            startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                Register.this.finishAffinity();
                            }

                        }
                    });

            AlertDialog alert = a_builder1.create();
            alert.setTitle("Demo");
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setTitle("Title");
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            lp.copyFrom(alert.getWindow().getAttributes());
            lp.width = 40;
            lp.height = 40;
            lp.x = -170;
            lp.y = 40;
            alert.getWindow().setAttributes(lp);
            alert.show();
        } else {

            mobileEditText = findViewById(R.id.reg_mob);
            nameEditText = findViewById(R.id.reg_name);
            stateEditText = findViewById(R.id.reg_state);
            cityEditText = findViewById(R.id.reg_city);
            pincodeEditText = findViewById(R.id.reg_pincode);
            passwordEditText = findViewById(R.id.reg_pass);
            registerButton = findViewById(R.id.reg_btn);
            loginButton = findViewById(R.id.reg_log_btn);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mob = mobileEditText.getText().toString().trim();
                    String nam = nameEditText.getText().toString().trim();
                    String stat = stateEditText.getText().toString().trim();
                    String cit = cityEditText.getText().toString().trim();
                    String pin = pincodeEditText.getText().toString().trim();
                    String pas = passwordEditText.getText().toString().trim();

                    if (mob.isEmpty() || mob.length() < 9 ) {
                        mobileEditText.setError("Enter Mobile number.");
                        mobileEditText.requestFocus();
                    } else if (nam.isEmpty()) {
                        nameEditText.setError("Enter Name.");
                        nameEditText.requestFocus();
                    } else if (stat.isEmpty()) {
                        stateEditText.setError("Enter Stete.");
                        stateEditText.requestFocus();
                    } else if (cit.isEmpty()) {
                        cityEditText.setError("Enter City.");
                        cityEditText.requestFocus();
                    } else if (pin.isEmpty()) {
                        pincodeEditText.setError("Enter PinCode.");
                        pincodeEditText.requestFocus();
                    } else if (pas.isEmpty()) {
                        passwordEditText.setError("Enter Password.");
                        passwordEditText.requestFocus();
                    }else {
                        register(mob, nam, stat, cit, pin, pas);
                    }
                }
            });
        }
    }

    public void register(String mo, String na, String st, String ci, String pi, String pa ){
        try {
            Connection conn = dbHelper.connectionclasss();
            if (conn == null) {
                success = false;
            }else {

                PreparedStatement stmt = conn.prepareStatement("EXEC SP_REGISTRATION @MODE='INSERT_REGISTRATION',@MOBILE_NO='" + mo + "',@NAME='" + na + "',@STATES='" + st + "',@CITY='" + ci + "',@PIN_CODE=" + pi + ",@MPWD='" + pa + "'select 1");
                ResultSet rs = stmt.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        try {
                            Toast.makeText(getApplicationContext(), "Data Insert Successfully.!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    success = true;

                } else {
                    success = false;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            success = false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}