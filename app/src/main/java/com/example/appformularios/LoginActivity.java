package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegistro, btnIniciarSesion;
    private EditText txtUsuario, txtEmailR, txtPasswordR, txtEmailL, txtPasswordL;

    private String username = "";
    private String emailR = "";
    private String passwordR = "";

    private String emailL = "";
    private String passwordL = "";

    private FirebaseAuth auth;
    DatabaseReference dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference();

        txtUsuario = findViewById(R.id.txtNombre);
        txtEmailR = findViewById(R.id.txtCorreoR);
        txtPasswordR = findViewById(R.id.txtPasswordR);
        btnRegistro = findViewById(R.id.btnRegistro);
        txtEmailL = findViewById(R.id.txtCorreo);
        txtPasswordL = findViewById(R.id.txtPassword);
        btnIniciarSesion = findViewById(R.id.btnLogin);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsuario.getText().toString();
                emailR = txtEmailR.getText().toString();
                passwordR = txtPasswordR.getText().toString();

                if(!username.isEmpty() && !emailR.isEmpty() && !passwordR.isEmpty()) {
                    if(passwordR.length() >= 6) {
                        registrarUsuario();
                    } else {
                        Toast.makeText(LoginActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailL = txtEmailL.getText().toString();
                passwordL = txtPasswordL.getText().toString();

                if(!emailL.isEmpty() && !passwordL.isEmpty()) {
                    loginUser();
                } else {
                    Toast.makeText(LoginActivity.this, "Complete los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(auth.getCurrentUser() != null) {
            getUserInfo();
            reiniciarActivity();
        }
    }

    private void loginUser() {
        auth.signInWithEmailAndPassword(emailL,passwordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    vaciar();
                    reiniciarActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "No se puede iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserInfo() {
        String id = "";
        FirebaseUser mFirebaseUser = auth.getCurrentUser();
        if(mFirebaseUser != null) {
            id = auth.getCurrentUser().getUid();
        }
        dataBase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String name = snapshot.child("nombre").getValue() + "";
                    String email = snapshot.child("usuario").getValue().toString();
                    //Toast.makeText(LoginActivity.this, name +" ha iniciado sesión", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void registrarUsuario() {
        auth.createUserWithEmailAndPassword(emailR, passwordR).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", username);
                    map.put("usuario", emailR);
                    map.put("password", passwordR);
                    map.put("n_form", 1);

                    String id = auth.getCurrentUser().getUid();

                    dataBase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                vaciar();
                                Toast.makeText(LoginActivity.this, "El registro fue completado exitosamente", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "El registro no fue completado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void vaciar() {
        txtEmailR.setText("");
        txtPasswordR.setText("");
        txtUsuario.setText("");
        txtEmailL.setText("");
        txtPasswordL.setText("");

    }

    public void reiniciarActivity(){
        Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
        startActivity(intent);
    }

}