package medfinder.ro.bruteforce.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class AppointmentsForTheDay extends AppCompatActivity {

    private TextView appointmentsForTextView;
    private ListView myAppointmentsListView;
    private DatabaseReference dbRefAppointments;
    private FirebaseAuth firebaseAuth;
    private AppointmentsAdapter adapter;
    private FirebaseUser user;
    private String currentDate;
    private ArrayList<Appointments> appointments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_for_the_day);

        appointmentsForTextView = (TextView)findViewById(R.id.appointmentsForTextView);
        myAppointmentsListView = (ListView)findViewById(R.id.myAppointmentsListView);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        currentDate = getIntent().getStringExtra("selectedDate");
        appointmentsForTextView.setText(currentDate);

        adapter = new AppointmentsAdapter(this, appointments);
        myAppointmentsListView.setAdapter(adapter);


        /**get reference for the appintmentsfortheday**/
        dbRefAppointments = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user.getUid()).child("Appointments").child(currentDate);

        dbRefAppointments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Appointments appointment = snapshot.getValue(Appointments.class);
                        appointments.add(appointment);
                        adapter.notifyDataSetChanged();

                    }
                }else {
                    Toast.makeText(AppointmentsForTheDay.this, "You have no appointments for the day", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
