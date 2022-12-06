package com.example.tartantransporttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tartantransporttracker.ui.route.AdminViewRoute;
import com.example.tartantransporttracker.ui.user.ViewUser;

public class MainActivity extends AppCompatActivity {

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawLayout);

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.logout_button);

        btn = findViewById(R.id.goToLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_button:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;

                //TODO: Dear Special Ops, Any other activities can be added below for easy navigation
            //TODO: Just remember to use the switch below. Tie the ID to a menu item.
//            case R.id.scans:
//                Intent intent = new Intent(MainActivity.this,PastRecords.class);
//                startActivity(intent);
//                break;
//
//            case R.id.diseases:
//                Intent intent1 = new Intent(MainActivity.this,MaizeDiseases.class);
//                startActivity(intent1);
//                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}