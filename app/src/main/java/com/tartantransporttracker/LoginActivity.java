package com.tartantransporttracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tartantransporttracker.ui.route.AdminViewRoute;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager viewPager;
    TabLayout tabLayout;
    Button signupBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.auth_tabLayout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        signupBtn = findViewById(R.id.signupBtn);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public void onClick(View view) {
        System.out.println("==============================================");
        Intent intent1 = new Intent(this, AdminViewRoute.class);
        startActivity(intent1);
    }
}
