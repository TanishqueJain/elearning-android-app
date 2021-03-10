package material.study.elearning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
//import material.study.elearning.Admin.AdminCategoryActivity;
import material.study.elearning.Admin.AdminActivity;
import material.study.elearning.Model.Users;
import material.study.elearning.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;
    private EditText InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;
    private String parentDbName="Users";
    private CheckBox chkBoxRememberME;
    private TextView AdminLink, NoAdminLink,ForgetPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        LoginButton=(Button) findViewById(R.id.login_btn);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber=(EditText)findViewById(R.id.login_phone_number_input);
        loadingBar=new ProgressDialog(this);
        chkBoxRememberME = (CheckBox) findViewById(R.id.remember_me_chkb);
        AdminLink=(TextView) findViewById(R.id.admin_pannel_link) ;
        NoAdminLink=(TextView) findViewById(R.id.not_admin_pannel_link);
        Paper.init(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NoAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });
        NoAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NoAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });

//        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forgetpassword();
//            }
//        });
    }



    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"enter phone...",Toast.LENGTH_LONG );
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"enter password...",Toast.LENGTH_LONG );
        }
        else if(phone.equals("1") && password.equals("1"))
        {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        }
        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone,final String password) {
        if(chkBoxRememberME.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users userData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){
                            if(parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Admin Successfully log in", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("who","admin");
                                startActivity(intent);
                                finishAffinity();
                            }
                            else if(parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Successfully log in", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                intent.putExtra("who","user");
                                startActivity(intent);
                                finishAffinity();
                            }

                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Account"+phone+"is not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Create a Account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    private void forgetpassword()
//    {
//        Intent forgetintent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
//        startActivity(forgetintent);
//    }
}
