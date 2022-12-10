package com.tartantransporttracker.ui.route;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.tartantransporttracker.BaseActivity;
import com.tartantransporttracker.MainActivity;
import com.tartantransporttracker.R;
import com.tartantransporttracker.databinding.ActivityAdminViewRouteBinding;
import com.tartantransporttracker.databinding.ActivityMainBinding;


public class AdminViewRoute extends BaseActivity<ActivityAdminViewRouteBinding> implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;

    Button deleteBtn;

    @Override
    public ActivityAdminViewRouteBinding getViewBinding() {
        return ActivityAdminViewRouteBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);
        deleteBtn = findViewById(R.id.deleteBtn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.SecondFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete route
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


}