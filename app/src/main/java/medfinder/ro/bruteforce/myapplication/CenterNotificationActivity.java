package medfinder.ro.bruteforce.myapplication;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.List;

public class CenterNotificationActivity extends AppCompatActivity {

    private ListView myListViewCenter;
    private FirebaseAuth firebaseAuth;
    private ItemAdapterReceivedRequests adapter;
    private ProgressDialog mProgressDialog;
    private String CenterName, CenterKey;
    private String hour, AnsweredRequestKey;
    private int selected = 0;
    private DatabaseReference dbReference, dbRefCentru, databaseReference2, databaseReference;
    private Button AcceptBttn, DeleteBttn;
    private TextView TextViewNtfCenter;
    private ArrayList<ReceivedRequests> receivedRequests = new ArrayList<>();
    private ArrayList<String> requestKeys = new ArrayList<String>();
    private String answerForRequest, Appointment, UserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_notification);


        myListViewCenter = (ListView) findViewById(R.id.myListViewCenter);
        firebaseAuth = FirebaseAuth.getInstance();

        /**make a list of receivedRequests**/
        adapter = new ItemAdapterReceivedRequests(this, receivedRequests);
        myListViewCenter.setAdapter(adapter);

        TextViewNtfCenter = (TextView) findViewById(R.id.TextViewNtfCenter);

        if(firebaseAuth.getCurrentUser() == null){
            //closing the activity
            finish();
            //start login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user.getUid()).child("receivedRequests");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ReceivedRequests request = snapshot.getValue(ReceivedRequests.class);
                        receivedRequests.add(request);
                        adapter.notifyDataSetChanged();
                        requestKeys.add(snapshot.getKey());
                        TextViewNtfCenter.setVisibility(View.INVISIBLE);
                        showProgressDialog();
                    }

                }
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });


        /** answer a request**/
        myListViewCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder PopupBuilder = new AlertDialog.Builder(CenterNotificationActivity.this);
                View PopupView = getLayoutInflater().inflate(R.layout.popup_medcenter, null);
                PopupBuilder.setView(PopupView);
                final AlertDialog dialog = PopupBuilder.create();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                final ReceivedRequests request = receivedRequests.get(position);
                dbRefCentru = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

                dbRefCentru.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            // user exists
                            UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                            CenterName = mUser.getName();
                            CenterKey = dataSnapshot.getKey();

                        } else {
                            Toast.makeText(CenterNotificationActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width = dm.widthPixels;
                int height = dm.heightPixels;

                getWindow().setLayout((int)(width*.9),(int)(height*.6));*/
                Spinner dropdown = PopupView.findViewById(R.id.spinner1);
                String[] items = new String[]{"0:00", "1:00", "2:00", "3:00", "4:00", "5:00",
                        "6:00", "7:00", "8:00", "9:00","10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
                        "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(CenterNotificationActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter2);

                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        hour = parent.getItemAtPosition(position).toString();
                        selected = 1;
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                AcceptBttn = (Button) PopupView.findViewById(R.id.AcceptBttn);
                DeleteBttn = (Button) PopupView.findViewById(R.id.DeleteBttn);

                /**accept a request**/
                AcceptBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String UserName = request.getReceivedUserName();
                        String UserKey = request.getReceivedUserKey();
                        String UserDate = request.getReceivedUserDate();
                        String UserService = request.getReceivedUserService();
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
                            Toast.makeText(CenterNotificationActivity.this, "You didn't choose an hour.", Toast.LENGTH_SHORT).show();
                        }
                        receivedRequests.remove(position);
                        dbReference.child(requestKeys.get(position)).removeValue();
                        requestKeys.remove(position);
                        adapter.notifyDataSetChanged();
                        if(receivedRequests.size() == 0){
                            TextViewNtfCenter.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                });

                /**reject a request**/
                DeleteBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String UserName = request.getReceivedUserName();
                        String UserKey = request.getReceivedUserKey();
                        String UserDate = request.getReceivedUserDate();
                        String UserService = request.getReceivedUserService();
                        answerForRequest = "Cererea nu a fost aprobata";
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(UserKey);
                        databaseReference2 = databaseReference.child("answeredRequests");
                        AnsweredRequestKey = databaseReference2.push().getKey();
                        databaseReference2.child(AnsweredRequestKey)
                                .setValue(new AnsweredRequests(CenterName, CenterKey, UserDate, hour, UserService, answerForRequest));
                        receivedRequests.remove(position);
                        dbReference.child(requestKeys.get(position)).removeValue();
                        requestKeys.remove(position);
                        adapter.notifyDataSetChanged();
                        if(receivedRequests.size() == 0){
                            TextViewNtfCenter.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                /*Intent myIntent = new Intent(CenterNotificationActivity.this,PopUp.class);

                myIntent.putExtra("UserName", request.getReceivedUserName());
                myIntent.putExtra("UserKey", request.getReceivedUserKey());
                myIntent.putExtra("UserDate", request.getReceivedUserDate());
                myIntent.putExtra("UserService", request.getReceivedUserService());
                startActivity(myIntent);*/

            }
        });

        /**show no notifications text**/
        if(receivedRequests.size() == 0){
            TextViewNtfCenter.setVisibility(View.VISIBLE);
        }
        else{

        }

        /**bottom navigation view**/
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_center);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                startActivity(new Intent(CenterNotificationActivity.this, CenterCalendar.class));
                                break;
                            case R.id.action_profil_centru:
                                startActivity(new Intent(CenterNotificationActivity.this, CenterProfileActivity.class));
                                break;
                            case R.id.action_ntf_centru:

                                break;
                        }
                        return false;
                    }
                });
    }

    /**functions**/
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(CenterNotificationActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
}
