package com.example.attendancemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AttendanceFragment extends Fragment {

    AttendanceFragment attendanceFragment = new AttendanceFragment();

    static final Fragment batchChildFragment = new AtChildBatchFragment();
    final Fragment listChildFragment = new AtChildListFragment();
    final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    static Fragment active = batchChildFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transaction.add(R.id.frameLayout2, listChildFragment, "2").hide(listChildFragment).commit();
        transaction.add(R.id.frameLayout2, batchChildFragment, "1").commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }
}