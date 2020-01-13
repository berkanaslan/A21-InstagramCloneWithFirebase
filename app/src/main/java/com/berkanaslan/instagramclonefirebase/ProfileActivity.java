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

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> userEmailForProfile;
    ArrayList<String> userCommentForProfile;
    ArrayList<String> userImgDateForProfile;
    ArrayList<String> userImgUrlForProfile;
    ProfileRecyclerAdapter profileRecyclerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.instagram_options_menu,menu);
        menu.getItem(0).setTitle("Feed");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.newPost) {
            Intent gotoUpload = new Intent(ProfileActivity.this, UploadActivity.class);
            startActivity(gotoUpload);
        } else if (item.getItemId() == R.id.signOut) {
            firebaseAuth.signOut();
            Intent gotoLoginActivity = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(gotoLoginActivity);
            finish();
        } else if (item.getItemId() == R.id.profile) {
            Intent goToProfile = new Intent(ProfileActivity.this,FeedActivity.class);
            startActivity(goToProfile);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userEmailForProfile = new ArrayList<>();
        userCommentForProfile = new ArrayList<>();
        userImgDateForProfile = new ArrayList<>();
        userImgUrlForProfile = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestoreForProfile();

        //RecycleView
        RecyclerView recyclerView = findViewById(R.id.profileRacyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileRecyclerAdapter = new ProfileRecyclerAdapter(userEmailForProfile, userCommentForProfile, userImgUrlForProfile, userImgDateForProfile);
        recyclerView.setAdapter(profileRecyclerAdapter);

    }

    public void getDataFromFirestoreForProfile() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String profileCheckID = firebaseUser.getEmail();

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.whereEqualTo("User_EMail", profileCheckID).orderBy("Upload_Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    System.out.println(e);
                    //Toast.makeText(ProfileActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String comment = (String) data.get("User_Comment");
                        String userEmail = (String) data.get("User_EMail");
                        String imgdataurl = (String) data.get("User_Image_Url");

                        Timestamp dataTimestamp = (Timestamp) data.get("Upload_Date");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM kk:mm", Locale.getDefault());
                        String formattedDate = dateFormat.format(dataTimestamp.toDate());

                        if (comment.matches("")) { userCommentForProfile.add(comment);
                        } else { userCommentForProfile.add(userEmail + ": " + comment); }
                        userEmailForProfile.add(userEmail);
                        userImgUrlForProfile.add(imgdataurl);
                        userImgDateForProfile.add(formattedDate);
                        profileRecyclerAdapter.notifyDataSetChanged();
                    }

                }


            }
        });

    }
}
