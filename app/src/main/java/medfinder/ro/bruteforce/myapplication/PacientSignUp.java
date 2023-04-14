package medfinder.ro.bruteforce.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PacientSignUp extends AppCompatActivity {

    private EditText SignUpNameText, SignUpPassText, SignUpConPassText, SignUpPhoneText, SignUpEmailText;
    private ImageView SignUpUserImg;
    private Button SignUpBttn;
    private ProgressDialog progressDialog2;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private int i;
    private DataSnapshot snapshot;

    private void registerUser(){
        String email = SignUpEmailText.getText().toString().trim();
        String password = SignUpPassText.getText().toString().trim();
        String copass = SignUpConPassText.getText().toString().trim();
        final String name = SignUpNameText.getText().toString().trim();
        final String phone = SignUpPhoneText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(copass)){
            Toast.makeText(this, "Please confim password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please confim password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.equals(password, copass)){
            Toast.makeText(this, "Passwords not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog2.setMessage("Registering User...");
        progressDialog2.show();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserInformation userInformation = new UserInformation(name, phone,"", 0, false, 0.0f);

                            databaseReference.child(user.getUid()).setValue(userInformation);
                            Toast.makeText(PacientSignUp.this, "Information saved", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(new Intent(PacientSignUp.this, ListActivity.class));
                            Toast.makeText(PacientSignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(PacientSignUp.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            //message.hide();
                            return;
                            //Toast.makeText(PacientSignUp.this, "Could not register... please try again", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog2.dismiss();
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

                    if (i == 0) {
                        finish();
                        startActivity(new Intent(PacientSignUp.this, ListActivity.class));
                    } else {
                        finish();
                        startActivity(new Intent(PacientSignUp.this, CenterProfileActivity.class));
                    }

                } else {
                    Toast.makeText(PacientSignUp.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_pacient_sign_up);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog2 = new ProgressDialog(this);

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }
        SignUpNameText = (EditText) findViewById(R.id.SignUpNameText);
        SignUpPassText = (EditText) findViewById(R.id.SignUpPassText);
        SignUpConPassText = (EditText) findViewById(R.id.SignUpConPassText);
        SignUpEmailText = (EditText) findViewById(R.id.SignUpEmailText);
        SignUpPhoneText = (EditText) findViewById(R.id.SignUpPhoneText);

        SignUpUserImg = (ImageView) findViewById(R.id.SignUpUserImg);
        SignUpBttn = (Button) findViewById(R.id.SignUpBttn);

        SignUpBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
}
