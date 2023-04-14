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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText LogInEmailText, LogInPassText;
    private TextView SignupText, OrText;
    private ImageButton PacientImgBttn, CenterImgBttn;
    private ImageView LogoImg;
    private Button LoginBttn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private int Identity;
    private DataSnapshot snapshot;
    private DatabaseReference databaseReference;
    private int i;
    private String userId;


    private void loginUser(){
        String email = LogInEmailText.getText().toString().trim();
        String password = LogInPassText.getText().toString().trim();

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

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //start profile activity
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserInfo(user.getUid());
                            }
                        else{
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(MainActivity.this, "Could not log in... please try again"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
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
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    } else {
                        finish();
                        startActivity(new Intent(MainActivity.this, CenterProfileActivity.class));
                    }

                } else {
                    Toast.makeText(MainActivity.this, "****NOT FOUND****", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_main);

        /**declaration**/
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            UserInfo(user.getUid());
        }

        LogInEmailText = (EditText) findViewById(R.id.LogInEmailText);
        LogInPassText = (EditText) findViewById(R.id.LogInPassText);

        PacientImgBttn = (ImageButton) findViewById(R.id.PacientImgBttn);
        CenterImgBttn = (ImageButton) findViewById(R.id.CenterImgBttn);

        LogoImg = (ImageView) findViewById(R.id.LogoImg);

        SignupText = (TextView) findViewById(R.id.SignUpText);
        OrText = (TextView) findViewById(R.id.OrText);

        LoginBttn = (Button) findViewById(R.id.LoginBttn);

        /**sign up as pacient**/
        PacientImgBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this,  PacientSignUp.class));
            }
        });
        /**sign up as center**/
        CenterImgBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, CenterSignUp.class));
            }
        });
        LoginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
}
