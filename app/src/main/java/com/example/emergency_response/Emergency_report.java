package com.example.emergency_response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Emergency_report extends AppCompatActivity {

    CardView fireCard;
    CardView accidentCard;
    CardView securityCard;
    CardView healthCard;
    CardView othersCard;
    Button Logout;
    TextView responder_login;
    FirebaseAuth fauth ;
    public String pageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_report);
        //report fire
        fireCard = findViewById(R.id.card1);
        fireCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageTitle = "Fire";
                Intent intent = new Intent(Emergency_report.this, Success_activity.class);
                try {
                    intent.putExtra("Title",pageTitle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });
        //report accident
        accidentCard = findViewById(R.id.card2);
        accidentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageTitle = "Accident";
                Intent intent = new Intent(Emergency_report.this, Success_activity.class);
                try {
                    intent.putExtra("Title",pageTitle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });
        //report security issue
        securityCard = findViewById(R.id.card3);
        securityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageTitle = "Security";
                Intent intent = new Intent(Emergency_report.this, Success_activity.class);
                try {
                    intent.putExtra("Title",pageTitle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });
        // report health issue
        healthCard = findViewById(R.id.card4);
        healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageTitle = "Health";
                Intent intent = new Intent(Emergency_report.this, Success_activity.class);
                try {
                    intent.putExtra("Title",pageTitle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });
        othersCard = findViewById(R.id.card5);
        othersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageTitle = "Other";
                Intent intent = new Intent(Emergency_report.this, other_emergency_Activity.class);
                try {
                    intent.putExtra("Title",pageTitle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
            }
        });
        //logout
        Logout = findViewById(R.id.logoutButton);
        fauth = FirebaseAuth.getInstance();
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Emergency_report.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //responder login
        responder_login = findViewById(R.id.responder_login);
        // Redirect user to responder login activity
        responder_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Emergency_report.this,responder_loginActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}