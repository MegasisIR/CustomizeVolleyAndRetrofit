package com.example.customizevolley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class AddNewStudentActivity extends AppCompatActivity {
    private static final String TAG = "AddNewStudentActivity";

    private TextInputEditText firstNameEt;
    private TextInputEditText lastNameEt;
    private TextInputEditText scoreEt;
    private TextInputEditText courseEt;

    private ApiServiceVolley apiServiceVolley;
    private ApiServiceRetrofit apiServiceRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);
        apiServiceVolley = new ApiServiceVolley(this, TAG);
        apiServiceRetrofit = new ApiServiceRetrofit(this, TAG);
        Toolbar toolbar = findViewById(R.id.toolbar_addNewStudent);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        firstNameEt = findViewById(R.id.et_addNewStudent_firstName);
        lastNameEt = findViewById(R.id.et_addNewStudent_LastName);
        scoreEt = findViewById(R.id.et_addNewStudent_score);
        courseEt = findViewById(R.id.et_addNewStudent_course);


        View fabSaveStudent = findViewById(R.id.fab_saveStudent);
        fabSaveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstNameEt.length() > 0
                        && lastNameEt.length() > 0
                        && scoreEt.length() > 0
                        && courseEt.length() > 0) {

                    //Save Student with Retrofit
                    apiServiceRetrofit.saveStudent(
                            firstNameEt.getText().toString(),
                            lastNameEt.getText().toString(),
                            Integer.parseInt(scoreEt.getText().toString()),
                            courseEt.getText().toString(), new ApiServiceRetrofit.SaveStudentCallback() {
                                @Override
                                public void onSuccess(Student student) {
                                    Intent intent = new Intent();
                                    intent.putExtra("student", student);
                                    setResult(MainActivity.RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void onError(Throwable error) {
                                    Log.e(TAG, "onError: ", error);
                                }
                            });

                    //Save Student with Volley
                   /* apiServiceVolley.saveStudent(firstNameEt.getText().toString(),
                            lastNameEt.getText().toString(),
                            Integer.parseInt(scoreEt.getText().toString()),
                            courseEt.getText().toString(), new ApiServiceVolley.SaveStudentCallback() {
                                @Override
                                public void onSuccess(Student student) {
                                    Intent intent = new Intent();
                                    intent.putExtra("student", student);
                                    setResult(MainActivity.RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });*/

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // apiServiceVolley.cancelRequest();
    }
}
