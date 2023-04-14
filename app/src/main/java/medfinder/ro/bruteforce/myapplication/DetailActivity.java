package medfinder.ro.bruteforce.myapplication;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import android.widget.RemoteViews;


public class DetailActivity extends AppCompatActivity {

    private long number;
    boolean pus = true;
    private int usersThatVoted;
    private CompactCalendarView calendarView;
    private String  centruName, centruStringKey, userName, Appointment;
    private TextView myDate, centruTextView;
    private Button SaveMyDateBttn, ServicesBttn2;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRefCentru, dbRefUser;
    private DatabaseReference dbRefUserSentRequests, dbRefCentruReceivedRequests, ratingRef, dbRefUser2,
            dbRefVotedBy,dbRefVotedByNr;
    private String SentRequestKey, ReceivedRequestKey;
    private Toast toast;
    private float ratingNumber, actualRating;
    private String[] ServicesList2;
    private boolean[] ServicesChecked2;
    private ArrayList<String> servicesToSaveInFirebase;
    private ArrayList<Integer> ServicesCenter2 = new ArrayList<>();
    private ActionBar toolbar;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private TextView MonthText;
    private String item;
    private Date selectedDate;
    private RatingBar ratingBar;

    public static class UserSentRequest {

            public String requestedCenterName;
            public String requestedCenterKey;
            public String requestedCenterDate;
            public String requestedCenterService;

            public UserSentRequest(String requestedCenterName, String requestedCenterKey, String requestedCenterDate, String requestedCenterService) {
                this.requestedCenterDate = requestedCenterDate;
                this.requestedCenterKey = requestedCenterKey;
                this.requestedCenterName = requestedCenterName;
                this.requestedCenterService = requestedCenterService;
            }


        }

    public static class CenterReceivedRequest {

        public String receivedUserName;
        public String receivedUserKey;
        public String receivedUserDate;


        public CenterReceivedRequest(String receivedUserName, String receivedUserKey, String receivedUserDate) {
            this.receivedUserDate = receivedUserDate;
            this.receivedUserKey = receivedUserKey;
            this.receivedUserName = receivedUserName;
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /**declaration**/
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);
        centruTextView = (TextView) findViewById(R.id.centruTextView);

        ratingBar= (RatingBar) findViewById(R.id.rat);

        SaveMyDateBttn = (Button) findViewById(R.id.SaveMyDateBttn);
        MonthText = (TextView) findViewById(R.id.MonthText);

        centruStringKey = getIntent().getStringExtra("CenterUser");

