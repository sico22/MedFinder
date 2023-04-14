package medfinder.ro.bruteforce.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SearchView;

public class ListActivity extends AppCompatActivity {

    private ListView myListView;
    private MaterialSearchView searchView;
    private EditText searchField;
    private ImageButton searchButton;
    private Button findButton;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference,databaseReference, dbRefCenterServices, dbRefCenterSwitch;
    private int i = 0;
    private ProgressDialog mProgressDialog;
    private ItemAdapter adapter, adapterSearch;
    private ArrayList<UserInformation> userInformations=new ArrayList<>();
    private ArrayList<String> userKeys =new ArrayList<String>();
    private String id;
    private BottomNavigationView bottomNavigationView;
    private FirebaseUser user;
    private int itemId;

    public void UserInfo(final String userId){
        /**verify if user is actually a doctor**/
        databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    i = mUser.getIdentity();

                    if (i == 1){
                        finish();
                        startActivity(new Intent(ListActivity.this, CenterProfileActivity.class));
                    }

                } else {
                    Toast.makeText(ListActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /**declaration**/
        mProgressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        myListView = (ListView) findViewById(R.id.myListView);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        adapter = new ItemAdapter(this, userInformations);
        myListView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        /**verify if user is actually a doctor**/
        if(firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }

        /**verify wether user is actually null**/
        if(firebaseAuth.getCurrentUser() == null){
            //closing the activity
            finish();
            //start login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        /**get centers in the listview**/
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        dbReference.orderByChild("ratingCentru").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserInformation user = snapshot.getValue(UserInformation.class);
                                i = user.getIdentity();
                                id = snapshot.getKey();

                                if(i == 1){
                                    userInformations.add(user);
                                    userKeys.add(id);
                                    adapter.notifyDataSetChanged();
                                }
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

        /**clicking on a center -> DetailActivity**/
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent showDetailActivity = new Intent(ListActivity.this, DetailActivity.class);
                final String user = userKeys.get(position);
                showDetailActivity.putExtra("CenterUser",user);
                startActivity(showDetailActivity);
            }
        });

        /*Query FirebaseSearchQuery = dbReference.orderByChild("name")
                .startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() */


        /**while searching**/
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                /**if closed search view, listview will reetrn default**/
                /*myListView = (ListView) findViewById(R.id.myListView);
                adapter = new ItemAdapter(ListActivity.this, userInformations);
                myListView.setAdapter(adapter);*/

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText != null && !newText.isEmpty()) {
                    final ArrayList<UserInformation> lstFound=new ArrayList<>();
                    adapter = new ItemAdapter(ListActivity.this, lstFound);
                    myListView.setAdapter(adapter);
                    for (int l = 0; l < userInformations.size(); l++) {
                        final UserInformation item = userInformations.get(l);
                        String centerId = userKeys.get(l);
                        dbRefCenterServices = dbReference.child(centerId).child("availableServices");
                        dbRefCenterSwitch = dbReference.child(centerId).child("switchul");
                        if(itemId == R.id.sortByName) {
                            /**sortare dupa Numele Centrului**/
                            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                                lstFound.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else if(itemId == R.id.sortByAnalize){
                            /**sortare dupa analizele disponibile**/
                            dbRefCenterServices.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    GenericTypeIndicator<ArrayList<String>> genericTypeIndicator =new GenericTypeIndicator<ArrayList<String>>(){};

                                    ArrayList<String> servicesList =dataSnapshot.getValue(genericTypeIndicator);
                                    for(int v = 0; v < servicesList.size(); v++){
                                        if(servicesList.get(v).toLowerCase().contains(newText.toLowerCase())){
                                            lstFound.add(item);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    hideProgressDialog();
                                }
                            });
                        }
                    }
                }
                else{
                    //if closed search view, listview will reetrn default
                    myListView = (ListView) findViewById(R.id.myListView);
                    adapter = new ItemAdapter(ListActivity.this, userInformations);
                    myListView.setAdapter(adapter);
                }
                return true;
            }
        });

        /**bottom Navigation View**/
        bottomNavigationView =
                findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_centre:

                                break;
                            case R.id.action_profil:
                                startActivity(new Intent(ListActivity.this, ProfileActivity.class));
                                break;
                            case R.id.action_notification:
                                startActivity(new Intent(ListActivity.this, NotificationActivity.class));
                                break;
                        }
                        return false;
                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        itemId = item.getItemId();


        if(itemId == R.id.sortByFonduri){
            /**sortare dupa fonduri available sau nu**/
            final ArrayList<UserInformation> lstFound=new ArrayList<>();
            adapter = new ItemAdapter(ListActivity.this, lstFound);
            myListView.setAdapter(adapter);
            for (int l = 0; l < userInformations.size(); l++) {
                final UserInformation itemSelected = userInformations.get(l);
                String centerId = userKeys.get(l);
                dbRefCenterSwitch = dbReference.child(centerId).child("switchul");
                dbRefCenterSwitch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Boolean> genericTypeIndicator = new GenericTypeIndicator<Boolean>() {
                        };

                        Boolean switching = dataSnapshot.getValue(genericTypeIndicator);
                        if (switching == true) {
                            lstFound.add(itemSelected);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hideProgressDialog();
                    }
                });
            }
        }
        return true;
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ListActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    private boolean checkEmptyString(String text) {
        if(text==null || text.equals(""))
            return true;
        return false;
    }

}
