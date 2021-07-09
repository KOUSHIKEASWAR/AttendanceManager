package com.example.attendancemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String batchSelected = "NS"; //NS for Not Selected
    Spinner batchSpin, deptSpin, sectSpin, semSpin, hrSpin, subSpin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        batchSpin = view.findViewById(R.id.batch_Spinner);
        deptSpin = view.findViewById(R.id.dept_Spinner);
        sectSpin = view.findViewById(R.id.section_Spinner);
        semSpin = view.findViewById(R.id.sem_Spinner);
        hrSpin = view.findViewById(R.id.hour_Spinner);
        subSpin = view.findViewById(R.id.subject_Spinner);

        batchSpin.setOnItemSelectedListener(this);
        ArrayAdapter batchAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, Batchdetails.batches);
        batchSpin.setAdapter(batchAdapter);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView == batchSpin){
            batchSelected = batchSpin.getSelectedItem().toString();
            setDeptOptions();
            setSemOption();
        }

    }

    private void setDeptOptions() {

        if (!batchSelected.equals("NS")) {

            ArrayList<String> deptOptions = new ArrayList<>(Collections.singletonList("Department"));
            int index = Batchdetails.batches.indexOf(batchSelected);
            String[] temp;

            switch (index) {
                case 0:
                    temp = Batchdetails.batch0dept.get(0);
                    Collections.addAll(deptOptions, temp);
                    break;

                case 1:
                    temp = Batchdetails.batch1dept.get(0);
                    Collections.addAll(deptOptions, temp);
                    break;

                case 2:
                    temp = Batchdetails.batch2dept.get(0);
                    Collections.addAll(deptOptions, temp);
                    break;

                case 3:
                    temp = Batchdetails.batch3dept.get(0);
                    Collections.addAll(deptOptions, temp);
                    break;
            }
            deptSpin.setOnItemSelectedListener(this);
            ArrayAdapter deptAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, deptOptions);
            deptSpin.setAdapter(deptAdapter);

        }
    }

    private void setSemOption() {
        if (!batchSelected.equals("NS")) {
            ArrayList<String> semOptions = new ArrayList<>();
            semOptions.add(Batchdetails.sems.get(Batchdetails.batches.indexOf(batchSelected)));
            semSpin.setOnItemSelectedListener(this);
            ArrayAdapter semAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, semOptions);
            semSpin.setAdapter(semAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}