package com.example.customizevolley;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceRetrofit {
    private final String BASE_URL = "http://expertdevelopers.ir/api/v1/";
    private RetrofitApiService apiService;
    private static final String TAG = "ApiService";

    public ApiServiceRetrofit(Context context, String requestTag) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(RetrofitApiService.class);
    }


    public void getStudent(final GetListStudentsCallback callback) {
        apiService.getStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                callback.getStudentsSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                callback.getStudentError(t);
            }
        });
    }


    public void saveStudent(String firstName,
                            String lastName,
                            int score,
                            String course, final SaveStudentCallback callback) {
/*  //org.gson
        JSONObject studentObj = new JSONObject();
        try {
            studentObj.put("first_name", firstName);
            studentObj.put("last_name", lastName);
            studentObj.put("score", score);
            studentObj.put("course", course);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //com.google.gson
        JsonObject student = new JsonObject();
        student.addProperty("first_name", firstName);
        student.addProperty("last_name", lastName);
        student.addProperty("course", course);
        student.addProperty("score", score);

        apiService.saveStudent(student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    interface SaveStudentCallback {
        void onSuccess(Student students);

        void onError(Throwable error);
    }

    interface GetListStudentsCallback {
        void getStudentsSuccess(List<Student> students);

        void getStudentError(Throwable error);
    }

    public void cancelRetrofitRequests() {

    }

}
