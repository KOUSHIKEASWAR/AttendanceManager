package com.example.attendancemanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AttendanceConfirmFragment extends Fragment {

    ListView absentList, odList;
    ListAdapter absentListAdapter, odListAdapter;
    TextView batchDeptSect, sem, hour, date, day, subject, present, absent, od;
    int present_count = 0, absent_count = 0, od_count = 0;
    Button saveAttendance;

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_confirm, container, false);

        saveAttendance = view.findViewById(R.id.saveAttendance_Button);

        batchDeptSect = view.findViewById(R.id.batchDeptSect_TextView);
        batchDeptSect.setText(Batchdetails.batchSelected);

        sem = view.findViewById(R.id.semester_TextView);
        sem.setText(String.format("Sem : %s", Batchdetails.semSelected));

        hour = view.findViewById(R.id.hour_TextView);
        hour.setText(Batchdetails.hrSelected);

        subject = view.findViewById(R.id.subject_TextView);
        subject.setText(Batchdetails.subSelected);

        date = view.findViewById(R.id.date_TextView);
        date.setText(Batchdetails.dateSelected);

        day = view.findViewById(R.id.day_TextView);
        day.setText(Batchdetails.daySelected);

        Batchdetails.absenteesList.clear();
        Batchdetails.odList.clear();

        for (int i = 1; i < Batchdetails.attendance.size(); i++) {
            switch (Batchdetails.attendance.get(i)) {
                case "P":
                    present_count++;
                    break;

                case "A":
                    absent_count++;
                    HashMap<String, String> item = new HashMap<>();
                    item.put("RollNumber", Batchdetails.Rno.get(i - 1));
                    item.put("Name", Batchdetails.Sname.get(i - 1));
                    Batchdetails.absenteesList.add(item);
                    break;

                case "OD":
                    od_count++;
                    HashMap<String, String> item2 = new HashMap<>();
                    item2.put("RollNumber", Batchdetails.Rno.get(i - 1));
                    item2.put("Name", Batchdetails.Sname.get(i - 1));
                    Batchdetails.odList.add(item2);
                    break;
            }
        }

        present = view.findViewById(R.id.presentees_TextView);
        present.setText(String.format("No. of Present : %d", present_count));

        absent = view.findViewById(R.id.absentees_TextView);
        absent.setText(String.format("No. of Absent : %d", absent_count));

        od = view.findViewById(R.id.od_TextView);
        od.setText(String.format("No. on OD : %d", od_count));

        absentList = view.findViewById(R.id.absentees_ListView);
        absentListAdapter = new SimpleAdapter(getContext(), Batchdetails.absenteesList, R.layout.single_item_list_view,
                new String[]{"RollNumber","Name"},new int[]{R.id.studentRno_TextView,R.id.sName_TextView});
        absentList.setAdapter(absentListAdapter);

        odList = view.findViewById(R.id.od_ListView);
        odListAdapter = new SimpleAdapter(getContext(), Batchdetails.odList, R.layout.single_item_list_view,
                new String[]{"RollNumber","Name"},new int[]{R.id.studentRno_TextView,R.id.sName_TextView});
        odList.setAdapter(odListAdapter);

        saveAttendance.setOnClickListener(v -> {
            //Toast.makeText(getContext(), String.valueOf(Batchdetails.attendance.subList(1, Batchdetails.attendance.size())), Toast.LENGTH_LONG).show();
            addItemToSheet();
            HomeActivity.changeToViewAttendanceFragment();
        });

        return view;
    }

    private void   addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(getContext(),"Adding Item","Please wait");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyIWvtQ5qgqR_2AuQuk5qcfMn-zIFmz677HOwqXafQAMV7DFb_4V25W9AJCvom-Gpc/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        //Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getContext(),MainActivity.class);
                        //startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();
                String status = null;
                String attDet;

                //here we pass params
                parmas.put("action", "addItem");
                attDet = new StringBuilder().append(Batchdetails.dateSelected).append("\n").append(Batchdetails.hrSelected).append("\n").append(Batchdetails.subSelected).toString();
                parmas.put("Attendance", attDet);
                for (int i = 1; i < Batchdetails.attendance.size(); i++) {
                    status = new StringBuilder().append(status).append("/").append(Batchdetails.attendance.get(i)).toString();
                }
                parmas.put("Status", status);

                //parmas.put("Status", String.valueOf(Batchdetails.attendance.subList(1, Batchdetails.attendance.size())));

                return parmas;
            }
        };

        int socketTimeOut = 10000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        queue.add(stringRequest);

    }

}