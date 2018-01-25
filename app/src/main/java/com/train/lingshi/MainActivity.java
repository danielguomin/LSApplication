package com.train.lingshi;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.train.lingshi.find.FindFragment;
import com.train.lingshi.login.LoginFragment;
import com.train.lingshi.register.RegisterFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .commit();
        }
    }

}
