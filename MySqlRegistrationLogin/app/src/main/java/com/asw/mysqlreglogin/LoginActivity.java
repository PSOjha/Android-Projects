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

import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText mobEditText, passwordEditText;
    SqlServerConnectionHelper dbHelper;
    private boolean success = false;
    SharedPreference session;
    String fullname , mob ,state,city;
    TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new SqlServerConnectionHelper(this);
        session = new SharedPreference(this);

        if (!isNetworkAvailable()) {

            AlertDialog.Builder a_builder1 = new AlertDialog.Builder(LoginActivity.this);
            a_builder1.setMessage("Check your internet connection and try again.")
                    .setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                LoginActivity.this.finishAffinity();
                            }

                        }
                    });

            AlertDialog alert = a_builder1.create();
            alert.setTitle("Demo");
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

            mobEditText = findViewById(R.id.login_mob_et);
            passwordEditText = findViewById(R.id.password_et);
            loginButton = findViewById(R.id.login_btn);
            registerTextView = findViewById(R.id.log_reg_btn);

            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Register.class);
                    startActivity(intent);
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mob = mobEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (mob.isEmpty() || mob.length() < 9) {
                        mobEditText.setError("Enter Mobile no");
                        mobEditText.requestFocus();
                    } else if (password.isEmpty()) {
                        passwordEditText.setError("Please Enter Password");
                        passwordEditText.requestFocus();
                    } else {
                        loginButton(mob, password);
                    }
                }
            });
        }
    }

    public void loginButton(String mobno, String pass) {

        try {
            Connection conn;
            conn = dbHelper.connectionclasss();
            if (conn == null) {
                success = false;
            } else {
                String query = "EXEC SP_REGISTRATION @MODE='REGISTRATION_LOGIN',@MPWD='"+pass+"',@MOBILE_NO="+mobno+"";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                success = false;
                if (rs != null) {

                    while (rs.next()) {
                        try {
                            success = true;

                            fullname = rs.getString("NAME");
                            mob = rs.getString("MOBILE_NO");
                            state = rs.getString("STATES");
                            city = rs.getString("CITY");

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (success == true) {
                        session.createLoginSession(fullname,mob,state,city);
                        Toast.makeText(LoginActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
/*
                        //Toast.makeText(this, "Login Successful..!!", Toast.LENGTH_SHORT).show();
                        inputLinearLayout.setVisibility(View.GONE);
                        layoutVerify.setVisibility(View.VISIBLE);
                      //  Toast.makeText(LoginActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
                        getLogin(mobEditText.getText().toString());*/

                    } else {
                        Toast.makeText(this, "Login Fail,Please enter valid data..!!", Toast.LENGTH_SHORT).show();
                    }
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