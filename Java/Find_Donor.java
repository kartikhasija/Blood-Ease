package in.kartikhasija.bloodease;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Find_Donor extends AppCompatActivity{

    Spinner spinner_blood_group;
    String blood_group,url,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donner);
        spinner_blood_group=(Spinner)findViewById(R.id.blood_groups_find_donor);
        spinner_blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood_group = spinner_blood_group.getSelectedItem().toString();

                if (blood_group.equals("Select Blood Group")) {

                }
                else {
                    url = "http://kartikhasija.in/bloodease/fd.php?blood_group=" + blood_group;
                    Log.e(">>", "" + url);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                status = response.getString("status");
                                if (status.equals("done")) {

                                    JSONArray jsonArray = response.getJSONArray("items");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                                        String name = jsonObject.getString("first_name");
                                        ArrayConstants.first_name.add(name);

                                        String user_id = jsonObject.getString("user_id");
                                        ArrayConstants.user_id.add(user_id);

                                        String email_id = jsonObject.getString("email_id");
                                        ArrayConstants.email_id.add(email_id);

                                        String mobile_number = jsonObject.getString("mobile_number");
                                        ArrayConstants.mobile_number.add(mobile_number);

                                        String blood_group = jsonObject.getString("blood_group");
                                        ArrayConstants.blood_group.add(blood_group);

                                        String can_donate = jsonObject.getString("can_donate");
                                        ArrayConstants.can_donate.add(can_donate);

                                        String profile_picture = jsonObject.getString("profile_picture");
                                        ArrayConstants.profile_picture.add(profile_picture);

                                        String current_location_long = jsonObject.getString("current_location_long");
                                        ;
                                        ArrayConstants.current_location_long.add(current_location_long);

                                        String current_location_lat = jsonObject.getString("current_location_lat");
                                        ;
                                        ArrayConstants.current_location_lat.add(current_location_lat);

                                    }

                                    startActivity(new Intent(Find_Donor.this, Locate_Donor.class));
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

                    RequestQueue queue = Volley.newRequestQueue(Find_Donor.this);
                    queue.add(request);
                }
                }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){

                }
               });
    }
}
