package com.berkanaslan.instagramclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    FeedRecyclerAdapter feedRecyclerAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.instagram_options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.newPost) {
            Intent gotoUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(gotoUpload);
        } else if (item.getItemId() == R.id.signOut) {
            firebaseAuth.signOut();
            Intent gotoLoginActivity = new Intent(FeedActivity.this, LoginActivity.class);
            startActivity(gotoLoginActivity);
            finish();
        } else if (item.getItemId() == R.id.profile) {
            Intent goToProfile = new Intent(FeedActivity.this,ProfileActivity.class);
            startActivity(goToProfile);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userCommentFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFromFB, userCommentFromFB, userImageFromFB);
        recyclerView.setAdapter(feedRecyclerAdapter);


    }

    public void getDataFromFirestore() {

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("Upload_Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(FeedActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            //Casting
                            String comment = (String) data.get("User_Comment");
                            String userEmail = (String) data.get("User_EMail");
                            String imgdataurl = (String) data.get("User_Image_Url");

                            if (comment.matches("")) { userCommentFromFB.add(comment);
                            } else { userCommentFromFB.add(userEmail + ": " + comment); }
                            userEmailFromFB.add(userEmail);
                            userImageFromFB.add(imgdataurl);
                            feedRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
            }
        });

    }
}
