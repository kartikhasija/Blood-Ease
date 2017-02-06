package in.kartikhasija.bloodease;

import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class Locate_Donor extends AppCompatActivity implements OnMapReadyCallback {


    SupportMapFragment supportMapFragment;
    LatLng mCurrentLatlon;
    LatLng mUrlLatlon;
    ArrayList<LatLng> displayPoints=new ArrayList<>();
    ArrayList<String> names=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate__donor);

        if(ArrayConstants.current_lon !=null && ArrayConstants.current_lat !=null)
        {
            mCurrentLatlon=new LatLng(ArrayConstants.current_lat,ArrayConstants.current_lon);
        }

        if(ArrayConstants.current_location_lat!=null && ArrayConstants.current_location_long!=null)
        {
            if(ArrayConstants.current_location_lat.size()>0 && ArrayConstants.current_location_long.size()>0)
            {
                for(int i=0;i<ArrayConstants.current_location_long.size();i++)
                {
                    mUrlLatlon=new LatLng(Double.parseDouble(ArrayConstants.current_location_lat.get(i)) ,Double.parseDouble(ArrayConstants.current_location_long.get(i)));
                    double distance=distanceBetween(mCurrentLatlon,mUrlLatlon);
                    int dis= (int) distance;
                    if(dis<=400)
                    {
                        names.add(ArrayConstants.first_name.get(i));
                        displayPoints.add(new LatLng(Double.parseDouble(ArrayConstants.current_location_lat.get(i)),Double.parseDouble(ArrayConstants.current_location_long.get(i))));
                        Toast.makeText(getApplicationContext(),"selected distance->"+String.valueOf(dis),Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(),"distance->"+String.valueOf(distance),Toast.LENGTH_SHORT).show();
                }
            }
        }

        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().isMapToolbarEnabled();
        googleMap.getUiSettings().isZoomControlsEnabled();
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        for(int i=0;i<displayPoints.size();i++)
        {
            if(i==0)
            {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(displayPoints.get(i),15));
            }
            displayMarker(names.get(i),displayPoints.get(i),googleMap);
        }
    }

    public void displayMarker(String name,LatLng latLng,GoogleMap googleMap)
    {
        MarkerOptions options=new MarkerOptions();
        options.title(name);
        options.position(latLng);
        googleMap.addMarker(options);
    }

    public double distanceBetween(LatLng point_one,LatLng point_two)
    {
        if(point_one==null || point_two==null)
        {
            return Double.parseDouble(null);
        }

        return SphericalUtil.computeDistanceBetween(point_one,point_two);
    }
}