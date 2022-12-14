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

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
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

    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    ActivityMainBinding activityMainBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        bus = findViewById(R.id.imageView2);
        welcome = findViewById(R.id.welcome);
        logo = findViewById(R.id.logo);
        bus.setAnimation(top);
        logo.setAnimation(bottom);
        welcome.setAnimation(bottom);

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