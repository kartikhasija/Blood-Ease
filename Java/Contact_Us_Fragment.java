package in.kartikhasija.bloodease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kartik on 27-Jun-16.
 */
public class Contact_Us_Fragment extends Fragment implements  OnMapReadyCallback, View.OnClickListener {

    SupportMapFragment supportMapFragment;
    TextView textView_call,textView_email_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_contact_us,container,false);

        supportMapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_contact_us);
        supportMapFragment.getMapAsync(this);
        textView_call= (TextView) view.findViewById(R.id.contact_us_call);
        textView_email_id= (TextView) view.findViewById(R.id.contact_us_email_id);
        textView_call.setOnClickListener(this);
        textView_email_id.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.contact_us_call){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:7206509091"));
            startActivity(intent);
        }

        if(v.getId()==R.id.contact_us_email_id){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"admin@kartikhasija.in"});
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(29.834055, 74.981996)).title("KHAS"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.834055, 74.981996),15));
    }
}