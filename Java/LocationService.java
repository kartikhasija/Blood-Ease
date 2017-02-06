package in.kartikhasija.bloodease;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kartik on 27-Jun-16.
 */
public class LocationService extends IntentService {

    String status;
    Handler handler=new Handler();
    Runnable runnable;
    GoogleApiClient apiClient;
    Location mylocation;

    public LocationService() {
        super("");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runnable=new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();

                apiClient=new GoogleApiClient.Builder(getApplicationContext()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                        mylocation=LocationServices.FusedLocationApi.getLastLocation(apiClient);
                        if (mylocation != null) {
                            String lat= String.valueOf(mylocation.getLatitude());
                            String lon= String.valueOf(mylocation.getLongitude());
                            updateLocation(lat,lon);
                        }
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                }).addApi(LocationServices.API).build();
                apiClient.connect();
                handler.postDelayed(runnable,5000);
            }
        };

        handler.postDelayed(runnable,2000);
        return START_STICKY;
    }

    public void updateLocation(String lat, String lon){
        String url="http://kartikhasija.in/bloodease/ucl.php?user_id="+Log_In.user_id+"&current_location_lat="+lat+"&current_location_long="+lon;
        Log.e(">>",""+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    status = response.getString("status");

                    if (status.equals("done")) {
                        Log.e(">>","done");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        handler.removeCallbacks(runnable);
    }
}
