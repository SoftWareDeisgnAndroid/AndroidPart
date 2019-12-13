package com.example.mylectureroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class nextActivity extends AppCompatActivity {
    private Fragment firstFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        firstFragment = new courseFragment();
        setDefaultFragment();

    }

    public void setDefaultFragment(){
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.container, firstFragment);
    transaction.commit();
    }
}


