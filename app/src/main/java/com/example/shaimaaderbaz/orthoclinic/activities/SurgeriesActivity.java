package com.example.shaimaaderbaz.orthoclinic.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.adapters.SurgeryAdapter;
import com.example.shaimaaderbaz.orthoclinic.models.Surgery;
import com.example.shaimaaderbaz.orthoclinic.test;

import java.util.List;

public class SurgeriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private List<Surgery> surgeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgeries);
        // for testing purposes
        surgeries = test.list;
        //init view
        recyclerView = findViewById(R.id.surgery_list);
        floatingActionButton = findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** open {@link AddSurgeryActivity } to add new Surgery  **/
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SurgeriesActivity.this, AddSurgeryActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SurgeryAdapter surgeryAdapter = new SurgeryAdapter(surgeries, new SurgeryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Surgery item) {
                Intent intent = new Intent(SurgeriesActivity.this,SurgeryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddSurgeryActivity.SURGERY_EXTRA,item);
                intent.putExtra(AddSurgeryActivity.SURGERY_EXTRA,bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(surgeryAdapter);
    }


}
