package com.example.emergency_response;

import static com.example.emergency_response.R.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.sql.Time;
import java.util.ArrayList;


public class reportedEm_Activity extends AppCompatActivity {
    FirebaseFirestore fstore;
    RecyclerView recyclerView;
    ArrayList<Em_model> Em_arraylist;
    Em_adapter em_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_reported_em);
        EdgeToEdge.enable(this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(reportedEm_Activity.this));
        fstore = FirebaseFirestore.getInstance();
        Em_arraylist = new ArrayList<Em_model>();
        em_adapter =new Em_adapter(reportedEm_Activity.this, Em_arraylist);
        recyclerView.setAdapter(em_adapter);
        EventChangeListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void EventChangeListener(){
        fstore.collection("Reported Emergencies").orderBy("title")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                    }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                Em_arraylist.add(dc.getDocument().toObject(Em_model.class));
                            }
                            em_adapter.notifyDataSetChanged();
                        }
                }
        });
    }
}