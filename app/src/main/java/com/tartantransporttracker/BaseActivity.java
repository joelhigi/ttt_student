package com.tartantransporttracker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    abstract  T getViewBinding();
    protected T binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstance){
        super.onCreate(savedInstance);
        initBinding();
    }

    private void initBinding(){
        binding = getViewBinding();
        View view = binding.getRoot();
        setContentView(view);
    }
}
