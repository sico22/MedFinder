package medfinder.ro.bruteforce.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button LogoutBttn, ChangeNameBttn, ChangeEmailBttn,
            ChangePhoneBttn,ConfirmPassBttn, ChangePassBttn;
    private FirebaseAuth firebaseAuth;
    private ListView myListView;
    private String[] centre;
    private String[] locatie;
    private String[] descriptions;
    private EditText NameText, mailText, Phone, ConfirmPassEditText,
            ActualPassEditText, NewPassEditText;
    private int i;
    private DatabaseReference databaseReference;

    public void UserInformation(final String userId){
        databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    i = mUser.getIdentity();
                    NameText.setText(mUser.getName());
                    Phone.setText(mUser.getPhone());
                } else {
                    Toast.makeText(ProfileActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void UserInfo(final String userId){
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
                        startActivity(new Intent(ProfileActivity.this, CenterProfileActivity.class));
                    }

                } else {
                    Toast.makeText(ProfileActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        Button DeleteAccountBttn2 = findViewById(R.id.DeleteAccountBttn2);
        DeleteAccountBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder DeleteBuilder2 = new AlertDialog.Builder(ProfileActivity.this);
                View DeleteView2 = getLayoutInflater().inflate(R.layout.dialog_delete_account,null);
                DeleteBuilder2.setView(DeleteView2);
                final AlertDialog dialog = DeleteBuilder2.create();
                final Button YesBttn = (Button) DeleteView2.findViewById(R.id.YesBttn);
                final Button NoBttn = (Button) DeleteView2.findViewById(R.id.NoBttn);
                YesBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        user.delete();
                        dialog.dismiss();
                        finish();
                    }
                });
                NoBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        //initialazing firebase authentification object


        final EditText newPassword = (EditText) findViewById(R.id.newPassword);
        final EditText newConfPassword = (EditText) findViewById(R.id.newConfPassword);
        final EditText actualPassword = (EditText) findViewById(R.id.actualPassword);
        final Button okPass = (Button) findViewById(R.id.okPass);

       //Resources res = getResources();
       //centre = res.getStringArray(R.array.centre);
       // locatie = res.getStringArray(R.array.locatie);

        //if the user is not logged in that means current user
        //will return null
        if(firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }

        if(firebaseAuth.getCurrentUser() == null){
            //closing the activity
            finish();
            //start login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        LogoutBttn = (Button) findViewById(R.id.LogoutBttn);
        ChangeNameBttn = (Button) findViewById(R.id.ChangeNameBttn);
        ChangeEmailBttn = (Button) findViewById(R.id.ChangeEmailBttn);
        ChangePhoneBttn = (Button) findViewById(R.id.ChangePhoneBttn);
        ChangePassBttn = (Button) findViewById(R.id.ChangePassBttn);

        NameText = (EditText) findViewById(R.id.NameText);
        Phone = (EditText) findViewById(R.id.Phone);
        mailText = (EditText) findViewById(R.id.mailText);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserInformation(user.getUid());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        mailText.setText(user.getEmail());


        ChangeEmailBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                user.updateEmail(mailText.toString());
                Toast.makeText(ProfileActivity.this, "You changed your email", Toast.LENGTH_LONG).show();
            }
        });
        ChangePhoneBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("phone").setValue(Phone.getText().toString());
                Toast.makeText(ProfileActivity.this, "You changed your phone", Toast.LENGTH_LONG).show();
            }
        });
        ChangeNameBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("name").setValue(NameText.getText().toString());
                Toast.makeText(ProfileActivity.this, "You changed your name", Toast.LENGTH_LONG).show();
            }
        });

        Button ChangePassBttn = findViewById(R.id.ChangePassBttn);
        ChangePassBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ChangePassBuilder = new AlertDialog.Builder(ProfileActivity.this);
                View ChangePassView = getLayoutInflater().inflate(R.layout.dialog_change_pass,null);
                ChangePassBuilder.setView(ChangePassView);
                final AlertDialog dialog = ChangePassBuilder.create();
                ActualPassEditText = (EditText)ChangePassView.findViewById(R.id.ActualPassEditText);
                NewPassEditText = (EditText)ChangePassView.findViewById(R.id.NewPassEditText);
                ConfirmPassEditText =(EditText) ChangePassView.findViewById(R.id.ConfirmPassEditText);
                ConfirmPassBttn = (Button) ChangePassView.findViewById(R.id.ConfirmPassBttn);

                ConfirmPassBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View V) {
                        String actualpass = ActualPassEditText.getText().toString().trim();
                        String newpass = NewPassEditText.getText().toString().trim();
                        String confnewpass = ConfirmPassEditText.getText().toString().trim();
                        if(checkEmptyString(actualpass)){
                            Toast.makeText(ProfileActivity.this, "Please enter the current password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(checkEmptyString(newpass)){
                            Toast.makeText(ProfileActivity.this, "Please enter a new password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(checkEmptyString(confnewpass)){
                            Toast.makeText(ProfileActivity.this, "Please confirm the new password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(!TextUtils.equals(newpass,confnewpass)){
                            Toast.makeText(ProfileActivity.this, "The passwords don't match",Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        LogoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                // starting Login Activity

                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_centre:
                                startActivity(new Intent(ProfileActivity.this, ListActivity.class));
                                break;
                            case R.id.action_profil:

                                break;
                            case R.id.action_notification:
                                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                                break;
                        }
                        return false;
                    }
                });
    }
    private boolean checkEmptyString(String text) {
        if(text==null || text.equals(""))
            return true;
        return false;
    }
}
