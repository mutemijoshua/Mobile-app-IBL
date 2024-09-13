package com.example.emergency_response;

import static com.example.emergency_response.R.id.location;
import static com.example.emergency_response.R.id.submitButton;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emergency_response.util.Appdata;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Success_activity extends AppCompatActivity {
    private EditText description, currentlocation ;
    private TextView title;
    Bundle intentExtras;
    private Button Submit;
    private String newtitle;
    String n,t,tt;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    private String Title,Description;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success);
        description = findViewById(R.id.Description);
        currentlocation = findViewById(R.id.locationdetails);
        title = findViewById(R.id.titleText);
        Submit = findViewById(R.id.submitButton);
        intentExtras = getIntent().getExtras();
        if (intentExtras != null){
            newtitle = intentExtras.getString("Title");
            title.setText(newtitle);
        }
        fstore = FirebaseFirestore.getInstance();
        sendemail();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getlastlocation();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = description.getText().toString().trim();
                Description = currentlocation.getText().toString().trim();
                sendemail();
                uploadData(Title,Description);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //send email notification(with emergency details and location) to the responder
    public void sendemail() {
        try {
            String senderEmail = "joshuamutua555@gmail.com";
            String receiverEmail = "joshu.herman97@gmail.com";
            String passwordEmail = "azkk pkfj qdlp ovap";
            String emailHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", emailHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            javax.mail.Session session = Session.getInstance (properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, passwordEmail);
                }
            });
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

            mimeMessage.setSubject("Subject: Emergency Report");
            mimeMessage.setText( newtitle + " Emergency reported at Pin [ "+location + "] " + " Title: " +Title + " Description: "+Description);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
    //Request for permission to access the current loation for the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getlastlocation();
            }else {
                Toast.makeText(this, "Location permission denied , please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //Get the current location of the user
    private void getlastlocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location ;
                }
            }
        });
    }
    //Update the emergency case report to fire store database
    public void uploadData(String title,String Description){
        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();
        doc.put("id",id);//set id of the data
        doc.put("title",title);
        doc.put("description", Description);
        //add data
        fstore.collection("Reported Emergencies").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Success_activity.this, "Emergency Succesfully reported, help is on the way",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Success_activity.this, "Unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}