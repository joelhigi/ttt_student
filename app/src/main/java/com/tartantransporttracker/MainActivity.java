package com.tartantransporttracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.tartantransporttracker.databinding.ActivityMainBinding;
import com.tartantransporttracker.managers.UserManager;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final int RC_SIGN_IN = 123;
    private UserManager userManager = UserManager.getInstance();
    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
    }
    private void setupListeners(){
        binding.goToLogin.setOnClickListener(view -> {
            if(userManager.isCurrentUserLogged()){
                startMapActivity();
            } else{
                startSignInActivity();
            }
        });
    }


    private void startSignInActivity(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                );

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
//                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
//                        .setLogo(R.drawable.ic_launcher_foreground)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        this.handleResponseAfterSignIn(requestCode,resultCode,data);
    }

    private void showSnackBar(String message){
        Snackbar.make(binding.mainLayout, message,Snackbar.LENGTH_SHORT).show();
    }

    public void handleResponseAfterSignIn(int requestCode,int resultCode,Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if(requestCode == RC_SIGN_IN){
            //success
            if(resultCode == RESULT_OK){
                userManager.createUser();
                showSnackBar(getString(R.string.connection_succeed));
            } else {
                //ERRORS
                if(response == null){
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if( response.getError() !=null){
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if( response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }

    @Override
    protected  void onResume() {
        super.onResume();
        updateLoginButton();
    }

    private void startMapActivity(){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    private void updateLoginButton(){
        binding.goToLogin.setText(userManager.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) :
                getString(R.string.button_login_text_not_logged));
    }
}