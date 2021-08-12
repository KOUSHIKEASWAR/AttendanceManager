package com.example.attendancemanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Batchdetails {
    static ArrayList<String> batches = new ArrayList<>(Arrays.asList("Select a Batch"));
    static HashMap<String, String[]> batch0dept = new HashMap<>();
    static HashMap<String, String[]> batch1dept = new HashMap<>();
    static HashMap<String, String[]> batch2dept = new HashMap<>();
    static HashMap<String, String[]> batch3dept = new HashMap<>();
    static ArrayList<String> sems = new ArrayList<>(Arrays.asList("Sem"));

    static ArrayList<String> subjects = new ArrayList<>(Arrays.asList("Select Subject"));
    static String batchSelected = "2000", deptSelected, sectSelected, semSelected, hrSelected, subSelected, daySelected, dateSelected;

    static ArrayList<String> Sno = new ArrayList<>();
    static ArrayList<String> Rno = new ArrayList<>();
    static ArrayList<String> Sname = new ArrayList<>();

    static ArrayList<String> attendance = new ArrayList<>();

    static ArrayList<HashMap<String, String>> absenteesList = new ArrayList<>();
    static ArrayList<HashMap<String, String>> odList = new ArrayList<>();

}
