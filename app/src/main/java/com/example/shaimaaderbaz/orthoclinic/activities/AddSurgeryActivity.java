package com.example.shaimaaderbaz.orthoclinic.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.adapters.ImageAdapter;
import com.example.shaimaaderbaz.orthoclinic.models.PatientItem;
import com.example.shaimaaderbaz.orthoclinic.models.Surgery;
import com.example.shaimaaderbaz.orthoclinic.presenter.PatientListPresenterImp;
import com.example.shaimaaderbaz.orthoclinic.test;
import com.example.shaimaaderbaz.orthoclinic.views.PatientListView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddSurgeryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PatientListView {
    private static final int PENDING_INTENT_RC = 100;
    private EditText etTitle, etAddress;
    private Spinner spPatient;
    private TextView tvTime;
    private Button btAddPatient, btNewImages;
    private ViewPager vpImagesContainer;
    private String time = "00:00 AM";
    private String date = "12/12/1970";
    private int PICK_IMAGE_MULTIPLE = 1;
    List<Uri> images;
    private PatientListPresenterImp presenter;
    private List<PatientItem> allPatients;
    public static final String ACTION_ADD_PATIENT = "add_patient";
    private static final Calendar ALARM_TIME = Calendar.getInstance();
    public static final String SURGERY_EXTRA = "surgery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_surgery);
        // init view
        etTitle = findViewById(R.id.editTextTitle);
        etAddress = findViewById(R.id.editTextAddress);
        spPatient = findViewById(R.id.spinnerPatient);
        tvTime = findViewById(R.id.textViewTime);
        btAddPatient = findViewById(R.id.buttonNewPatient);
        btNewImages = findViewById(R.id.buttonNewImage);
        setTitle("add Surgery");
        // retrieve Patients From Server
        presenter = new PatientListPresenterImp(this);


        // open Date picker dialog to set date
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(AddSurgeryActivity.this, AddSurgeryActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });
        // open gallery to chose images
        btNewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        btAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSurgeryActivity.this, HomeActivity.class);
                intent.setAction(ACTION_ADD_PATIENT);
                startActivity(intent);
            }
        });

    }

    private void addSurgery(String title, String address, String pid, String name, String time, String date, List<Uri> images) {
        Surgery surgery = new Surgery(pid, name, title, address, date, time, images);
        test.list.add(surgery);

        setAlarm(ALARM_TIME.getTimeInMillis(), surgery);
        System.out.println("pid = " + pid);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_surgery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                String pid = allPatients.get(spPatient.getSelectedItemPosition()).getId();
                String name = allPatients.get(spPatient.getSelectedItemPosition()).getPatientName();
                addSurgery(etTitle.getText().toString(), etAddress.getText().toString(), pid, name, time, date, images);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method is called when the user chooses the date
     *
     * @param datePicker
     * @param i          year
     * @param i1         month
     * @param i2         day Of month
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        ALARM_TIME.set(Calendar.YEAR, i);
        ALARM_TIME.set(Calendar.MONTH, i1);
        ALARM_TIME.set(Calendar.DAY_OF_MONTH, i2);


        // save choosing date
        date = String.valueOf(i2) + "/" + String.valueOf(i1 + 1) + "/" + String.valueOf(i);
        // open time picker dialog to set time
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddSurgeryActivity.this, AddSurgeryActivity.this, 12, 12, false);
        timePickerDialog.show();
    }

    /**
     * This method is called when the user chooses the date
     *
     * @param timePicker
     * @param i          hour
     * @param i1         minute
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        ALARM_TIME.set(Calendar.HOUR_OF_DAY, i);
        ALARM_TIME.set(Calendar.MINUTE, i1);
        ALARM_TIME.set(Calendar.SECOND, 0);
        // convert time from 24 to 12 hour
        String state = "am";
        if (i > 12) {
            i -= 12;
            state = "pm";
        }
        // save choosing time
        time = String.valueOf(i) + " : " + String.valueOf(i1) + " " + state;

        tvTime.setText(time + "  " + date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    final List<Uri> imagesUri = new ArrayList<>();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) // add images uri to list
                        imagesUri.add(data.getClipData().getItemAt(i).getUri());

                    images = imagesUri;
                    // set images to view pager
                    vpImagesContainer = findViewById(R.id.images_container);
                    ImageAdapter imageAdapter = new ImageAdapter(AddSurgeryActivity.this, images);
                    vpImagesContainer.setAdapter(imageAdapter);
                }
            }
        }


    }


    @Override
    public void showPatients(List<PatientItem> AllPatients) {
        allPatients = AllPatients;
        List<String> list = new ArrayList<>();
        for (PatientItem patientItem : allPatients)
            list.add(patientItem.getPatientName());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddSurgeryActivity.this, android.R.layout.simple_spinner_item, list);
        spPatient.setAdapter(arrayAdapter);

    }

    @Override
    public void showSearchResult(List<PatientItem> filteredPatients) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.retreivePatientsFromServer();
    }

    private void setAlarm(long time, Surgery surgery) {
        final long hour = (long) 3.6e+6;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SURGERY_EXTRA, surgery);
        intent.putExtra(SURGERY_EXTRA, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, PENDING_INTENT_RC, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, (time - hour), pendingIntent);

    }

}
