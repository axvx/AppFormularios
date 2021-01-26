package com.example.appformularios.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.Entidades.Pregunta;
import com.example.appformularios.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InformePreguntasAdapter extends RecyclerView.Adapter<InformePreguntasAdapter.ViewHolder> {

    private List<Pregunta> items;
    private List<String> respuestas;
    private String id_form;
    private LayoutInflater inflater;
    private Context context;
    private int position;


    public InformePreguntasAdapter(List<Pregunta> preguntas, List<String> respuestas, String id_form, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setItems(preguntas);
        setItemsRespuestas(respuestas);
        this.id_form = id_form;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @NonNull
    @Override
    public InformePreguntasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pregunta_informe, null);
        return new InformePreguntasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformePreguntasAdapter.ViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    public Context getContext() {
        return context;
    }

    public void setItems(List<Pregunta> items) {
        this.items = items;
    }

    public void setItemsRespuestas(List<String> items) {
        this.respuestas = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta;
        TextView tvRespuesta;
        private FirebaseAuth auth;
        private DatabaseReference mDatabase;

        ViewHolder(View view) {
            super(view);
            auth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            tvPregunta = view.findViewById(R.id.tvPreguntaInforme);
            tvRespuesta = view.findViewById(R.id.tvUsuarioRespuestasInforme);
        }

        void bindData(final Pregunta item) {
            tvPregunta.setText(item.getId() + ". " + item.getPregunta());
            final String id = new String(auth.getCurrentUser().getUid());
            mDatabase.child("Usuarios").child(id).child("Respuestas").child(id_form).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        tvRespuesta.setText(snapshot.child(item.getId()).getValue().toString());
                    } else {
                        Toast.makeText(context, "No existe el registro", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
