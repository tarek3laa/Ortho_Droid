package com.example.shaimaaderbaz.orthoclinic.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.adapters.ImageAdapter;
import com.example.shaimaaderbaz.orthoclinic.models.Surgery;

public class SurgeryActivity extends AppCompatActivity {
    private ViewPager vpImagesContainer;
    private TextView tvTitle, tvDate, tvTime, tvPatient, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgery);
        // init view
        tvTitle = findViewById(R.id.textViewTitle);
        tvDate = findViewById(R.id.textViewDate);
        tvTime = findViewById(R.id.textViewTime);
        tvPatient = findViewById(R.id.textViewName);
        tvAddress = findViewById(R.id.textViewAddress);
        vpImagesContainer = findViewById(R.id.images_container);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(AddSurgeryActivity.SURGERY_EXTRA);
        Surgery surgery = (Surgery) bundle.getSerializable(AddSurgeryActivity.SURGERY_EXTRA);

        if (surgery.getImages() != null) {
            ImageAdapter imageAdapter = new ImageAdapter(this, surgery.getImages());
            vpImagesContainer.setAdapter(imageAdapter);
        }
        else Toast.makeText(this, "null pointer", Toast.LENGTH_LONG).show();
        tvTitle.setText(surgery.getTitle());
        tvDate.setText(surgery.getDate());
        tvTime.setText(surgery.getTime());
        tvPatient.setText(surgery.getPatientName());
        tvAddress.setText(surgery.getAddress());
    }
}
