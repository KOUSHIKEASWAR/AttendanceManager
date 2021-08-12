package com.example.attendancemanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Date;

public class MarkAttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView markAttendanceRecyclerView;
    MarkAttendanceAdapter markAttendanceAdapter;
    ProgressDialog loading;
    TextView batch, sem, hour, date, day, deptSect;
    Button confirmAttendance;
    Spinner subjectSpin;

    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);

        markAttendanceRecyclerView = view.findViewById(R.id.students_ListView);

        getSubItems();
        getItems();

        confirmAttendance = view.findViewById(R.id.confirmAttendance_Button);

        batch = view.findViewById(R.id.batch_TextView);
        batch.setText(Batchdetails.batchSelected);

        deptSect = view.findViewById(R.id.deptSect_TextView);
        deptSect.setText(String.format("%s - %s", Batchdetails.deptSelected, Batchdetails.sectSelected));

        sem = view.findViewById(R.id.semester_TextView);
        sem.setText(String.format("Sem : %s", Batchdetails.semSelected));

        hour = view.findViewById(R.id.hour_TextView);
        hour.setText(Batchdetails.hrSelected);

        subjectSpin = view.findViewById(R.id.subject_Spinner);
        subjectSpin.setOnItemSelectedListener(this);
        ArrayAdapter subjectAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, Batchdetails.subjects);
        subjectSpin.setAdapter(subjectAdapter);

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

        confirmAttendance.setOnClickListener(v -> {
            HomeActivity.changeToAttendanceConfirmFragment();
        });

        return view;
    }

    private void getItems() {

        loading =  ProgressDialog.show(getContext(),"Loading","please wait",false,true);

        String url = "https://script.google.com/macros/s/AKfycbws-4BNJ4I-KEM9DxQMMAlJsBtzHeQkl-LGh9ymWWfH5B96zGtsBeE9DpOvb8hHKPs/exec?action=getItems&Sheet=";
        String url2 = new StringBuilder().append(url).append(Batchdetails.batchSelected.substring(0,4)).append("_").append(Batchdetails.deptSelected).append("_").append(Batchdetails.sectSelected).toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
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

        int socketTimeOut = 50000;
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

        //Batchdetails.attendance.clear();
        if (Batchdetails.attendance.size() == 0) {
            Batchdetails.attendance.add(0, "Attendance");
            for (int i = 0; i < Batchdetails.Sno.size(); i++)
                Batchdetails.attendance.add(Integer.parseInt(Batchdetails.Sno.get(i)), "P");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        markAttendanceRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(markAttendanceRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        markAttendanceRecyclerView.addItemDecoration(dividerItemDecoration);

        markAttendanceAdapter = new MarkAttendanceAdapter(getContext(), Batchdetails.Sno, Batchdetails.Rno, Batchdetails.Sname);
        markAttendanceRecyclerView.setAdapter(markAttendanceAdapter);

        loading.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Batchdetails.subSelected = subjectSpin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getSubItems() {

        String url = "https://script.google.com/macros/s/AKfycbyigUAHnXyT_K9hhJSZcAQSpofrXbGSw8ljE5NuH4elno9n24vVMnZu5kh9h2N_UJQ/exec?action=getItems&Detail=";
        String url2 = new StringBuilder().append(url).append(Batchdetails.deptSelected).append("_SEM_").append(Batchdetails.semSelected).toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseSubItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error 404 Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void parseSubItems(String jsonResposnce) {

        String[] subs;
        String det = new StringBuilder().append(Batchdetails.deptSelected).append("_SEM_").append(Batchdetails.semSelected).toString();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            JSONObject jo = jarray.getJSONObject(0);

            subs = jo.getString(det).split("/");

            Batchdetails.subjects.clear();
            Batchdetails.subjects.add("Select a subject");
            Collections.addAll(Batchdetails.subjects, subs);

        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}