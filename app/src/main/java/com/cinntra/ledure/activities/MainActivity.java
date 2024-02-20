package com.cinntra.ledure.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.cinntra.ledure.R;
import com.cinntra.ledure.fragments.Calender;
import com.cinntra.ledure.fragments.Dashboard_BToC;
import com.cinntra.ledure.fragments.Graph_;
import com.cinntra.ledure.fragments.Settings;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixplicity.easyprefs.library.Prefs;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MainBaseActivity implements FragmentRefresher {


  @BindView(R.id.new_contact)
  FloatingActionButton add_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Globals.CURRENT_CLASS = getClass().getName();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 25);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


        loadFragment(new Dashboard_BToC());
       BottomNavigationView navigationView = findViewById(R.id.navigationView);

        navigationView.setBackground(null);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
           {
       Fragment fragment = null;
       switch (item.getItemId()){
       case R.id.home:
           fragment = new Dashboard_BToC();
           break;
       case R.id.graph:
           fragment = new Graph_();
           break;
       case R.id.notification:
            fragment = new Calender();
           break;
       case R.id.settings:
            fragment = new Settings();

            break;
       /*case R.id.add_contact:
        break;*/
            }
        return loadFragment(fragment);
            }
        });


        add_contact.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,AddContact.class);
                Prefs.putString(Globals.AddContactPerson,"Dashboard");

                startActivity(intent);
         }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if(Globals.CURRENT_CLASS.equalsIgnoreCase(fragment.getClass().getName())){
            return false;
        }
        //switching fragment
        if (fragment != null) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, fragment)

            .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onRefresh() {
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}