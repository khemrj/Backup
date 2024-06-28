package com.example.bloodlink.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bloodlink.R;
import com.example.bloodlink.databinding.ActivityDashboardBinding;
import com.example.bloodlink.donorpage.donorPage;
import com.example.bloodlink.myprofile.myprofile;
import com.example.bloodlink.searchdonor.searchdonor;

import java.util.ArrayList;

public class dashboard extends AppCompatActivity {
ActivityDashboardBinding binding;
    ImageButton notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        notify = findViewById(R.id.notification);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, donorPage.class);
                startActivity(i);
            }
        });

        ImageSlider imageSlider=binding.imageSlider;
        ArrayList<SlideModel>slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.bl1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.b2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


        binding.requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(dashboard.this, searchdonor.class);
                startActivity(intent);
            }
        });
       binding.person.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent=new Intent(dashboard.this, myprofile.class);
               startActivity(intent);
           }
       });

    }
}