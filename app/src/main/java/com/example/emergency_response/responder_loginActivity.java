package com.example.emergency_response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;
public class responder_loginActivity extends AppCompatActivity {
    private EditText LoginCode,ResponderPassword;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_responder_login);
        LoginCode = findViewById(R.id.code);
        ResponderPassword = findViewById(R.id.Password);
        login = findViewById(R.id.LoginButton);
        String code , pass ;
        code = LoginCode.getText().toString() ;
        pass = ResponderPassword.getText().toString();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(responder_loginActivity.this,reportedEm_Activity.class);
                    startActivity(intent);
                    Toast.makeText(responder_loginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}