package medfinder.ro.bruteforce.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CenterCalendar extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CompactCalendarView calendarViewCenter;
    private int i ;
    private Button SubmitBttn;
    private TextView MonthTextCenter, NewNameEditText, NewServiceEditText, NewDateEditText, NewHourEditText;
    private Date selectedDate;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public void UserInfo(final String userId){
        /**veirfy if the user is actually pacient**/

        databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    i = mUser.getIdentity();

                    if (i == 0){
                        finish();
                        startActivity(new Intent(CenterCalendar.this, ListActivity.class));
                    }

                } else {
                    Toast.makeText(CenterCalendar.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_calendar);

        firebaseAuth = FirebaseAuth.getInstance();

        MonthTextCenter = (TextView)findViewById(R.id.MonthTextCenter);

        calendarViewCenter = (CompactCalendarView) findViewById(R.id.calendarViewCenter);

        calendarViewCenter.setFirstDayOfWeek(Calendar.MONDAY);
        calendarViewCenter.setCurrentDayTextColor(R.color.colorAccentShade);
        calendarViewCenter.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.colorAccentShade));
        MonthTextCenter.setText(dateFormatForMonth.format(calendarViewCenter.getFirstDayOfCurrentMonth()));

        if(firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            UserInfo(user.getUid());
        }


        /**new appointment**/
        FloatingActionButton NewAppointmentActionButton = findViewById(R.id.NewAppointmentActionButton);
        NewAppointmentActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder NewBuilder = new AlertDialog.Builder(CenterCalendar.this);
                View NewView = getLayoutInflater().inflate(R.layout.dialog_new_appointment,null);
                NewBuilder.setView(NewView);
                final AlertDialog dialog = NewBuilder.create();
                NewNameEditText = (EditText)NewView.findViewById(R.id.NewNameEditText);
                NewServiceEditText = (EditText)NewView.findViewById(R.id.NewServiceEditText);
                NewDateEditText = (EditText)NewView.findViewById(R.id.NewDateEditText);
                NewHourEditText = (EditText)NewView.findViewById(R.id.NewHourEditText);
                SubmitBttn = (Button)NewView.findViewById(R.id.SubmitButton);


                SubmitBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = NewNameEditText.getText().toString();
                        String service = NewServiceEditText.getText().toString();
                        String date = NewDateEditText.getText().toString();
                        String hour = NewHourEditText.getText().toString();
                        databaseReference.child("Appointments").child(NewDateEditText.getText().toString())
                                .push().setValue(new Appointments(name, service, date, hour));
                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        });

        /**click listeners**/
        calendarViewCenter.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectedDate = dateClicked;
                Intent myIntent = new Intent(CenterCalendar.this, AppointmentsForTheDay.class);
                myIntent.putExtra("selectedDate", dateFormatForDisplaying.format(dateClicked));
                startActivity(myIntent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                /*toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));*/
                MonthTextCenter.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        /**bottm navigationview**/
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_center);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:

                                break;
                            case R.id.action_profil_centru:
                                startActivity(new Intent(CenterCalendar.this, CenterProfileActivity.class));
                                break;
                            case R.id.action_ntf_centru:
                                startActivity(new Intent(CenterCalendar.this, CenterNotificationActivity.class));
                                break;
                        }
                        return false;
                    }
                });
    }
}
