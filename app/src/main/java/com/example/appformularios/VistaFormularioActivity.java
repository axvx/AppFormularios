package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appformularios.Adapters.PreguntasAdapter;
import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.Entidades.Pregunta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VistaFormularioActivity extends AppCompatActivity {

    private TextView tvTituloFormulario;
    private static RecyclerView listPreguntas;
    private RecyclerView.LayoutManager manager;
    private static RecyclerView.Adapter adapter;
    private static ArrayList<Pregunta> preguntas = new ArrayList<>();
    private Formulario form = new Formulario();
    private Button btnGuardarRespuestas;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private ArrayList<String> preg = new ArrayList<>();

    private int id_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_formulario);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        tvTituloFormulario = findViewById(R.id.tvTituloFormulario);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setId_form(getIntent().getIntExtra("id_form", 0));
        final String id = new String(auth.getCurrentUser().getUid());
        btnGuardarRespuestas = findViewById(R.id.btnGuardarRespuestas);
        btnGuardarRespuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Usuarios").child(id).child("Respuestas").child(
                        PreguntasAdapter.idform).setValue(PreguntasAdapter.resp);
                Toast.makeText(VistaFormularioActivity.this, "Respuestas guardadas correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VistaFormularioActivity.this, MisFormulariosActivity.class);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser() != null) {
            listPreguntas = findViewById(R.id.rvPreguntas);
            getFormularioFromFirebase();
            getPreguntasFromFirebase();

        } else {
            Toast.makeText(VistaFormularioActivity.this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPreguntasFromFirebase() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(id).child("Formularios").child(getId_form()+"").child("preguntas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pregunta pregunta;
                preg.clear();
                preguntas.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        ArrayList<String> opciones = new ArrayList<>();
                        String titulo_preg = snap.child("pregunta").getValue()+"";
                        String tipo = snap.child("tipo").getValue()+"";
                        if(tipo.equals("0")) {
                            pregunta = new Pregunta();
                            for(DataSnapshot aux : snap.child("opciones").getChildren()) {
                                opciones.add(aux.getValue()+"");
                            }
                            pregunta.setPregunta(titulo_preg);
                            pregunta.setId(snap.child("id").getValue()+"");
                            pregunta.setTipo(Integer.parseInt(tipo));
                            pregunta.setOpciones(opciones);
                            preguntas.add(pregunta);
                        } else if(tipo.equals("1")) {
                            pregunta = new Pregunta();
                            for(DataSnapshot aux : snap.child("opciones").getChildren()) {
                                opciones.add(aux.getValue()+"");
                            }
                            pregunta.setPregunta(titulo_preg);
                            pregunta.setId(snap.child("id").getValue()+"");
                            pregunta.setTipo(Integer.parseInt(tipo));
                            pregunta.setOpciones(opciones);
                            preguntas.add(pregunta);
                        } else if(tipo.equals("2")) {
                            pregunta = new Pregunta();
                            for(DataSnapshot aux : snap.child("opciones").getChildren()) {
                                opciones.add(aux.getValue()+"");
                            }
                            pregunta.setPregunta(titulo_preg);
                            pregunta.setId(snap.child("id").getValue()+"");
                            pregunta.setTipo(Integer.parseInt(tipo));
                            pregunta.setOpciones(opciones);
                            preguntas.add(pregunta);
                        } else if(tipo.equals("3")) {
                            pregunta = new Pregunta();
                            for(DataSnapshot aux : snap.child("opciones").getChildren()) {
                                opciones.add(aux.getValue()+"");
                            }
                            pregunta.setPregunta(titulo_preg);
                            pregunta.setId(snap.child("id").getValue()+"");
                            pregunta.setTipo(Integer.parseInt(tipo));
                            pregunta.setOpciones(opciones);
                            preguntas.add(pregunta);
                        } else {

                        }
                    }
                    form.setPreguntas(preguntas);
                    listPreguntas.setHasFixedSize(true);
                    manager = new LinearLayoutManager(VistaFormularioActivity.this);
                    listPreguntas.setLayoutManager(manager);
                    listPreguntas.setItemAnimator(new DefaultItemAnimator());
                    adapter = new PreguntasAdapter(form.getPreguntas(), VistaFormularioActivity.this, form);
                    listPreguntas.setAdapter(adapter);
                } else {
                    Toast.makeText(VistaFormularioActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFormularioFromFirebase() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(id).child("Formularios").child(getId_form()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    tvTituloFormulario.setText(snapshot.child("titulo").getValue().toString());
                    form = new Formulario();
                    form.setTitulo(snapshot.child("titulo").getValue().toString());
                    form.setDescripcion(snapshot.child("descripcion").getValue().toString());
                    form.setId(snapshot.child("id").getValue().toString());
                    form.setIdUsuario(snapshot.child("id_usuario").getValue().toString());
                } else {
                    Toast.makeText(VistaFormularioActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getId_form() {
        return id_form;
    }

    public void setId_form(int id_form) {
        this.id_form = id_form;
    }
}