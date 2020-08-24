package com.example.customizevolley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApiService {
    private final String BASE_URL = "http://expertdevelopers.ir/api/v1/";
    private RequestQueue requestQueue;
    private static final String TAG = "ApiService";
    private String requestTag;
    private Gson gson;

    public ApiService(Context context, String requestTag) {
        this.requestTag = requestTag;
        this.gson = new Gson();
        if (requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public void getStudent(final GetListStudentsCallback callback) {
        GsonRequest<List<Student>> request = new GsonRequest<>(Request.Method.GET,
                new TypeToken<List<Student>>() {
                }.getType(),
                BASE_URL + "experts/student",
                new Response.Listener<List<Student>>() {
                    @Override
                    public void onResponse(List<Student> response) {
                        callback.getStudentsSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.getStudentError(error);
            }
        }
        );
        request.setTag(requestTag);
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
        GsonRequest<Student> request = new GsonRequest<>(Request.Method.POST, Student.class,
                BASE_URL + "experts/student",
                studentObj,
                new Response.Listener<Student>() {
                    @Override
                    public void onResponse(Student response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        request.setTag(requestTag);
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

    public void cancelRequest() {
        requestQueue.cancelAll(requestTag);
    }
}
