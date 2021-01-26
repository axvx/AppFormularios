package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class FormCompartidoActivity extends AppCompatActivity {

    private Button btnGuardarFormCodigo;
    private EditText txtCodigoForm;
    private static ArrayList<Pregunta> preguntas = new ArrayList<>();
    private Formulario form = new Formulario();
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private ArrayList<String> preg = new ArrayList<>();

    private int id_form;
    private String id_usuario;
    private String titulo_form;
    private int nform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_compartido);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);
        btnGuardarFormCodigo = findViewById(R.id.btnGuardarFormCodigo);
        txtCodigoForm = findViewById(R.id.txtCodigoForm);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnGuardarFormCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = txtCodigoForm.getText().toString();
                obtenerCódigo(codigo);
                getFormularioFromFirebase();
                getPreguntasFromFirebase();
                if(form.getId() != null) {
                    guardarForm();
                    Toast toast = Toast.makeText(FormCompartidoActivity.this,
                            "Formulario compartido: " +  form.getTitulo() + " guardado correctamente", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void obtenerCódigo(String codigo) {
        String string = codigo;
        String[] parts = string.split("-");
        String id_usuario = parts[0];
        String id_form = parts[1];
        String titulo = parts[2];
        setId_form(Integer.parseInt(id_form));
        setId_usuario(id_usuario);
        setTitulo_form(titulo);
    }

    private void getPreguntasFromFirebase() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(getId_usuario()).child("Formularios").child(getId_form()+"").child("preguntas").addValueEventListener(new ValueEventListener() {
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
                } else {
                    Toast.makeText(FormCompartidoActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFormularioFromFirebase() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(getId_usuario()).child("Formularios").child(getId_form()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    form = new Formulario();
                    form.setTitulo(snapshot.child("titulo").getValue().toString());
                    form.setDescripcion(snapshot.child("descripcion").getValue().toString());
                    form.setId(snapshot.child("id").getValue().toString());
                    form.setIdUsuario(snapshot.child("id_usuario").getValue().toString());
                } else {
                    Toast.makeText(FormCompartidoActivity.this, "No existe el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void guardarForm() {
        final String id = auth.getCurrentUser().getUid();
        if(!form.getId().isEmpty()) {
            final Map<String, Object> frm = new HashMap<>();
            mDatabase.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    setForm(Integer.parseInt(snapshot.child("n_form").getValue().toString()));
                    System.out.println(getForm());
                    frm.put("titulo", form.getTitulo());
                    frm.put("id_usuario", id);
                    frm.put("preguntas", form.getPreguntas());
                    frm.put("id", getForm());
                    frm.put("descripcion", form.getDescripcion());
                    mDatabase.child("Usuarios").child(id).child("Formularios").child(getForm()+"").setValue(frm);
                    setForm(getForm()+1);
                    System.out.println(getForm());
                    mDatabase.child("Usuarios").child(id).child("n_form").setValue(getForm());
                    form = new Formulario();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(FormCompartidoActivity.this, "Asigne un nombre al formulario", Toast.LENGTH_SHORT).show();
        }
    }

    public int getId_form() {
        return id_form;
    }

    public void setId_form(int id_form) {
        this.id_form = id_form;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTitulo_form() {
        return titulo_form;
    }

    public void setTitulo_form(String titulo_form) {
        this.titulo_form = titulo_form;
    }

    public int getForm() {
        return nform;
    }

    public void setForm(int form) {
        this.nform = form;
    }
}