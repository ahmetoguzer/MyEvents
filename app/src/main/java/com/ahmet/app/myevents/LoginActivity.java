package com.ahmet.app.myevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmet Oguz Er on 20.09.2018.
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText etName,etPassword;
    private Button btnRegister,btnLogin;
    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFirebase();
        initView();
        addEventFireBaseListener();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cnt=0;

                for(int i = 0; i < userList.size(); i++){
                        cnt++;
                    if(userList.get(i).getName().toString().equals(etName.getText().toString())
                            && userList.get(i).getPassword().toString().equals(etPassword.getText().toString())
                            ){
                        Toast.makeText(LoginActivity.this, "Succesful",
                                Toast.LENGTH_SHORT).show();
                        Global.eventListName = etName.getText().toString();
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        return;
                    }else if(cnt == userList.size()){
                        Toast.makeText(LoginActivity.this, "Not Found User",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.et_Name);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }


    private void addEventFireBaseListener() {

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(userList.size() > 0){
                    userList.clear();
                }
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    userList.add(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
