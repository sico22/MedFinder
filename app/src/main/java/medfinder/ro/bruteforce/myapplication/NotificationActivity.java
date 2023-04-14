package medfinder.ro.bruteforce.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ListView myListViewNotifications;
    private TextView TextViewNtf;
    private FirebaseAuth firebaseAuth;
    private ItemAdapterAnsweredRequests adapter;
    private ProgressDialog mProgressDialog;
    private DatabaseReference dbReference, dbReference2;
    private String requestKey;
    private ArrayList<AnsweredRequests> answeredRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        firebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        myListViewNotifications = (ListView) findViewById(R.id.myListViewNotifications);

        TextViewNtf = (TextView) findViewById(R.id.TextViewNtf);

        adapter = new ItemAdapterAnsweredRequests(this, answeredRequests);
        myListViewNotifications.setAdapter(adapter);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser() == null){
            //closing the activity
            finish();
            //start login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user.getUid()).child("answeredRequests");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AnsweredRequests request = snapshot.getValue(AnsweredRequests.class);
                        answeredRequests.add(request);
                        adapter.notifyDataSetChanged();
                        TextViewNtf.setVisibility(View.INVISIBLE);
                        requestKey = snapshot.getKey();
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

        myListViewNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        if(answeredRequests.size() == 0){
            TextViewNtf.setVisibility(View.VISIBLE);
        }
        else{
            TextViewNtf.setVisibility(View.INVISIBLE);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_centre:
                                startActivity(new Intent(NotificationActivity.this, ListActivity.class));
                                break;
                            case R.id.action_profil:
                                startActivity(new Intent(NotificationActivity.this, ProfileActivity.class));
                                break;
                            case R.id.action_notification:

                                break;
                        }
                        return false;
                    }
                });
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
        mProgressDialog.dismiss();
    }
}

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(NotificationActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
}
