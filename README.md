<pre dir="rtl">
کلاس ApiService قبل از شخصی ساز وافزودن کلاس GsonRequest
به شکل زیر می باشد
</pre>
<pre>
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
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "experts/student",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        List<Student> students = gson.fromJson(response,new TypeToken<List<Student>>(){}.getType());
                        callback.getStudentsSuccess(students);
                     //   List<Student> students = new ArrayList<>();
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject studentObject = jsonArray.getJSONObject(i);
//                                Student student = new Student();
//                                student.setId(studentObject.getInt("id"));
//                                student.setFirstName(studentObject.getString("first_name"));
//                                student.setLastName(studentObject.getString("last_name"));
//                                student.setCourse(studentObject.getString("course"));
//                                student.setScore(studentObject.getInt("score"));
//                                students.add(student);
//                            }
//                            Log.d(TAG, "onResponse: " + students.size());
//                            callback.getStudentsSuccess(students);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                callback.getStudentError(error);
            }
        });
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


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "experts/student",
                studentObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response);
                Student student = gson.fromJson(response.toString(), Student.class);
                callback.onSuccess(student);
                /**
                  * اگر از کتابخانه Gson استفاده نکنیم باید کد زیر رو بنویسیم
                  */
//                Student student = new Student();
//                try {
//                    student.setId(response.getInt("id"));
//                    student.setFirstName(response.getString("first_name"));
//                    student.setLastName(response.getString("last_name"));
//                    student.setCourse(response.getString("course"));
//                    student.setScore(response.getInt("score"));
//                    callback.onSuccess(student);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
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

</pre>
<pre dir="rtl">
بعد از ساخت کلاس GsonRequest کلاس ApiService
به شکل زیر تبدیل می شود
</pre>

<pre>
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

</pre>

<pre dir="rtl">
  در آخر  کلاس  شخصی سازی شده با کتابخانه Volley
 </pre>
 <pre>

 public class GsonRequest<T> extends Request<T> {
     private Gson gson = new Gson();
     private Type type;
     private Response.Listener<T> listener;
     private JSONObject body;

     public GsonRequest(int method, Type type, String url, @Nullable JSONObject body, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
         super(method, url, errorListener);
         this.type = type;
         this.listener = listener;
         this.body = body;
     }

     public GsonRequest(int method, Type type, String url, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
         this(method, type, url, null, listener, errorListener);
     }

     @Override
     protected Response<T> parseNetworkResponse(NetworkResponse netWorkResponse) {
         try {
             String responseInString = new String(netWorkResponse.data);
             T response = gson.fromJson(responseInString, type);
             return Response.success(response, HttpHeaderParser.parseCacheHeaders(netWorkResponse));
         } catch (Exception e) {
             return Response.error(new VolleyError(e));
         }
     }

     @Override
     protected void deliverResponse(T response) {
         listener.onResponse(response);
     }

     @Override
     public byte[] getBody() throws AuthFailureError {
         if (body == null)
             return super.getBody();
         else {
             return body.toString().getBytes();
         }
     }

     @Override
     public String getBodyContentType() {
         return "application/json";
     }

     @Override
     public Map<String, String> getHeaders() throws AuthFailureError {
         Map<String, String> headers = new HashMap<>();
         headers.put("Accept", "application/json");
         //  headers.put("keys",values) ...
         return headers;
     }
 }

 </pre>