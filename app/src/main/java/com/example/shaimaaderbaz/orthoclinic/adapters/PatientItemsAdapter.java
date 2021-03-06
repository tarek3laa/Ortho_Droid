package com.example.shaimaaderbaz.orthoclinic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.models.PatientItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by shaimaa Derbaz on 08/04/2018.
 */

public class PatientItemsAdapter extends RecyclerView.Adapter<PatientItemsAdapter.ViewHolder> {

    private List<PatientItem> DataSet;
    private static Context context;
    private PatientsItemsAdapterListener mPatientAdapterListener;
    public interface PatientsItemsAdapterListener {
        void onItemClicked(int id);

    }

    public PatientItemsAdapter(Context cont, List<PatientItem> dataSet, PatientsItemsAdapterListener listener)
    {
        context=cont;
        DataSet = dataSet;
        mPatientAdapterListener = listener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.patientName)TextView patientName;
        @BindView(R.id.patientId)TextView patientId;
        @BindView(R.id.age)TextView age;
        @BindView(R.id.date)TextView date;
        @BindView(R.id.info)TextView info;


        public ViewHolder(View v)
        {

            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    if(mPatientAdapterListener != null){
                        PatientItem clickedItem = DataSet.get(getAdapterPosition());
                        mPatientAdapterListener.onItemClicked(Integer.parseInt(
                                clickedItem.getId()
                        ));
                    }

                }
            });

            ButterKnife.bind(this,v);

        }

        public TextView getPatientName() {
            return patientName;
        }

        public void setPatientName(TextView patientName) {
            this.patientName = patientName;
        }

        public TextView getPatientId() {
            return patientId;
        }

        public void setPatientId(TextView patientId) {
            this.patientId = patientId;
        }

        public TextView getAge() {
            return age;
        }

        public void setAge(TextView age) {
            this.age = age;
        }

        public TextView getDate() {
            return date;
        }

        public void setDate(TextView date) {
            this.date = date;
        }

        public TextView getInfo() {
            return info;
        }

        public void setInfo(TextView info) {
            this.info = info;
        }


    }

    @Override
    public PatientItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_item, parent, false);

        return  new PatientItemsAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final PatientItemsAdapter.ViewHolder holder, int position)
    {
        if (DataSet.get(position) != null) {
            Log.d("", "Element " + position + " set.");
            holder.getPatientName().setText(DataSet.get(position).getPatientName());
            holder.getPatientId().setText(DataSet.get(position).getP_id());
            holder.getAge().setText(DataSet.get(position).getAge()+"");
            holder.getInfo().setText(DataSet.get(position).getInfo());
            holder.getDate().setText(DataSet.get(position).getCreatedDate().substring(0,10));


        }
    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }

    public void filterList(List<PatientItem> filterdNames) {
        this.DataSet = filterdNames;
        notifyDataSetChanged();
    }


}
