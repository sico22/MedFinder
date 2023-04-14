package medfinder.ro.bruteforce.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUp extends Activity {

    private Button AcceptBttn, DeleteBttn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, databaseReference2;
    private String hour, AnsweredRequestKey;
    private String UserName, UserKey, UserDate, CenterName, CenterKey;
    private DatabaseReference dbRefCentru;
    private String answerForRequest, Appointment, UserService;
    private int selected = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_medcenter);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        UserName = getIntent().getStringExtra("UserName");
        UserKey = getIntent().getStringExtra("UserKey");
        UserDate = getIntent().getStringExtra("UserDate");
        UserService = getIntent().getStringExtra("UserService");

        dbRefCentru = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        /**get center name**/
        dbRefCentru.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    CenterName = mUser.getName();
                    CenterKey = dataSnapshot.getKey();

                } else {
                    Toast.makeText(PopUp.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.6));

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"0:00", "1:00", "2:00", "3:00", "4:00", "5:00",
                "6:00", "7:00", "8:00", "9:00","10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour = parent.getItemAtPosition(position).toString();
                selected = 1;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AcceptBttn = (Button) findViewById(R.id.AcceptBttn);
        DeleteBttn = (Button) findViewById(R.id.DeleteBttn);

        /**accept a request**/
        AcceptBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hour != null){
                    answerForRequest = "Cererea a fost aprobata";
                    databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(UserKey);
                    databaseReference2 = databaseReference.child("answeredRequests");
                    AnsweredRequestKey = databaseReference2.push().getKey();
                    databaseReference2.child(AnsweredRequestKey)
                            .setValue(new AnsweredRequests(CenterName, CenterKey, UserDate, hour, UserService, answerForRequest));
                    dbRefCentru.child("Appointments").child(UserDate).push().setValue(new Appointments(UserName, UserService, UserDate, hour));
                }
                else{
                    Toast.makeText(PopUp.this, "You didn't choose an hour.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        /**reject a request**/
        DeleteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerForRequest = "Cererea nu a fost aprobata";
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(UserKey);
                databaseReference2 = databaseReference.child("answeredRequests");
                AnsweredRequestKey = databaseReference2.push().getKey();
                databaseReference2.child(AnsweredRequestKey)
                        .setValue(new AnsweredRequests(CenterName, CenterKey, UserDate, hour, UserService, answerForRequest));
                finish();
            }
        });
    }
}