        dbRefCentru = FirebaseDatabase.getInstance().getReference().child("Users").child(centruStringKey);
        dbRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        dbRefUserSentRequests = dbRefUser.child("sentRequests");
        dbRefCentruReceivedRequests = dbRefCentru.child("receivedRequests");

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        MonthText.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));


        ratingRef = dbRefCentru.child("ratingCentru");
        dbRefUser2 = dbRefUser.child("ratedByUser").child(centruStringKey);

        /**see if the user already voted else actualize the number of users that voted**/
        dbRefVotedBy = dbRefCentru.child("votedBy");
        dbRefUser2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final int alMeu;
                if(dataSnapshot.getValue() != null){
                    RatingAdap ratingAdap = dataSnapshot.getValue(RatingAdap.class);
                    ratingBar.setRating((float)ratingAdap.getRatingCentru());
                }
                else{
                    ratingNumber = ratingBar.getRating();
                    dbRefVotedBy.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            if(dataSnapshot2.getValue() != null && pus == true) {
                                VotedBy votedBy = dataSnapshot2.getValue(VotedBy.class);
                                dbRefVotedByNr = dbRefVotedBy.child("nr");
                                dbRefVotedByNr.setValue(votedBy.getNr() + 1);
                                usersThatVoted = votedBy.getNr() + 1;
                                pus = false;
                            }
                            else if(dataSnapshot2.getValue() == null){
                                dbRefVotedByNr = dbRefVotedBy.child("nr");
                                dbRefVotedByNr.setValue(1);
                                pus = false;
                                usersThatVoted = 1;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError2) {

                        }
                    });

                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            if (fromUser){
                                /**get the actual rating of the center**/
                                dbRefCentru.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                        if(dataSnapshot2.getValue() != null) {
                                            UserInformation mUser = dataSnapshot2.getValue(UserInformation.class);
                                            actualRating = (float)mUser.getRatingCentru();
                                        }
                                        else{
                                            actualRating = 0.0f;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError2) {

                                    }
                                });
                                //actualRating = (actualRating + rating);
                                //actualRating = (float)(actualRating/usersThatVoted);
                                actualRating += rating;
                                ratingRef.setValue((float)(actualRating / usersThatVoted));
                                dbRefUser2.child("ratingCentru").setValue(rating);
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**get centru name**/
        dbRefCentru.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    centruName = mUser.getName();
                    centruTextView.setText(centruName);

                } else {
                    Toast.makeText(DetailActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /**get userName**/
        dbRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser2 = dataSnapshot.getValue(UserInformation.class);
                    userName = mUser2.getName();

                } else {
                    Toast.makeText(DetailActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        calendarView.setCurrentDayTextColor(R.color.colorAccentShade);
        calendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.colorAccentShade));

        /**adding events into calendar**/
        dbRefCentru.child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        // user exists
                        number = snapshot.getChildrenCount();
                        if (number > 5) {
                            String date = snapshot.getKey();
                            Event ev1 = null;
                            try {
                                ev1 = new Event(Color.RED, dateFormatForDisplaying.parse(date).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            calendarView.addEvent(ev1);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**ClickListeners**/
       calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectedDate = dateClicked;
                myDate.setText(dateFormatForDisplaying.format(dateClicked));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                /*toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));*/
                MonthText.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        /**send request**/
        SaveMyDateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Event> events = calendarView.getEvents(selectedDate);
                if(events.size() == 0) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    SentRequestKey = dbRefUserSentRequests.push().getKey();
                    dbRefUserSentRequests.child(SentRequestKey)
                            .setValue(new UserSentRequest(centruTextView.getText().toString(), centruStringKey, myDate.getText().toString(), item));
                    ReceivedRequestKey = dbRefCentruReceivedRequests.push().getKey();
                    dbRefCentruReceivedRequests.child(ReceivedRequestKey)
                            .setValue(new ReceivedRequests(userName, user.getUid(), myDate.getText().toString(), item));
                    Toast.makeText(DetailActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DetailActivity.this, "This day is full.Choose another day", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**get avaialable servies**/
        ServicesBttn2 = (Button) findViewById(R.id.ServicesBttn2);
        dbRefCentru.child("availableServices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    GenericTypeIndicator<ArrayList<String>> genericTypeIndicator =new GenericTypeIndicator<ArrayList<String>>(){};

                    ArrayList<String> list =dataSnapshot.getValue(genericTypeIndicator);
                    ServicesChecked2 = new boolean[list.size()];
                    ServicesList2 = new String[list.size()];
                    for(int i = 0; i < list.size(); i++){
                        ServicesList2[i] = list.get(i);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**choose available servie for request**/
        ServicesBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(DetailActivity.this);
                Builder.setTitle(R.string.dialog_title);
                Builder.setSingleChoiceItems(ServicesList2, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                Builder.setMultiChoiceItems(ServicesList2, ServicesChecked2, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                        if(isChecked){
                            if(! ServicesCenter2.contains(pos)){
                                ServicesCenter2.add(pos);
                            }
                            else{
                                ServicesCenter2.remove((Integer)pos);
                            }

                        }

                    }
                });

                Builder.setCancelable(false);
                Builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        item = ServicesList2[selectedPosition];

                    }
                });

                Builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = Builder.create();
                mDialog.show();
            }
        });
    }
}

