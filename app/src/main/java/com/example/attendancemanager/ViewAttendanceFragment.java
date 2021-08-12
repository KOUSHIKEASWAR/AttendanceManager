package com.example.attendancemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewAttendanceFragment extends Fragment {

    TextView batchDeptSect, sem, hour, date, day, subject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);

        batchDeptSect = view.findViewById(R.id.batchDeptSect_TextView);
        batchDeptSect.setText(Batchdetails.batchSelected);

        sem = view.findViewById(R.id.semester_TextView);
        sem.setText(String.format("Sem : %s", Batchdetails.semSelected));

        hour = view.findViewById(R.id.hour_TextView);
        hour.setText(Batchdetails.hrSelected);

        subject = view.findViewById(R.id.sub_TextView);
        subject.setText(Batchdetails.subSelected);

        date = view.findViewById(R.id.date_TextView);
        date.setText(Batchdetails.dateSelected);

        day = view.findViewById(R.id.day_TextView);
        day.setText(Batchdetails.daySelected);

        return view;
    }
}