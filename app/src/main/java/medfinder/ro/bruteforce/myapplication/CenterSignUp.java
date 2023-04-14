package medfinder.ro.bruteforce.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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


public class CenterSignUp extends AppCompatActivity {

    private Button CenterSignUpBttn, ServicesBttn;

    private EditText CenterSignUpEmailText, CenterSignUpPassText, CenterSignUpConPassText,
            CenterSignUpNameText, CenterSignUpPhoneText, CenterSignUpAddress;

    private Switch SwitchFunds;

    private ProgressDialog progressDialog2;

    private String[] ServicesList;
    private boolean[] ServicesChecked;
    private ArrayList<Integer> ServicesCenter = new ArrayList<>();
    private ArrayList<String> ServicesToSaveInFirebase = new ArrayList<String>();

    private DatabaseReference databaseReference, dbRefServicesList;
    private FirebaseAuth firebaseAuth;

    private int i;
    private Boolean switchState;


    private void registerCenter(){
        String email = CenterSignUpEmailText.getText().toString().trim();
        String password = CenterSignUpPassText.getText().toString().trim();
        String copass = CenterSignUpConPassText.getText().toString().trim();
        final ArrayList<String> servicesToSave = ServicesToSaveInFirebase;

        if(TextUtils.isEmpty(email)){
            //mail is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(copass)){
            //password is
            Toast.makeText(this, "Please confim password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.equals(password, copass)){

            Toast.makeText(this, "Passwords not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog2.setMessage("Registering User...");
        progressDialog2.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registred and logged in
                            //we will start the profile activity here

                            firebaseAuth = FirebaseAuth.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                            String name = CenterSignUpNameText.getText().toString().trim();
                            String phone = CenterSignUpPhoneText.getText().toString().trim();
                            String address = CenterSignUpAddress.getText().toString().trim();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            switchState = SwitchFunds.isChecked();

                            UserInformation userInformation = new UserInformation(name, phone, address, 1, switchState, 0.0f);


                            databaseReference.child(user.getUid()).setValue(userInformation);

                            dbRefServicesList = databaseReference.child(user.getUid()).child("availableServices");

                            dbRefServicesList.setValue(servicesToSave);
                            Toast.makeText(CenterSignUp.this, "Information saved", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(new Intent(CenterSignUp.this, CenterProfileActivity.class));
                            Toast.makeText(CenterSignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(CenterSignUp.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            //message.hide();
                            return;
                            //Toast.makeText(PacientSignUp.this, "Could not register... please try again", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog2.dismiss();
                    }
                });
    }
    public void UserInfo(final String userId){
        /**verify what the user is**/
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
                        startActivity(new Intent(CenterSignUp.this, ListActivity.class));
                    } else {
                        finish();
                        startActivity(new Intent(CenterSignUp.this, CenterProfileActivity.class));
                    }

                } else {
                    Toast.makeText(CenterSignUp.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_center_sign_up);

        /** declaration**/

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog2 = new ProgressDialog(this);

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        CenterSignUpBttn = (Button) findViewById(R.id.CenterSignUpBttn);
        CenterSignUpEmailText = (EditText) findViewById(R.id.CenterSignUpEmailText);
        CenterSignUpConPassText = (EditText) findViewById(R.id.CenterSignUpConPassText);
        CenterSignUpPassText = (EditText) findViewById(R.id.CenterSignUpPassText);
        CenterSignUpAddress = (EditText) findViewById(R.id.CenterSignUpAddress);
        CenterSignUpPhoneText = (EditText) findViewById(R.id.CenterSignUpPhoneText);
        CenterSignUpNameText = (EditText) findViewById(R.id.CenterSignUpNameText);
        SwitchFunds = (Switch) findViewById(R.id.SwitchFunds);
        ServicesBttn = (Button) findViewById(R.id.ServicesBttn);
        ServicesList = getResources().getStringArray(R.array.services);
        ServicesChecked = new boolean[ServicesList.length];

        /**choose available services**/
        ServicesBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(CenterSignUp.this);
                Builder.setTitle(R.string.dialog_title);
                Builder.setMultiChoiceItems(ServicesList, ServicesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                        if(isChecked){
                            if(! ServicesCenter.contains(pos)){
                                ServicesCenter.add(pos);
                            }
                            else{
                                ServicesCenter.remove((Integer)pos);
                            }

                        }

                    }
                });

                Builder.setCancelable(false);
                Builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int d = 0; d < ServicesCenter.size(); d++){
                            ServicesToSaveInFirebase.add(ServicesList[ServicesCenter.get(d)]);
                        }

                    }
                });

                Builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Builder.setNeutralButton(R.string.clearall_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int d = 0; d < ServicesChecked.length; d++){
                            ServicesChecked[d] = false;
                            ServicesCenter.clear();
                        }


                    }
                });

                AlertDialog mDialog = Builder.create();
                mDialog.show();
            }
        });
        CenterSignUpBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCenter();
            }
        });

    }
}
