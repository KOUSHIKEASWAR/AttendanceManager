package com.example.attendancemanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    static final Fragment markAttendanceFragment = new MarkAttendanceFragment();
    static final Fragment attendanceConfirmFragment = new AttendanceConfirmFragment();

    static final Fragment viewAttendanceFragment = new ViewAttendanceFragment();
    static final Fragment attendanceFragment = new AttendanceFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    static Fragment active = attendanceFragment;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fragmentManager.beginTransaction().add(R.id.frameLayout, attendanceConfirmFragment, "5").hide(markAttendanceFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, markAttendanceFragment, "4").hide(markAttendanceFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, profileFragment, "3").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, viewAttendanceFragment, "2").hide(viewAttendanceFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, attendanceFragment, "1").commit();

        getItems();
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        int id = item.getItemId();
        switch (id){
            case R.id.view_attendance:
                fragmentManager.beginTransaction().hide(active).show(viewAttendanceFragment).commit();
                active = viewAttendanceFragment;
                return true;

            case R.id.attendance:
                fragmentManager.beginTransaction().hide(active).show(attendanceFragment).commit();
                active = attendanceFragment;
                return true;

            case R.id.profile:
                fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                return true;
        }
        return false;
    };

    public static void changeToMarkAttendanceFragment(){
        FragmentManager fragmentManager1 = attendanceFragment.getParentFragmentManager();
        //fragmentManager1.beginTransaction().hide(active).show(markAttendanceFragment).commit();
        //active = markAttendanceFragment;

        fragmentManager1.beginTransaction()
                .replace(R.id.frameLayout, MarkAttendanceFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("1") // name can be null
                .commit();

        active = markAttendanceFragment;

    }

    public static void changeToAttendanceConfirmFragment() {
        FragmentManager fragmentManager1 = attendanceFragment.getParentFragmentManager();
        //fragmentManager1.beginTransaction().hide(active).show(markAttendanceFragment).commit();
        //active = markAttendanceFragment;

        fragmentManager1.beginTransaction()
                .replace(R.id.frameLayout, AttendanceConfirmFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("4") // name can be null
                .commit();

        active = markAttendanceFragment;
    }

    public static void changeToViewAttendanceFragment() {
        FragmentManager fragmentManager1 = attendanceFragment.getParentFragmentManager();
        //fragmentManager1.beginTransaction().hide(active).show(markAttendanceFragment).commit();
        //active = markAttendanceFragment;

        fragmentManager1.beginTransaction()
                .replace(R.id.frameLayout, ViewAttendanceFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("1") // name can be null
                .commit();

        active = markAttendanceFragment;
    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbydb7Fr8ItCL_zxLL9AGEqkXuAn-472IQVP54-zmgZmVSWsZy8y63_HL2OSokQCXzw/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Error 404 Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        int socketTimeOut = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResp){

        try {
            JSONObject jobj = new JSONObject(jsonResp);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String batch = jo.getString("Batch");
                String dept = jo.getString("Dept");
                String section = jo.getString("Section");
                String sem = jo.getString("Sem");

                Batchdetails.batches.add(batch);
                Batchdetails.sems.add(sem);

                if (i == 0) {
                    String[] tempDept = dept.split("/");
                    String[] tempSection = section.split("/");
                    for (int j = 0; j < tempDept.length; j++) {
                        Batchdetails.batch0dept.put(tempDept[j], tempSection[j].split(","));
                    }
                }
                else if (i == 1) {
                    String[] tempDept = dept.split("/");
                    String[] tempSection = section.split("/");
                    for (int j = 0; j < tempDept.length; j++) {
                        Batchdetails.batch1dept.put(tempDept[j], tempSection[j].split(","));
                    }
                }
                else if (i == 2) {
                    String[] tempDept = dept.split("/");
                    String[] tempSection = section.split("/");
                    for (int j = 0; j < tempDept.length; j++) {
                        Batchdetails.batch2dept.put(tempDept[j], tempSection[j].split(","));
                    }
                }
                else if (i == 3) {
                    String[] tempDept = dept.split("/");
                    String[] tempSection = section.split("/");
                    for (int j = 0; j < tempDept.length; j++) {
                        Batchdetails.batch3dept.put(tempDept[j], tempSection[j].split(","));
                    }
                }

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();

    }

}