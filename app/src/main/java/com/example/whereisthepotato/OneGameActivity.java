package com.example.whereisthepotato;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.whereisthepotato.databinding.AppBarMainBinding;

public class OneGameActivity extends AppCompatActivity {

    private AppBarMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AppBarMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        startService(new Intent(this, LocationService.class));

        OneGamePagerFragment fragment = new OneGamePagerFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
    }

}
