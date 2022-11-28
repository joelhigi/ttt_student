package com.tartantransporttracker.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.tartantransporttracker.R;


public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        return  (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);
    }
}
