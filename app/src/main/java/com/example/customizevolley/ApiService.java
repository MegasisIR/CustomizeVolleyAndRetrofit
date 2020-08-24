package com.example.customizevolley;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiService {
    private final String BASE_URL = "http://expertdevelopers.ir/api/v1/";
    private RequestQueue requestQueue;
    private static final String TAG = "ApiService";


    public ApiService(Context context) {
        if (requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public void getStudent(final GetListStudentsCallback callback) {
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "experts/student",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        List<Student> students = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject studentObject = jsonArray.getJSONObject(i);
                                Student student = new Student();
                                student.setId(studentObject.getInt("id"));
                                student.setFirstName(studentObject.getString("first_name"));
                                student.setLastName(studentObject.getString("last_name"));
                                student.setCourse(studentObject.getString("course"));
                                student.setScore(studentObject.getInt("score"));
                                students.add(student);
                            }
                            Log.d(TAG, "onResponse: " + students.size());
                            callback.getStudentsSuccess(students);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                callback.getStudentError(error);
            }
        });
        requestQueue.add(request);

    }


    public void saveStudent(String firstName,
                            String lastName,
                            int score,
                            String course, final SaveStudentCallback callback) {

        JSONObject studentObj = new JSONObject();
        try {
            studentObj.put("first_name", firstName);
            studentObj.put("last_name", lastName);
            studentObj.put("score", score);
            studentObj.put("course", course);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "experts/student",
                studentObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response);
                Student student = new Student();
                try {
                    student.setId(response.getInt("id"));
                    student.setFirstName(response.getString("first_name"));
                    student.setLastName(response.getString("last_name"));
                    student.setCourse(response.getString("course"));
                    student.setScore(response.getInt("score"));
                    callback.onSuccess(student);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                callback.onError(error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        requestQueue.add(request);
    }


    interface SaveStudentCallback {
        void onSuccess(Student students);

        void onError(VolleyError error);
    }

    interface GetListStudentsCallback {
        void getStudentsSuccess(List<Student> students);

        void getStudentError(VolleyError error);
    }
}
