package com.example.attendancemanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Batchdetails {
    //The list of batches is stored in this ArrayList which is present in google sheet
    static ArrayList<String> batches = new ArrayList<>(Collections.singletonList("Select a batch"));

    /*The departments present in each batch and its respective sections are stored
      in the following Hashmaps. */
    static HashMap<String, String[]> batch0dept = new HashMap<>();
    static HashMap<String, String[]> batch1dept = new HashMap<>();
    static HashMap<String, String[]> batch2dept = new HashMap<>();
    static HashMap<String, String[]> batch3dept = new HashMap<>();

    //The current semester for the batch being selected is stored in this variable
    static ArrayList<String> sems = new ArrayList<>(Collections.singletonList("Sem"));

    //With respect to the department selected and the semester the subjects are stored
    static ArrayList<String> subjects = new ArrayList<>(Collections.singletonList("Select a subject"));

    //The following variables contain the values the user selects in the spinner
    static String batchSelected = "2000", deptSelected, sectSelected, semSelected, hrSelected, subSelected, daySelected, dateSelected;

    /*According to the batch details, department and section students roll number and name are
      stored in the following variables. Sno denotes serial number.*/
    static ArrayList<String> Sno = new ArrayList<>();
    static ArrayList<String> Rno = new ArrayList<>();
    static ArrayList<String> Sname = new ArrayList<>();

    /*When the students list is loaded their attendance status is created
      and set to 'present' as default and each time the position (serial number - 1) is
      updated when attendance is updated for a student. */
    static ArrayList<String> attendance = new ArrayList<>();

    /* When a person becomes absent his/her name is added to the absenteesList
       Same way when she/he gets OD then their name is added to odList. */
    static ArrayList<HashMap<String, String>> absenteesList = new ArrayList<>();
    static ArrayList<HashMap<String, String>> odList = new ArrayList<>();

}
