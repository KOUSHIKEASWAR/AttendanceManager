package com.example.attendancemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String batchSelected = "NS"; //NS for "Not Selected"
    Spinner batchSpin, deptSpin, sectSpin, semSpin, hrSpin, subSpin;
    String[] hours = {"Select Hour", "1", "2", "3", "4", "5", "6", "7", "8"};

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

        hrSpin.setOnItemSelectedListener(this);
        ArrayAdapter hourAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, hours);
        hrSpin.setAdapter(hourAdapter);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView == batchSpin){
            batchSelected = batchSpin.getSelectedItem().toString();
            setDeptOptions();
            setSemOption();
        }
        if (adapterView == deptSpin) {
            String[] temp;
            int index = Batchdetails.batches.indexOf(batchSelected);
            String temp2 = deptSpin.getSelectedItem().toString();
            if (temp2 != "Select Department") {
                switch (index) {
                    case 1:
                        temp = Batchdetails.batch0dept.get(temp2);
                        break;

                    case 2:
                        temp = Batchdetails.batch1dept.get(temp2);
                        break;

                    case 3:
                        temp = Batchdetails.batch2dept.get(temp2);
                        break;

                    case 4:
                        temp = Batchdetails.batch3dept.get(temp2);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + index);
                }
                sectSpin.setOnItemSelectedListener(this);
                ArrayAdapter sectAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, temp);
                sectSpin.setAdapter(sectAdapter);
            }
        }

    }

    private void setDeptOptions() {

        if (!batchSelected.equals("NS")) {

            ArrayList<String> deptOptions = new ArrayList<>(Collections.singletonList("Department"));
            int index = Batchdetails.batches.indexOf(batchSelected);
            ArrayList<String> temp = new ArrayList<>();
            temp.add("Select Department");
            switch (index) {
                case 1:
                    for (String ks : Batchdetails.batch0dept.keySet()) {
                        temp.add(ks);
                    }
                    break;

                case 2:
                    for (String ks : Batchdetails.batch1dept.keySet()) {
                        temp.add(ks);
                    }
                    break;

                case 3:
                    for (String ks : Batchdetails.batch2dept.keySet()) {
                        temp.add(ks);
                    }
                    break;

                case 4:
                    for (String ks : Batchdetails.batch3dept.keySet()) {
                        temp.add(ks);
                    }
                    break;

            }
            deptSpin.setOnItemSelectedListener(this);
            ArrayAdapter deptAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, temp);
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