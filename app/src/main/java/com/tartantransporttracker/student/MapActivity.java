package com.tartantransporttracker.student;
/*
* Map activity where bus location tracking takes place
* by Joel*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tartantransporttracker.student.StudentMapFragment;
import com.tartantransporttracker.student.databinding.ActivityMapBinding;
import com.tartantransporttracker.student.managers.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends DrawerBaseActivity {

    ActivityMapBinding activityMapBinding;
    private static final int RC_SIGN_IN = 123;
    private UserManager userManager = UserManager.getInstance();

    private FirebaseDatabase tttRealTime;
    private DatabaseReference tttRealTimeRef;
    private FirebaseFirestore tttFireStore;
    private FirebaseUser tttUser;
    private String userRoute;
    private String uid;
    private String refAddress;
    private Map<String, LatLng> busLocations = new HashMap<String,LatLng>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = activityMapBinding.inflate(getLayoutInflater());
        setContentView(activityMapBinding.getRoot());
        nameActivityTitle(getString(R.string.student_map));

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        //Acquiring user id
        tttUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = tttUser.getUid();
        ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 100);

        if(userManager.isCurrentUserLogged())
        {
            //Acquiring Preferred Route
            tttFireStore = FirebaseFirestore.getInstance();

            DocumentReference docRef = tttFireStore.collection("users").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userRoute = document.get("route").toString();
                            String rout = userRoute.replaceAll(" ", "_");
                            firebaseMessaging.subscribeToTopic(rout);
                            //Acquiring Locations
                            tttRealTime = FirebaseDatabase.getInstance("https://noble-radio-299516-default-rtdb.europe-west1.firebasedatabase.app/");
                            refAddress = "journeys/routes/"+userRoute;
                            tttRealTimeRef = tttRealTime.getReference(refAddress);

                            //Listening for LatLng changes
                            ValueEventListener tttRealTimeListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists())
                                    {
                                        Log.e("Analyze",""+snapshot.child("sharingStatus"));
                                        busLocations.clear();
//                        for(DataSnapshot childSnapShot : snapshot.getChildren())
//                        {

                                        boolean shareStatus = snapshot.child("sharingStatus").getValue(Boolean.class);
                                        if(shareStatus)
                                        {
                                            double latitude = snapshot.child("latitude").getValue(Double.class);
                                            double longitude = snapshot.child("longitude").getValue(Double.class);
                                            LatLng currentLoc = new LatLng(latitude,longitude);
                                            busLocations.put(userRoute+" Driver",currentLoc);

                                            FragmentManager fragManager = getSupportFragmentManager();
                                            FragmentTransaction fragSwitch = fragManager.beginTransaction();
                                            FragmentContainerView fragContainer = findViewById(R.id.fragmentContainerView);
                                            fragContainer.removeAllViews();
                                            fragSwitch.replace(R.id.fragmentContainerView, new StudentMapFragment(busLocations));
                                            fragSwitch.commit();
                                        }
                                        else{
                                            Toast noDriver = Toast.makeText(getApplicationContext(),getString(R.string.no_driver)+userRoute,Toast.LENGTH_SHORT);
                                            noDriver.show();
                                        }

//


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Authorization Error","Database Inaccessible");
                                }
                            };
                            tttRealTimeRef.addValueEventListener(tttRealTimeListener);
                        } else {
                            Log.d("Failed", "No such document");
                        }
                    } else {
                        Log.d("Failed 2", "get failed with ", task.getException());
                    }
                }
            });



        }
    }
}