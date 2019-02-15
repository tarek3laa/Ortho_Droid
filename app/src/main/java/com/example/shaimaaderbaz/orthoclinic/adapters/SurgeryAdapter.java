package com.example.shaimaaderbaz.orthoclinic.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.models.PatientItem;
import com.example.shaimaaderbaz.orthoclinic.models.Surgery;

import java.util.List;

public class SurgeryAdapter extends RecyclerView.Adapter<SurgeryAdapter.ViewHolder> {
    List<Surgery> surgeries;
    private final OnItemClickListener listener;

    public SurgeryAdapter(List<Surgery> surgeries, OnItemClickListener listener) {
        this.surgeries = surgeries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.surgery_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.title.setText(surgeries.get(position).getTitle());
        holder.date.setText(surgeries.get(position).getDate());
        holder.time.setText(surgeries.get(position).getTime());
        holder.address.setText(surgeries.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return surgeries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time, address;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            date = itemView.findViewById(R.id.textViewDate);
            time = itemView.findViewById(R.id.textViewTime);
            address = itemView.findViewById(R.id.textViewAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(surgeries.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Surgery item);
    }
}
