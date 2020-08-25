package com.example.customizevolley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ProgressBar progressBar;
    private ApiServiceVolley apiServiceVolley;
    private final String BASE_URL = "http://expertdevelopers.ir/api/v1/";
    private ApiServiceRetrofit apiServiceRetrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiServiceVolley = new ApiServiceVolley(this, TAG);
        recyclerView = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.progress_main);
        apiServiceRetrofit = new ApiServiceRetrofit(this,TAG);

        // get All Student with Retrofit
            apiServiceRetrofit.getStudent(new ApiServiceRetrofit.GetListStudentsCallback() {
                @Override
                public void getStudentsSuccess(List<Student> students) {
                    adapter = new ContactAdapter(students);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void getStudentError(Throwable error) {
                    Toast.makeText(MainActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });
        // get All Student with Volley
       /* apiServiceVolley.getStudent(new ApiServiceVolley.GetListStudentsCallback() {
            @Override
            public void getStudentsSuccess(List<Student> students) {
                adapter = new ContactAdapter(students);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void getStudentError(VolleyError error) {
                Toast.makeText(MainActivity.this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
            }
        });*/

        View fabAddStudent = findViewById(R.id.fab_main_addNewStudent);
        fabAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewStudentActivity.class);
                startActivityForResult(intent, 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == MainActivity.RESULT_OK && data != null) {

            Student student = new Student();
            student = data.getParcelableExtra("student");

            adapter.add(student);
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiServiceVolley.cancelRequest();
    }
}
