package com.example.appformularios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appformularios.Adapters.FormularioAdapter;
import com.example.appformularios.Entidades.Formulario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisFormulariosActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private static RecyclerView listFormularios;
    private RecyclerView.LayoutManager manager;
    private static RecyclerView.Adapter adapter;
    private static ArrayList<Formulario> formularios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_formularios);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("https://dynamic-forms-87996.firebaseio.com/");
        //scoresRef.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(auth.getCurrentUser() != null) {
            listFormularios = findViewById(R.id.rvMisFormularios);
            getFormulariosFromFirebase();
        } else {
            Toast.makeText(MisFormulariosActivity.this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show();
        }
        registerForContextMenu(listFormularios);
    }

    public void getFormulariosFromFirebase() {
        final String id = new String(auth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child(id).child("Formularios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Formulario form;
                formularios.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        String name = snap.child("titulo").getValue().toString();
                        String id_user = snap.child("id_usuario").getValue().toString();
                        if(id_user.equals(id)) {
                            form = new Formulario();
                            form.setTitulo(name);
                            form.setId(snap.child("id").getValue()+"");
                            form.setDescripcion(snap.child("descripcion").getValue()+"");
                            formularios.add(form);
                        }
                    }
                    listFormularios.setHasFixedSize(true);
                    manager = new LinearLayoutManager(MisFormulariosActivity.this);
                    listFormularios.setLayoutManager(manager);
                    listFormularios.setItemAnimator(new DefaultItemAnimator());
                    adapter = new FormularioAdapter(formularios, MisFormulariosActivity.this);
                    listFormularios.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}