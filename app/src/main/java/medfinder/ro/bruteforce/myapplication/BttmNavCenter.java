package medfinder.ro.bruteforce.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BttmNavCenter extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bttm_nav_center);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_center);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                startActivity(new Intent(getApplicationContext(), CenterCalendar.class));
                                break;
                            case R.id.action_profil_centru:
                                startActivity(new Intent(getApplicationContext(), CenterProfileActivity.class));
                                break;
                            case R.id.action_ntf_centru:
                                startActivity(new Intent(getApplicationContext(), CenterNotificationActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }
}

