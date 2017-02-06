package in.kartikhasija.bloodease;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Menu extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    GoogleApiClient apiClient;
    Location MY_CURRENT_LOCATION;

    FloatingActionButton button_find_donor,button_donate_blood_now,button_need_blood_regular;
    Boolean exit=false;
    NavigationView  navigationView;
    FragmentManager manager;
    FragmentTransaction transaction;
    DrawerLayout  drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        startService(new Intent(Main_Menu.this,LocationService.class));
        manager=getSupportFragmentManager();

        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);


        View view=navigationView.getHeaderView(0);
        TextView username= (TextView) view.findViewById(R.id.user_name_navigation);
        TextView usereamil= (TextView) view.findViewById(R.id.user_email_navigation);
        CircleImageView imageView= (CircleImageView) view.findViewById(R.id.header_image);
        Picasso.with(Main_Menu.this).load(Log_In.full_path).into(imageView);
        username.setText(Log_In.f_name);
        usereamil.setText(Log_In.email_id);

        navigationView.setNavigationItemSelectedListener(this);
        button_find_donor=(FloatingActionButton)findViewById(R.id.find_donor_main_menu);
        button_donate_blood_now=(FloatingActionButton)findViewById(R.id.donate_blood_now_main_menu);
        button_need_blood_regular=(FloatingActionButton)findViewById(R.id.need_blood_regular_main_menu);

        button_find_donor.setOnClickListener(this);
        button_donate_blood_now.setOnClickListener(this);
        button_need_blood_regular.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.find_donor_main_menu){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main_Menu.this);
            builder.setMessage("Select Blood Group");

            Intent intent = new Intent(Main_Menu.this,Find_Donor.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.contact_us_navigation_menu){
            manager.beginTransaction().replace(R.id.container,new Contact_Us_Fragment()).commit();
            drawerLayout.closeDrawers();
        }
        if(item.getItemId()==R.id.logout)
        {
            SharedPreferences preferences=getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();


            if(preferences==null)
            {
                stopService(new Intent(Main_Menu.this,LocationService.class));
                startActivity(new Intent(Main_Menu.this,Log_In.class));
            }
        }
        else if (item.getItemId()==R.id.submit_query_navigation_menu){
            manager.beginTransaction().replace(R.id.container,new Submit_A_Query_Fragment()).commit();
            drawerLayout.closeDrawers();
        }
        return false;
    }
}