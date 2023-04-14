package medfinder.ro.bruteforce.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class CenterProfileActivity extends AppCompatActivity {
    private Button LogoutBttn3, ChangeNameBttn2, ChangeEmailBttn2, ChangePhoneBttn2,
            DeleteAccountBttn, cngPassBttnCenter, okPassCenter, ConfirmPassBttn;
    private EditText ActualPassEditText, NewPassEditText, ConfirmPassEditText;
    private FirebaseAuth firebaseAuth;
    private EditText NameCenter, MailCenter, PhoneCenter;
    private int i;
    private DatabaseReference databaseReference;


    public void CenterInfo(final String userId) {
        databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    i = mUser.getIdentity();
                    NameCenter.setText(mUser.getName());
                    PhoneCenter.setText(mUser.getPhone());
                } else {
                    Toast.makeText(CenterProfileActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UserInfo(final String userId) {
        databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    UserInformation mUser = dataSnapshot.getValue(UserInformation.class);
                    i = mUser.getIdentity();
                    if (i == 0) {
                        finish();
                        startActivity(new Intent(CenterProfileActivity.this, ListActivity.class));
                    }

                } else {
                    Toast.makeText(CenterProfileActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_center_profile);

        /**delete account**/
        Button DeleteAccountBttn = findViewById(R.id.DeleteAccountBttn);
        DeleteAccountBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder DeleteBuilder = new AlertDialog.Builder(CenterProfileActivity.this);
                View DeleteView = getLayoutInflater().inflate(R.layout.dialog_delete_account, null);
                DeleteBuilder.setView(DeleteView);
                final AlertDialog dialog = DeleteBuilder.create();
                final Button YesBttn = (Button) DeleteView.findViewById(R.id.YesBttn);
                final Button NoBttn = (Button) DeleteView.findViewById(R.id.NoBttn);
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
                dialog.show();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }

        /**declaration**/
        LogoutBttn3 = (Button) findViewById(R.id.LogoutBttn3);
        ChangeNameBttn2 = (Button) findViewById(R.id.ChangeNameBttn2);
        ChangeEmailBttn2 = (Button) findViewById(R.id.ChangeEmailBttn2);
        ChangePhoneBttn2 = (Button) findViewById(R.id.ChangePhoneBttn2);
        cngPassBttnCenter = (Button) findViewById(R.id.cngPassBttnCenter);

        NameCenter = (EditText) findViewById(R.id.NameCenter);
        MailCenter = (EditText) findViewById(R.id.MailCenter);
        PhoneCenter = (EditText) findViewById(R.id.PhoneCenter);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        CenterInfo(user.getUid());

        MailCenter.setText(user.getEmail());

        if (firebaseAuth.getCurrentUser() == null) {
            //closing the activity
            finish();
            //start login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        /**change email**/
        ChangeEmailBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                user.updateEmail(MailCenter.toString());
                Toast.makeText(CenterProfileActivity.this, "You changed your email", Toast.LENGTH_LONG).show();
            }
        });
        /**change phone**/
        ChangePhoneBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("phone").setValue(PhoneCenter.getText().toString());
                Toast.makeText(CenterProfileActivity.this, "You changed your phone", Toast.LENGTH_LONG).show();
            }
        });
        /**change name**/
        ChangeNameBttn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("name").setValue(NameCenter.getText().toString());
                Toast.makeText(CenterProfileActivity.this, "You changed your name", Toast.LENGTH_LONG).show();
            }
        });
        /**change password**/
        cngPassBttnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ChangePassBuilder2 = new AlertDialog.Builder(CenterProfileActivity.this);
                View ChangePassView2 = getLayoutInflater().inflate(R.layout.dialog_change_pass,null);
                ChangePassBuilder2.setView(ChangePassView2);
                final AlertDialog dialog = ChangePassBuilder2.create();
                ActualPassEditText = (EditText)ChangePassView2.findViewById(R.id.ActualPassEditText);
                NewPassEditText = (EditText)ChangePassView2.findViewById(R.id.NewPassEditText);
                ConfirmPassEditText =(EditText) ChangePassView2.findViewById(R.id.ConfirmPassEditText);
                ConfirmPassBttn = (Button) ChangePassView2.findViewById(R.id.ConfirmPassBttn);

                ConfirmPassBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View V) {
                        String actualpass = ActualPassEditText.getText().toString().trim();
                        String newpass = NewPassEditText.getText().toString().trim();
                        String confnewpass = ConfirmPassEditText.getText().toString().trim();
                        if(checkEmptyString(actualpass)){
                            Toast.makeText(CenterProfileActivity.this, "Please enter the current password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(checkEmptyString(newpass)){
                            Toast.makeText(CenterProfileActivity.this, "Please enter a new password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(checkEmptyString(confnewpass)){
                            Toast.makeText(CenterProfileActivity.this, "Please confirm the new password",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(!TextUtils.equals(newpass,confnewpass)){
                            Toast.makeText(CenterProfileActivity.this, "The passwords don't match",Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


        /**logout**/
        LogoutBttn3 = (Button) findViewById(R.id.LogoutBttn3);
        LogoutBttn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                // starting Login Activity

                startActivity(new Intent(CenterProfileActivity.this, MainActivity.class));
            }
        });
        /**bottom navigationview**/
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_center);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                startActivity(new Intent(CenterProfileActivity.this, CenterCalendar.class));
                                break;
                            case R.id.action_profil_centru:

                                break;
                            case R.id.action_ntf_centru:
                                startActivity(new Intent(CenterProfileActivity.this, CenterNotificationActivity.class));
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

