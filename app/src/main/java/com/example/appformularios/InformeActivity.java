package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appformularios.Adapters.InformePreguntasAdapter;
import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.Entidades.Pregunta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InformeActivity extends AppCompatActivity {

    private Formulario form = new Formulario();
    private TextView tvTituloFormulario;
    private TextView tvUsuarioInforme;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ArrayList<String> preg = new ArrayList<>();
    private static ArrayList<Pregunta> preguntas = new ArrayList<>();
    private static RecyclerView listPreguntaRespuesta;
    private RecyclerView.LayoutManager manager;
    private static RecyclerView.Adapter adapter;
    private int id_form;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        setId_form(getIntent().getIntExtra("id_form", 0));
        tvTituloFormulario = findViewById(R.id.tvTituloFormularioInforme);
        tvUsuarioInforme = findViewById(R.id.tvUsuarioRespuestas);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listPreguntaRespuesta = findViewById(R.id.rvPreguntaRespuesta);
        getFormularioFromFirebase();
        getUsuario();
        getPreguntasFromFirebase();
    }

    private void getUsuario() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    tvUsuarioInforme.setText(snapshot.child("nombre").getValue().toString());
                } else {
                    Toast.makeText(InformeActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    listPreguntaRespuesta.setHasFixedSize(true);
                    manager = new LinearLayoutManager(InformeActivity.this);
                    listPreguntaRespuesta.setLayoutManager(manager);
                    listPreguntaRespuesta.setItemAnimator(new DefaultItemAnimator());
                    adapter = new InformePreguntasAdapter(preguntas, preg, form.getId(), InformeActivity.this);
                    listPreguntaRespuesta.setAdapter(adapter);
                } else {
                    Toast.makeText(InformeActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InformeActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
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