package com.example.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewAttendanceFragment extends Fragment {

    TextView batchDeptSect, sem, hour, date, day, subject;
    ListView listView;
    ListAdapter listAdapter;
    ArrayList<HashMap<String, String>> studList = new ArrayList<>();
    Button exportData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);

        batchDeptSect = view.findViewById(R.id.batchDeptSect_TextView);
        batchDeptSect.setText(String.format("%s - %s - %s",Batchdetails.batchSelected, Batchdetails.deptSelected, Batchdetails.sectSelected));

        exportData = view.findViewById(R.id.exportData_Button);

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

        for (int i = 0; i < Batchdetails.Sno.size(); i++) {
            HashMap<String, String> temp = new HashMap<>();
            temp.put("SerialNo", Batchdetails.Sno.get(i));
            temp.put("RollNo", Batchdetails.Rno.get(i));
            temp.put("Name", Batchdetails.Sname.get(i));
            temp.put("Status", Batchdetails.attendance.get(i + 1));

            studList.add(temp);
        }

        listView = view.findViewById(R.id.fragment_view_ListView);
        listAdapter = new SimpleAdapter(getContext(), studList, R.layout.single_view_item_list,
                new String[]{"SerialNo", "RollNo", "Name", "Status"},
                new int[]{R.id.studSno_TextView, R.id.studRno_TextView, R.id.studName_TextView, R.id.studStatus_TextView});
        listView.setAdapter(listAdapter);

        exportData.setOnClickListener(v -> export());

        return view;
    }

    public void export() {
        //generate data
        StringBuilder data = new StringBuilder();
        data.append(" ,");
        data.append(batchDeptSect.getText().toString()).append(",").append(hour.getText().toString()).append(",").append(subject.getText().toString());
        data.append("\n,").append(date.getText().toString()).append(",").append(day.getText().toString()).append("\n");
        data.append("SNo,Roll No,Name,Status");
        for (int i = 0; i < Batchdetails.Sno.size(); i++) {
            data.append("\n").append(Batchdetails.Sno.get(i)).append(",").append(Batchdetails.Rno.get(i))
            .append(",").append(Batchdetails.Sname.get(i)).append(",").append(Batchdetails.attendance.get(i + 1));
        }

        try {
            //saving the file into device
            //openFileOutput("data.csv", Context.MODE_PRIVATE);
            FileOutputStream out = getContext().openFileOutput("attendance.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getContext();
            File filelocation = new File(getContext().getFilesDir(), "attendance.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}