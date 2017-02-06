package in.kartikhasija.bloodease;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_Up extends AppCompatActivity {


    EditText editText_f_name,editText_l_name,editText_email_id,editText_mobile_number,editText_password,editText_c_password;
    Spinner spinner_blood_group;
    CheckBox checkBox_willing_to_donate,checkBox_term_and_conditions;
    Button button_sign_up;
    public static String f_name,l_name,email_id,mobile_number,password,c_password,blood_group,willing_to_donate;
    public static Boolean term_and_condition;
    boolean ccc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Find Id's
        editText_f_name=(EditText)findViewById(R.id.f_name_sign_up);
        editText_l_name=(EditText)findViewById(R.id.l_name_sign_up);
        editText_email_id=(EditText)findViewById(R.id.email_id_sign_up);
        editText_mobile_number=(EditText)findViewById(R.id.mobile_number_sign_up);
        editText_password=(EditText)findViewById(R.id.password_sign_up);
        editText_c_password=(EditText)findViewById(R.id.c_password_sign_up);
        spinner_blood_group=(Spinner)findViewById(R.id.blood_groups_sign_up);
        checkBox_willing_to_donate=(CheckBox)findViewById(R.id.willing_to_donate_sign_up);
        checkBox_term_and_conditions=(CheckBox)findViewById(R.id.term_and_conditions_sign_up);
        button_sign_up=(Button)findViewById(R.id.sign_up_sign_up);


//        Click Listener's
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_name=editText_f_name.getText().toString().trim();
                l_name=editText_l_name.getText().toString().trim();
                email_id=editText_email_id.getText().toString().trim();
                mobile_number=editText_mobile_number.getText().toString().trim();
                password=editText_password.getText().toString().trim();
                c_password=editText_c_password.getText().toString().trim();
                blood_group=spinner_blood_group.getSelectedItem().toString();
                term_and_condition=checkBox_term_and_conditions.isChecked();

                if(isNetworkAvailable())
                {
                    if(checkBox_willing_to_donate.isChecked()) {
                        willing_to_donate = "yes";
                    }
                    else {
                        willing_to_donate = "no";
                    }



                    if(f_name.equals("")||l_name.equals("")||email_id.equals("")||mobile_number.equals("")||password.equals("")||c_password.equals("")){
                        Toast.makeText(getApplication(),"All the fields are compulsory",Toast.LENGTH_LONG).show();
                    }

                    else if(mobile_number.length()!=10){
                        Toast.makeText(getApplicationContext(),"Please Enter Correct 10 Digit Mobile No",Toast.LENGTH_LONG).show();
                    }

                    else if(password.length()<8){
                        Toast.makeText(getApplication(),"Password length must be greater then 8",Toast.LENGTH_LONG).show();
                    }

                    else if(!password.equals(c_password)){
                        Toast.makeText(getApplication(),"Confirm Password's dose not Math",Toast.LENGTH_LONG).show();
                    }

                    else if((blood_group.equals("Select Blood Group"))){
                        Toast.makeText(getApplication(),"Please Select Blood Group",Toast.LENGTH_LONG).show();
                    }

                    else if(term_and_condition==false){
                        Toast.makeText(getApplication(),"You must agree to out terms and conditions",Toast.LENGTH_LONG).show();
                    }

                    else if(willing_to_donate=="no") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_Up.this);
                        builder.setMessage("Are You really don't willing to Donate Blood");
                        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                                } else {
                                    String url = "http://kartikhasija.in/bloodease/register.php?fname=" + f_name + "&lname=" + l_name + "&password=" + password + "&email=" + email_id + "&mobile_no=" + mobile_number + "&blood_group=" + blood_group + "&donar_status=" + willing_to_donate;

                                    Log.e(">>", url);
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                String status = response.getString("status");

                                                if (status.equals("done")) {
                                                    Intent intent = new Intent(Sign_Up.this, Main_Menu.class);
                                                    startActivity(intent);
                                                    Toast.makeText(getApplicationContext(), "Welcome To Blood Ease", Toast.LENGTH_LONG).show();
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

                                    RequestQueue queue = Volley.newRequestQueue(Sign_Up.this);
                                    queue.add(request);
                                }
                            }
                        });
                        builder.show();
                    }

                    else {

                        if (!isNetworkAvailable()) {
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            String url = "http://kartikhasija.in/bloodease/register.php?fname=" + f_name + "&lname=" + l_name + "&password=" + password + "&email=" + email_id + "&mobile_no=" + mobile_number + "&blood_group=" + blood_group + "&donar_status=" + willing_to_donate;

                            Log.e(">>", url);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                    Log.e("response", "" + response);
                                    try {
                                        String status = response.getString("status");

                                        if (status.equals("done")) {
                                            Intent intent = new Intent(Sign_Up.this, Log_In.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Welcome To Blood Ease /nPlease Verify Your Email Id and Log In", Toast.LENGTH_LONG).show();
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

                            RequestQueue queue = Volley.newRequestQueue(Sign_Up.this);
                            queue.add(request);
                        }
                    }
                }
                else
                {
                    Toast.makeText(Sign_Up.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo Info = connectivityManager.getActiveNetworkInfo();

        boolean isConnected=Info!=null && Info.isConnectedOrConnecting() && Info.isAvailable();

        return isConnected;
    }

}