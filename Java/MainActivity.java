package in.kartikhasija.bloodease;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient apiClient;
    Button log_in,sign_up;
    Location MY_CURRENT_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
        apiClient.connect();

        log_in=(Button)findViewById(R.id.log_in_main);
        sign_up=(Button)findViewById(R.id.sign_up_main);

        log_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.log_in_main){
            Intent intent=new Intent(MainActivity.this,Log_In.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.sign_up_main){
            Intent intent=new Intent(MainActivity.this,Sign_Up.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                Toast.makeText(getApplicationContext(), "App need GPS", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }

        }

        if (!isGpsAvalaible()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Use location");
            dialog.setMessage("this app wants to change your device settings");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,1);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        } else {

            MY_CURRENT_LOCATION = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (MY_CURRENT_LOCATION != null) {
                Log.e("mylocation>>>>", String.valueOf(MY_CURRENT_LOCATION));
                ArrayConstants.current_lat = MY_CURRENT_LOCATION.getLatitude();
                ArrayConstants.current_lon = MY_CURRENT_LOCATION.getLongitude();
                Toast.makeText(MainActivity.this, ">>>" + String.valueOf(MY_CURRENT_LOCATION), Toast.LENGTH_SHORT).show();
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
}