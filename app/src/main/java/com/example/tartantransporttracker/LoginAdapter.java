package com.example.tartantransporttracker;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tartantransporttracker.ui.login.LoginFragment;
import com.example.tartantransporttracker.ui.login.SignupFragment;

public class LoginAdapter extends FragmentPagerAdapter {
  private Context context;
  private int tabs;

  public LoginAdapter(FragmentManager fragmentManager, Context context1, int tbs){
      super(fragmentManager);
      context = context1;
      tabs =tbs;
  }

  @Override
  public int getCount(){
      return tabs;
  }
  public Fragment getItem(int p){
      switch (p){
          case 0:
              LoginFragment lf = new LoginFragment();
              return lf;
          case 1:
              SignupFragment sf = new SignupFragment();
              return sf;
          default:
              return null;
      }
  }

}
