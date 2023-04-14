package medfinder.ro.bruteforce.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;

public class BttmNavBar extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bttm_nav_bar);

            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_centre:
                                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                                    break;
                                case R.id.action_profil:
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    break;
                                case R.id.action_notification:
                                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                                    break;
                            }
                            return true;
                        }
                    });
        }
}
