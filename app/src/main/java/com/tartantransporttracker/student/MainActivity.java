package com.tartantransporttracker.student;
/*
* Main Activity. Login redirection occurs here, with the user being shown different activities
* depending on their login status
* by Edgar, Didier, Bienvenue, Moise and Joel*/
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tartantransporttracker.student.databinding.ActivityMainBinding;
import com.tartantransporttracker.student.managers.UserManager;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final int RC_SIGN_IN = 123;
    private static final int SPLASH_SCREEN = 3000;
    private UserManager userManager = UserManager.getInstance();
    private TextView userEmail, welcome;
    private Animation top, bottom;
    private ImageView bus, logo;

    private FirebaseFirestore tttFireStore;
    private FirebaseUser tttUser;
    private String uid,userRoute;

    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    ActivityMainBinding activityMainBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        bus = findViewById(R.id.imageView2);
        welcome = findViewById(R.id.welcome);
        logo = findViewById(R.id.logo);
        bus.setAnimation(top);
        logo.setAnimation(bottom);
        welcome.setAnimation(bottom);

        tttUser = FirebaseAuth.getInstance().getCurrentUser();
        tttFireStore = FirebaseFirestore.getInstance();
        uid = tttUser.getUid();

        //Get user route for notification
        DocumentReference docRef = tttFireStore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userRoute = document.get("route").toString();
                        firebaseMessaging.subscribeToTopic(userRoute.replaceAll(" ","_"));
                    } else {
                        Log.d("Failed", "No such document");
                    }
                } else {
                    Log.d("Failed 2", "get failed with ", task.getException());
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userManager.isCurrentUserLogged()) {
                    // getUserData();
                    startMapActivity();
                } else {
                    startSignInActivity();
                }
                // finish();
            }
        }, SPLASH_SCREEN);
        // setupListeners();
    }

    private void startSignInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        // .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        // .setLogo(R.drawable.ic_launcher_foreground)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    private void showSnackBar(String message) {
        Snackbar.make(binding.drawLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    public void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            // success
            if (resultCode == RESULT_OK) {
                userManager.createUser();
                startMapActivity();
                // getUserData();
                // showSnackBar(getString(R.string.connection_succeed));
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // updateLoginButton();
    }

    private void getUserData() {
        userManager.getUserData().addOnSuccessListener(user -> {
            Log.e("In Get user function", user.getUsername());
            userEmail.setText(user.getUsername());
        });
    }

    private void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    // private void updateLoginButton(){
    // binding.goToLogin.setText(userManager.isCurrentUserLogged() ?
    // getString(R.string.button_login_text_logged) :
    // getString(R.string.button_login_text_not_logged));
    // }

}