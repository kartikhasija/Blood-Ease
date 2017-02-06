package in.kartikhasija.bloodease;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class SpleshScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {



    GoogleApiClient apiClient;
    Location MY_CURRENT_LOCATION;
    int REQUESTCODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh_screen);

        apiClient = new GoogleApiClient.Builder(SpleshScreen.this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
        apiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(SpleshScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SpleshScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SpleshScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(SpleshScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                Toast.makeText(getApplicationContext(), "App need GPS", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SpleshScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }

        }

        if (!isGpsAvalaible()) {
            myAlert();
        } else {
            MY_CURRENT_LOCATION = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (MY_CURRENT_LOCATION != null) {
                Log.e("mylocation>>>>", String.valueOf(MY_CURRENT_LOCATION));
                ArrayConstants.current_lat = MY_CURRENT_LOCATION.getLatitude();
                ArrayConstants.current_lon = MY_CURRENT_LOCATION.getLongitude();
                runTheThread();
                Toast.makeText(SpleshScreen.this, ">>>" + String.valueOf(MY_CURRENT_LOCATION), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isGpsAvalaible() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsOn;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), " not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUESTCODE)
        {
            if(resultCode==RESULT_OK)
            {
                if(data.getData()!=null)
                {
                    apiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
                    apiClient.connect();
                }
            }
            else
            {
                myAlert();
            }
        }

    }

    public  void myAlert()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SpleshScreen.this);
        dialog.setTitle("Use location");
        dialog.setMessage("this app wants to change your device settings");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,REQUESTCODE);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    public  void runTheThread()
    {
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(4000);

                   SharedPreferences preferences=getSharedPreferences("login", Context.MODE_PRIVATE);
                    String status=preferences.getString("status","");
                    Log.e(">>>","status at splash  >> "+ status);
                    if(status!=null && status.equals("done"))
                    {
                        startActivity(new Intent(SpleshScreen.this,Main_Menu.class));
                    }
                    else {

                        Intent intent = new Intent(SpleshScreen.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t.start(); 
    }
    
}
