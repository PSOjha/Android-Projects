package com.asw.mysqlreglogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView nameTextView, mobileTextView, stateTextView, cityTextView;
    ImageButton logoutImageView;
    SharedPreference sesion;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sesion = new SharedPreference(this);
        builder = new AlertDialog.Builder(MainActivity.this);

        nameTextView = findViewById(R.id.profile_emp_name_tv);
        mobileTextView = findViewById(R.id.mobile_tv);
        stateTextView = findViewById(R.id.state_tv);
        cityTextView = findViewById(R.id.city_tv);
        logoutImageView = findViewById(R.id.logout);

        nameTextView.setText(sesion.getName());
        mobileTextView.setText(sesion.getEmpMob());
        stateTextView.setText(sesion.getState());
        cityTextView.setText(sesion.getCity());

        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Are you Sure, you want to Logout...")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sesion.logoutUser();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });
    }
}