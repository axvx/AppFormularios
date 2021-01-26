package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioActivity extends AppCompatActivity {

    private Button btnMisFormularios, btnInformes, btnAjustes, btnActions, btnLogOut;
    private FirebaseAuth auth;
    private DatabaseReference dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        btnMisFormularios = findViewById(R.id.btnMisFormularios);
        btnAjustes = findViewById(R.id.btnAjustes);
        btnInformes = findViewById(R.id.btnInformes);
        btnActions = findViewById(R.id.btnActions);
        btnLogOut = findViewById(R.id.btnLogOut);
        auth = FirebaseAuth.getInstance();

        btnMisFormularios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, MisFormulariosActivity.class);
                startActivity(intent);
            }
        });

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, AjustesActivity.class);
                startActivity(intent);
            }
        });

        btnInformes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, Acciones.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser() != null) {
                    auth.signOut();
                    Toast.makeText(InicioActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InicioActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(InicioActivity.this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}