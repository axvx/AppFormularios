package com.example.appformularios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appformularios.Entidades.Formulario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Acciones extends AppCompatActivity {

    private Button btnNuevoFormulario, btnNuevoInforme, btnAgFormularioCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);
        btnNuevoFormulario = findViewById(R.id.btnNuevoFormulario);
        btnNuevoInforme = findViewById(R.id.btnNuevoInforme);
        btnAgFormularioCodigo = findViewById(R.id.btnFormCompartido);

        btnNuevoFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acciones.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        btnNuevoInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acciones.this, CrearInformeActivity.class);
                startActivity(intent);
            }
        });

        btnAgFormularioCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acciones.this, FormCompartidoActivity.class);
                startActivity(intent);
            }
        });
    }
}