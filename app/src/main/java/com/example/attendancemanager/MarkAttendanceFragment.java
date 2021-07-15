package com.example.attendancemanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarkAttendanceFragment extends Fragment {

    RecyclerView markAttendanceRecyclerView;
    MarkAttendanceAdapter markAttendanceAdapter;
    ProgressDialog loading;
    TextView batchDeptSect, sem, hour, date, day, subject;
    Button confirmAttendance;
    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);

        confirmAttendance = view.findViewById(R.id.confirmAttendance_Button);

        batchDeptSect = view.findViewById(R.id.batchDeptSect_TextView);
        batchDeptSect.setText(String.format("%s-%s-%s", Batchdetails.batchSelected, Batchdetails.deptSelected, Batchdetails.sectSelected));

        sem = view.findViewById(R.id.semester_TextView);
        sem.setText(String.format("Sem : %s", Batchdetails.semSelected));

        hour = view.findViewById(R.id.hour_TextView);
        hour.setText(Batchdetails.hrSelected);

        subject = view.findViewById(R.id.subject_TextView);
        subject.setText(Batchdetails.subSelected);

        date = view.findViewById(R.id.date_TextView);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        date.setText(currentDate.format(todayDate));
        Batchdetails.dateSelected = currentDate.format(todayDate);

        day = view.findViewById(R.id.day_TextView);
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        day.setText(days[dayOfWeek - 1]);
        Batchdetails.daySelected = days[dayOfWeek - 1];

        //Batchdetails.attendance.add(currentDate.format(todayDate) + "\n" + Batchdetails.hrSelected + "\n" + Batchdetails.subSelected);

        getItems();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        markAttendanceRecyclerView = view.findViewById(R.id.students_ListView);
        markAttendanceRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(markAttendanceRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        markAttendanceRecyclerView.addItemDecoration(dividerItemDecoration);

        markAttendanceAdapter = new MarkAttendanceAdapter(view.getContext(), Batchdetails.Sno, Batchdetails.Rno, Batchdetails.Sname);
        markAttendanceRecyclerView.setAdapter(markAttendanceAdapter);

        Batchdetails.attendance.clear();
        Batchdetails.attendance.add(0, "Attendance");
        for (int i = 0; i < Batchdetails.Sno.size(); i++)
            Batchdetails.attendance.add(Integer.parseInt(Batchdetails.Sno.get(i)), "P");

        confirmAttendance.setOnClickListener(v -> {
            HomeActivity.changeToAttendanceConfirmFragment();
        });

        return view;
    }

    private void getItems() {

        loading =  ProgressDialog.show(getContext(),"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyIWvtQ5qgqR_2AuQuk5qcfMn-zIFmz677HOwqXafQAMV7DFb_4V25W9AJCvom-Gpc/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error 404 Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        int socketTimeOut = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResposnce) {

        Batchdetails.Sno.clear();
        Batchdetails.Rno.clear();
        Batchdetails.Sname.clear();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String sno = jo.getString("Sno");
                String rollno = jo.getString("RollNumber");
                String sname = jo.getString("StudentName");

                Batchdetails.Sno.add(sno);
                Batchdetails.Rno.add(rollno);
                Batchdetails.Sname.add(sname);

            }

        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();
    }

}