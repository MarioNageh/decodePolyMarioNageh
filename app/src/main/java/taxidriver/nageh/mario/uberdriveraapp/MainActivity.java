package taxidriver.nageh.mario.uberdriveraapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import taxidriver.nageh.mario.uberdriveraapp.Modules.Driver;

public class MainActivity extends AppCompatActivity {
    Button btn_reg, btn_signin;
    FirebaseDatabase mDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ConstraintLayout layout;
    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = mDatabase.getReference("Drivers");
        initview();
    }

    private void initview() {
        btn_reg = (Button) findViewById(R.id.main_btn_Rigister);
        btn_signin = (Button) findViewById(R.id.main_btn_signin);
        layout = (ConstraintLayout) findViewById(R.id.mainactibit_rootView);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBtnRegisterClicked();
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSignInMethod();
            }
        });


    }
    private void ShowProsseseDialog(){
        bar=ProgressDialog.show(this,"","Please Watting",true);
        bar.setCancelable(false);
    }

    private void OnBtnRegisterClicked() {
        AlertDialog.Builder alretDialog = new AlertDialog.Builder(this);
        alretDialog.setTitle("Register");
        alretDialog.setMessage("Enter Correct Informations");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);
        final EditText Username = (EditText) register_layout.findViewById(R.id.ebt_username);
        final EditText Password = (EditText) register_layout.findViewById(R.id.ebt_password);
        final EditText Email = (EditText) register_layout.findViewById(R.id.ebt_email);
        final EditText Phone = (EditText) register_layout.findViewById(R.id.ebt_phone);
        alretDialog.setView(register_layout);
        alretDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (TextUtils.isEmpty(Username.getText().toString())) {
                    Snackbar.make(layout, "Please Enter UserName", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Email.getText().toString())) {
                    Snackbar.make(layout, "Please Enter Email", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Phone.getText().toString())) {
                    Snackbar.make(layout, "Please Enter Phone", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Password.getText().toString())) {
                    Snackbar.make(layout, "Please Enter Password", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (Password.getText().toString().length() < 6) {
                    Snackbar.make(layout, "Password is too short", Snackbar.LENGTH_LONG).show();
                    return;
                }
                ShowProsseseDialog();
                firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Driver driver = new Driver();
                                driver.setEmail(Email.getText().toString());
                                driver.setPassword(Password.getText().toString());
                                driver.setPhone(Phone.getText().toString());
                                driver.setUsername(Username.getText().toString());
                                databaseReference.child(driver.getUsername())
                                        .setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(layout, "Success Registration", Snackbar.LENGTH_SHORT).show();
                                        bar.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(layout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        bar.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(layout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        bar.dismiss();
                    }
                });
            }
        });
        alretDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alretDialog.show();
    }

    private void OnSignInMethod(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Enter Data To Sign In");
        LayoutInflater inflater= LayoutInflater.from(this);
        View Layout_signin=inflater.inflate(R.layout.layout_signin,null);
        final EditText Email=Layout_signin.findViewById(R.id.edt_singin_email);
        final   EditText Password=Layout_signin.findViewById(R.id.edt_signin_password);
        dialog.setView(Layout_signin);
        dialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(TextUtils.isEmpty(Email.getText().toString())){
                    Snackbar.make(layout,"Please Enter The Email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password.getText().toString())){
                    Snackbar.make(layout,"Please Enter The Email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(Password.getText().length()<6){
                    Snackbar.make(layout,"Password Is Too Short",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ShowProsseseDialog();

                firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        bar.dismiss();
                        Intent intent=new Intent(MainActivity.this,Mainuser.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(layout, e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        bar.dismiss();
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}
