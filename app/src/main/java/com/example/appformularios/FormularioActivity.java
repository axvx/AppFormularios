package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity {

    private Button btnAnadirPregunta, btnGuardarForm, btnGuardarPreguntaA, btnAnadirOpcion,
            btnGuardarPreguntaOp, btnGuardarOpcion, btnVerForm;
    private EditText txtNombreForm, txtPreguntaA, txtPreguntaOp, txtOpcion, txtDescripcion;
    private LinearLayout layoutPreguntaAb, layoutPreguntaOp, layoutOpciones;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    private ArrayList<String> preguntas = new ArrayList<>();
    private ArrayList<Object> preguntas_firebase = new ArrayList<>();
    private ArrayList<String> opciones = new ArrayList<>();

    private Map<String, Object> preg = new HashMap<>();
    private Map<String, Object> op = new HashMap<>();

    private ListView listViewPreguntas;
    private ListView listViewOpciones;

    private int form;
    private int tipo;
    private int num_preg = 1;

    private String nombreForm = "";

    private boolean band = false;

    final String lista[] = {"Abierta (Respuesta corta)", "Abierta (Respuesta larga)",
            "Opción múltiple","Opción simple"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        txtNombreForm = findViewById(R.id.txtNombreForm);
        txtPreguntaA = findViewById(R.id.txtPreguntaAb);
        txtPreguntaOp = findViewById(R.id.txtPreguntaOp);
        txtOpcion = findViewById(R.id.txtOpcionRespuesta);
        txtDescripcion = findViewById(R.id.txtDescipcion);
        layoutPreguntaAb = findViewById(R.id.frmPreguntaAbierta);
        layoutPreguntaOp = findViewById(R.id.frmPreguntaOpciones);
        layoutOpciones = findViewById(R.id.frmOpciones);
        btnAnadirPregunta = findViewById(R.id.btnAnadirPregunta);
        btnGuardarForm = findViewById(R.id.btnGuardarForm);
        btnGuardarPreguntaA = findViewById(R.id.btnGuardarPreguntaAb);
        btnGuardarPreguntaOp = findViewById(R.id.btnGuardarPreguntaOp);
        btnAnadirOpcion = findViewById(R.id.btnAnadirOpciones);
        btnGuardarOpcion = findViewById(R.id.btnGuardarOpcion);
        btnVerForm = findViewById(R.id.btnVerForms);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        listViewPreguntas = findViewById(R.id.listPreguntas);
        listViewOpciones = findViewById(R.id.listOpciones);

        btnAnadirPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesPregunta();
            }
        });

        btnGuardarPreguntaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pregunta = txtPreguntaA.getText().toString();
                if(!pregunta.isEmpty()) {
                    layoutPreguntaAb.setVisibility(View.GONE);
                    preguntas.add(pregunta);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                            R.layout.item_rv_pregunta, R.id.tvPregunta, preguntas);
                    listViewPreguntas.setAdapter(adapter);
                    txtPreguntaA.setText("");
                    guardarPregunta(pregunta);
                    setNum_preg(getNum_preg()+1);
                } else {

                }

            }
        });

        btnGuardarPreguntaOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pregunta = txtPreguntaOp.getText().toString();
                if(!pregunta.isEmpty()) {
                    layoutPreguntaOp.setVisibility(View.GONE);
                    preguntas.add(pregunta);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                            R.layout.item_rv_pregunta, R.id.tvPregunta, preguntas);
                    listViewPreguntas.setAdapter(adapter);
                    txtPreguntaOp.setText("");
                    guardarOp();
                    guardarPreguntaOp(pregunta);
                    opciones.clear();
                    setNum_preg(getNum_preg()+1);
                } else {

                }
            }
        });

        btnAnadirOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getTipo() == 2) {
                    layoutOpciones.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                            R.layout.item_rv_opcion_simple, R.id.rdbOpcion, opciones);
                    listViewOpciones.setAdapter(adapter);
                } else if(getTipo() == 3) {
                    layoutOpciones.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                            R.layout.item_opcion_multiple, R.id.chkOpcion, opciones);
                    listViewOpciones.setAdapter(adapter);
                }

            }
        });

        btnGuardarOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opcion = txtOpcion.getText().toString();
                if(!opcion.isEmpty()) {
                    if(getTipo() == 2) {
                        layoutOpciones.setVisibility(View.GONE);
                        opciones.add(opcion);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                                R.layout.item_rv_opcion_simple, R.id.rdbOpcion, opciones);
                        listViewOpciones.setAdapter(adapter);
                        txtOpcion.setText("");
                    } else if(getTipo() == 3) {
                        layoutOpciones.setVisibility(View.GONE);
                        opciones.add(opcion);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                                R.layout.item_opcion_multiple, R.id.chkOpcion, opciones);
                        listViewOpciones.setAdapter(adapter);
                        txtOpcion.setText("");
                    }
                }
            }
        });

        btnGuardarForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                guardarForm();
                preguntas.clear();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FormularioActivity.this,
                        R.layout.item_rv_pregunta, R.id.tvPregunta, preguntas);
                listViewPreguntas.setAdapter(adapter);
            }
        });

        btnVerForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormularioActivity.this, MisFormulariosActivity.class);
                startActivity(intent);
                Toast.makeText(FormularioActivity.this, "Formulario " + txtNombreForm.getText().toString() + " creado", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void guardarForm() {
        setNombreForm(txtNombreForm.getText().toString());
        final String id = auth.getCurrentUser().getUid();
        if(!nombreForm.isEmpty()) {
            final Map<String, Object> frm = new HashMap<>();
            mDatabase.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    setForm(Integer.parseInt(snapshot.child("n_form").getValue().toString()));
                    System.out.println(getForm());
                    frm.put("titulo", nombreForm);
                    frm.put("id_usuario", id);
                    frm.put("preguntas", getPreg());
                    frm.put("id", getForm());
                    frm.put("descripcion", getDesc());
                    mDatabase.child("Usuarios").child(id).child("Formularios").child(getForm()+"").setValue(frm);
                    setForm(getForm()+1);
                    System.out.println(getForm());
                    mDatabase.child("Usuarios").child(id).child("n_form").setValue(getForm());
                    preguntas.clear();
                    opciones.clear();
                    txtNombreForm.setText("");
                    txtDescripcion.setText("");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(FormularioActivity.this, "Asigne un nombre al formulario", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDesc() {
        String descripcion = txtDescripcion.getText().toString();
        if(descripcion.isEmpty()) {
            descripcion = "Agrega una descripción";
        }
        return  descripcion;
    }

    private void guardarPregunta(String pregunta) {
        final Map<String, Object> pre = new HashMap<>();
        final Map<String, Object> op = new HashMap<>();
        op.put("1", "text");
        pre.put("pregunta", pregunta);
        pre.put("tipo", getTipo());
        pre.put("opciones", op);
        pre.put("id", getNum_preg());
        this.preg.put(getNum_preg()+"", pre);

    }

    private void guardarPreguntaOp(String pregunta) {
        final Map<String, Object> pre = new HashMap<>();
        pre.put("pregunta", pregunta);
        pre.put("tipo", getTipo());
        pre.put("opciones",getOp());
        pre.put("id", getNum_preg());
        this.preg.put(getNum_preg()+"", pre);

    }

    private void guardarOp() {
        final Map<String, Object> opc = new HashMap<>();
        for(int i = 0; i < this.opciones.size(); i++) {
            opc.put(i+1+"", opciones.get(i));
        }
        setOp(opc);
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getNum_preg() {
        return num_preg;
    }

    public void setNum_preg(int num_preg) {
        this.num_preg = num_preg;
    }

    public String getNombreForm() {
        return nombreForm;
    }

    public void setNombreForm(String nombreForm) {
        this.nombreForm = nombreForm;
    }

    public void opcionesPregunta() {
        final AlertDialog.Builder alertaMenu = new AlertDialog.Builder(FormularioActivity.this);
        alertaMenu.setTitle("Selecciona un tipo de pregunta");
        alertaMenu.setItems(lista, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    layoutPreguntaAb.setVisibility(View.VISIBLE);
                    setTipo(0);
                } else if(i == 1) {
                    layoutPreguntaAb.setVisibility(View.VISIBLE);
                    setTipo(1);
                } else if(i == 2) {
                    layoutPreguntaOp.setVisibility(View.VISIBLE);
                    setTipo(3);
                } else if(i == 3){
                    layoutPreguntaOp.setVisibility(View.VISIBLE);
                    setTipo(2);
                } else {
                    band = false;
                }
            }
        });
        alertaMenu.create().show();
    }

    public Map<String, Object> getPreg() {
        return preg;
    }

    public void setPreg(Map<String, Object> preg) {
        this.preg = preg;
    }

    public Map<String, Object> getOp() {
        return op;
    }

    public void setOp(Map<String, Object> op) {
        this.op = op;
    }
}