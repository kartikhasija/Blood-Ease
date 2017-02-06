package in.kartikhasija.bloodease;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Log_In extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email_id, editText_password;
    Button button_log_in;
    public static String full_path, email_id, password, status, f_name, user_id, mobile_number, date_of_birth, blood_group, willing_to_donate, profile_picture, verified;
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        editText_email_id = (EditText) findViewById(R.id.email_id_log_in);
        editText_password = (EditText) findViewById(R.id.password_log_in);
        button_log_in = (Button) findViewById(R.id.log_in_log_in);
        button_log_in.setOnClickListener(this);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void updateLocation() {
        String url = "http://kartikhasija.in/bloodease/ucl.php?user_id=" + user_id + "&current_location_lat=" + ArrayConstants.current_lat + "&current_location_long=" + ArrayConstants.current_lon;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    status = response.getString("status");

                    if (status.equals("done")) {
                        Intent intent = new Intent(Log_In.this, Main_Menu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Hello !" + f_name, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internal Server Error ", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(Log_In.this);
        queue.add(request);
    }

    public boolean isGpsAvalaible() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsOn;
    }

    @Override
    public void onClick(View v) {

        email_id = editText_email_id.getText().toString().trim();
        password = editText_password.getText().toString().trim();

        if (email_id.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your Email Id and Password", Toast.LENGTH_LONG).show();
        } else if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        } else if (!isGpsAvalaible()) {
            Toast.makeText(Log_In.this, "Please switch on your Gps", Toast.LENGTH_SHORT).show();
        } else {


            if (Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
                String url = "http://kartikhasija.in/bloodease/log_in.php?email_id=" + email_id + "&password=" + password;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status = response.getString("status");
                            JSONArray jsonArray = response.getJSONArray("items");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            user_id = jsonObject.getString("user_id");
                            f_name = jsonObject.getString("first_name");
                            email_id = jsonObject.getString("email_id");
                            mobile_number = jsonObject.getString("mobile_number");
                            date_of_birth = jsonObject.getString("date_of_birth");
                            blood_group = jsonObject.getString("blood_group");
                            willing_to_donate = jsonObject.getString("willing_to_donate");

                            profile_picture = jsonObject.getString("profile_picture");
                            full_path = "http://kartikhasija.in/bloodease/uploads/" + profile_picture;
                            verified = jsonObject.getString("verified");

                            if (status.equals("done") && verified.equals("yes")) {

                                SharedPreferences preferences=getSharedPreferences("login",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("status",status);
                                editor.commit();

                                updateLocation();
                            } else if (verified.equals("no")) {
                                Toast.makeText(getApplicationContext(), "Please Verify Your Email Id", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Internal Server Error ", Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(Log_In.this);
                queue.add(request);
            } else if (Patterns.PHONE.matcher(email_id).matches()) {
                String url = "http://kartikhasija.in/bloodease/log_in.php?mobile_number=" + email_id + "&password=" + password;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status = response.getString("status");
                            JSONArray jsonArray = response.getJSONArray("items");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            user_id = jsonObject.getString("user_id");
                            f_name = jsonObject.getString("first_name");
                            email_id = jsonObject.getString("email_id");
                            mobile_number = jsonObject.getString("mobile_number");
                            date_of_birth = jsonObject.getString("date_of_birth");
                            blood_group = jsonObject.getString("blood_group");
                            willing_to_donate = jsonObject.getString("willing_to_donate");

                            profile_picture = jsonObject.getString("profile_picture");
                            full_path = "http://kartikhasija.in/bloodease/uploads/" + profile_picture;
                            verified = jsonObject.getString("verified");

                            if (status.equals("done") && verified.equals("yes")) {

                                SharedPreferences preferences=getSharedPreferences("login",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("status",status);
                                editor.commit();

                                updateLocation();
                            } else if (verified.equals("no")) {
                                Toast.makeText(getApplicationContext(), "Please Verify Your Email Id", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Internal Server Error ", Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(Log_In.this);
                queue.add(request);
            } else {
                Toast.makeText(getApplicationContext(), "Please Enter Valid Email Id Or Mobile Number", Toast.LENGTH_LONG).show();
            }
        }

    }
}