package com.example.attendancemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;

public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button markAttendanceButton;
    Spinner batchSpin, deptSpin, sectSpin, semSpin, hrSpin;
    String[] hours = {"Select Hour", "First Hour", "Second Hour", "Third Hour", "Fourth Hour", "Fifth Hour", "Sixth Hour", "Seventh Hour"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        markAttendanceButton = view.findViewById(R.id.markAttendance_Button);

        batchSpin = view.findViewById(R.id.batch_Spinner);
        deptSpin = view.findViewById(R.id.dept_Spinner);
        sectSpin = view.findViewById(R.id.section_Spinner);
        semSpin = view.findViewById(R.id.sem_Spinner);
        hrSpin = view.findViewById(R.id.hour_Spinner);

        batchSpin.setOnItemSelectedListener(this);
        ArrayAdapter batchAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, Batchdetails.batches);
        batchSpin.setAdapter(batchAdapter);

        hrSpin.setOnItemSelectedListener(this);
        ArrayAdapter hourAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, hours);
        hrSpin.setAdapter(hourAdapter);

        markAttendanceButton.setOnClickListener(v -> {
            Batchdetails.attendance.clear();
            HomeActivity.changeToMarkAttendanceFragment();
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        if (adapterView == batchSpin){
            Batchdetails.batchSelected = batchSpin.getSelectedItem().toString();
            setDeptOptions();
            setSemOption();
        }

        if (adapterView == deptSpin) {
            String[] temp;
            int index = Batchdetails.batches.indexOf(Batchdetails.batchSelected);
            String temp2 = deptSpin.getSelectedItem().toString();
            if (!temp2.equals("Select Department")) {
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

        if (adapterView == sectSpin) {
            Batchdetails.sectSelected = sectSpin.getSelectedItem().toString();
        }

        if (adapterView == deptSpin) {
            Batchdetails.deptSelected = deptSpin.getSelectedItem().toString();
        }

        if (adapterView == hrSpin) {
            Batchdetails.hrSelected = hrSpin.getSelectedItem().toString();
        }

    }

    private void setDeptOptions() {

        if (!Batchdetails.batchSelected.equals("Select a Batch")) {

            ArrayList<String> deptOptions = new ArrayList<>(Collections.singletonList("Select Department"));
            int index = Batchdetails.batches.indexOf(Batchdetails.batchSelected);
            switch (index) {
                case 1:
                    deptOptions.addAll(Batchdetails.batch0dept.keySet());
                    break;

                case 2:
                    deptOptions.addAll(Batchdetails.batch1dept.keySet());
                    break;

                case 3:
                    deptOptions.addAll(Batchdetails.batch2dept.keySet());
                    break;

                case 4:
                    deptOptions.addAll(Batchdetails.batch3dept.keySet());
                    break;

            }
            deptSpin.setOnItemSelectedListener(this);
            ArrayAdapter deptAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, deptOptions);
            deptSpin.setAdapter(deptAdapter);

        }
    }

    private void setSemOption() {
        if (!Batchdetails.batchSelected.equals("Select a Batch")) {
            ArrayList<String> semOptions = new ArrayList<>();
            semOptions.add(Batchdetails.sems.get(Batchdetails.batches.indexOf(Batchdetails.batchSelected)));
            semSpin.setOnItemSelectedListener(this);
            ArrayAdapter semAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, semOptions);
            semSpin.setAdapter(semAdapter);

            Batchdetails.semSelected = semOptions.get(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}