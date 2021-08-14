package com.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    Button signupbtn;
    boolean isEmailValid;
    String finalPassword;
    FirebaseFirestore mFireStore;
    FirebaseAuth fAuth;
    EditText staffId, staffName, collegeMail, department, phoneNumber, password, passwordCheck;
    String emailPattern = "[a-z.]*+@ritchennai.edu.in$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupbtn = findViewById(R.id.sigUp_Button);
        staffId = findViewById(R.id.staffId_EditText);
        staffName = findViewById(R.id.staffName_EditText);
        collegeMail = findViewById(R.id.mail_editText);
        department = findViewById(R.id.dept_EditText);
        phoneNumber = findViewById(R.id.phone_EditText);
        password = findViewById(R.id.createPassword_EditText);
        passwordCheck = findViewById(R.id.confirmPassword_EditText);
        fAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String staffid = staffId.getText().toString().trim();
                final String staffname = staffName.getText().toString().trim();
                final String collegemail = collegeMail.getText().toString().trim();
                final String Department = department.getText().toString().trim();
                final String phonenumber = phoneNumber.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String passwordcheck = passwordCheck.getText().toString().trim();
                String finalpassword = null;
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                if (TextUtils.isEmpty(staffid)) {
                    staffId.setError("Staff ID is required");
                    return;
                }
                if (TextUtils.isEmpty(staffname)) {
                    staffName.setError("Staff Name is required");
                    return;
                }
                if (TextUtils.isEmpty(Department)) {
                    department.setError("Department Name is required");
                    return;
                }
                if (TextUtils.isEmpty(collegemail)) {
                    collegeMail.setError("Mobile Number is required");
                    return;
                }
                if (phonenumber.length() < 10) {
                    phoneNumber.setError("Mobile Number Shouldn't be less than 10 Digits");
                    return;
                }
                if (collegemail.isEmpty()) {
                    collegeMail.setError("Enter college email Address");
                    isEmailValid = false;
                } else {
                    if (collegemail.matches(emailPattern)) {
                        isEmailValid = true;
                    } else {
                        isEmailValid = false;
                        collegeMail.setError("Enter College Mail ID");
                        Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_SHORT).show();
                    }

                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password Required");
                    return;
                }
                if (TextUtils.isEmpty(passwordcheck)) {
                    passwordCheck.setError("Password Required");
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Password should be greater than six letters");
                    return;
                }

                if (!Password.equals(passwordcheck)) {
                    Toast.makeText(SignUpActivity.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();

                } else {
                    finalpassword = Password;

                }


                if (isEmailValid & Password.equals(passwordcheck)) {


                    finalPassword = finalpassword;
                    fAuth.createUserWithEmailAndPassword(collegemail, finalpassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userid = fAuth.getCurrentUser().getUid();

                                Map<String, Object> usermap = new HashMap<>();
                                usermap.put("StaffId", staffid);
                                usermap.put("StaffName", staffname);
                                usermap.put("Department", Department.toUpperCase());
                                usermap.put("Email", collegemail);
                                usermap.put("Mobile", phonenumber);


                                DocumentReference documentReference = mFireStore.collection("users").document(userid);

                                documentReference.set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                                Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();

                                //send verification link
                                FirebaseUser user = fAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "Verification Email has been sent successfully", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "Error!!" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            } else {
                                Toast.makeText(SignUpActivity.this, "Error!!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    if (!Password.equals(passwordcheck)) {
                        password.setError("Password Mismatch");
                    } else {
                        collegeMail.setError("Enter College Mail ID Only");
                    }
                }

            }
        });


    }
}