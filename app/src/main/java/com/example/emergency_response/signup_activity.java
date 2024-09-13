package com.example.emergency_response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup_activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email,phonenumber,fullname;
    private EditText password;
    private Button signUpButton;
    private FirebaseFirestore fstore;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password2);
        signUpButton = findViewById(R.id.signupButton);
        fullname = findViewById(R.id.fullname);
        phonenumber = findViewById(R.id.phonenumber);
        fstore = FirebaseFirestore.getInstance();
        //sign up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String name = fullname.getText().toString();
                String phone = phonenumber.getText().toString();

                if (useremail.isEmpty()){
                    email.setError("Email cannot be empty");
                }
                if(pass.isEmpty()){
                    password.setError("password cannot be empty");
                }else{
                    auth.createUserWithEmailAndPassword(useremail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(signup_activity.this, "sign up successful", Toast.LENGTH_SHORT).show();
                                userid = auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userid);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fname",name);
                                user.put("email",useremail);
                                user.put("phone",phone);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "onSuccess: user profile is created for"+ userid);
                                    }
                                });
                                startActivity(new Intent(signup_activity.this, login_activity.class));
                            }else{
                                Toast.makeText(signup_activity.this, "Sign up failed"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}