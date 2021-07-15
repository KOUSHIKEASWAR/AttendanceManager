package com.example.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.attendancemanager.Batchdetails.attendance;

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.ViewHolder> {

    ArrayList<String> serial_number_array;
    ArrayList<String> roll_number_array;
    ArrayList<String> student_name_array;
    Context context;

    public MarkAttendanceAdapter(Context context, ArrayList<String> sno_array, ArrayList<String> rno_array, ArrayList<String> studname_array) {
        this.serial_number_array = sno_array;
        this.roll_number_array = rno_array;
        this.student_name_array = studname_array;
        this.context = context;
    }

    @NonNull
    @Override
    public MarkAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_stduent_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull final ViewHolder holder, final int position) {
        holder.snoTextView.setText(serial_number_array.get(position));
        holder.rnoTextView.setText(roll_number_array.get(position));
        holder.studnameTextView.setText(student_name_array.get(position));

        String stat = attendance.get(position + 1);

        if (stat == "P") {
            holder.presentRadioButton.setChecked(true);
        }
        else if (stat == "A") {
            holder.absentRadioButton.setChecked(true);
        }
        else if (stat == "OD") {
            holder.odRadioButton.setChecked(true);
        }

        holder.presentRadioButton.setOnClickListener(v -> {
                    attendance.set(position + 1, "P");
                    //String temp = String.valueOf(holder.studnameTextView.getText());
                    //Toast.makeText(context, temp + " present", Toast.LENGTH_SHORT).show();
                });

        holder.absentRadioButton.setOnClickListener(v -> {
                    attendance.set(position + 1, "A");
                    //String temp2, temp = String.valueOf(holder.studnameTextView.getText());
                    //temp2 = attendance.toString();
                    //Toast.makeText(context, temp2, Toast.LENGTH_LONG).show();
                });

        holder.odRadioButton.setOnClickListener(v -> {
                    attendance.set(position + 1, "OD");
                    //String temp = String.valueOf(holder.studnameTextView.getText());
                    //Toast.makeText(context, temp + " is on OD", Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public int getItemCount() {
        return serial_number_array.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView snoTextView, rnoTextView, studnameTextView;
        //RadioGroup radioGroup;
        RadioButton presentRadioButton, absentRadioButton, odRadioButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            snoTextView = itemView.findViewById(R.id.sno_TextView);
            rnoTextView = itemView.findViewById(R.id.studentRollNo_TextView);
            studnameTextView = itemView.findViewById(R.id.studentName_TextView);
            //radioGroup = itemView.findViewById(R.id.status_RadioGroup);
            presentRadioButton = itemView.findViewById(R.id.present_RadioButton);
            absentRadioButton = itemView.findViewById(R.id.absent_RadioButton);
            odRadioButton = itemView.findViewById(R.id.od_RadioButton);

        }
    }
}
