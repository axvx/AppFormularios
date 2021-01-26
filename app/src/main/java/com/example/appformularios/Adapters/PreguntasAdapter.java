package com.example.appformularios.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.Entidades.Pregunta;
import com.example.appformularios.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.ViewHolder> {

    private List<Pregunta> items;
    private LayoutInflater inflater;
    private Context context;
    public static String respuesta = "";
    private Formulario formulario;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    public static Map<String, Object> resp = new HashMap<>();
    public static String idform;


    public PreguntasAdapter(List<Pregunta> preguntas, Context context, Formulario form) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.formulario = form;
        setItems(preguntas);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public PreguntasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pregunta, null);
        return new PreguntasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntasAdapter.ViewHolder holder, int position) {
        holder.bindData(items.get(position), this.context, this.formulario);
    }

    public void setItems(List<Pregunta> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta;
        RecyclerView opciones;
        RecyclerView.LayoutManager manager;
        RecyclerView.Adapter adapter;
        Button btnGuardarRespuestas;
        final String id = new String(auth.getCurrentUser().getUid());

        ViewHolder(View view) {
            super(view);
            tvPregunta = view.findViewById(R.id.tvPregunta);
            opciones = view.findViewById(R.id.rvOpciones);
            btnGuardarRespuestas = view.findViewById(R.id.btnGuardarRespuesta);
        }

        void bindData(final Pregunta item, final Context context, Formulario form) {
            opciones.setHasFixedSize(true);
            manager = new LinearLayoutManager(context);
            opciones.setLayoutManager(manager);
            //adapter = new OpcionesAdapter(item.getOpciones(), context, item, form);
            switch (item.getTipo()) {
                case 2:
                    adapter = new OpcionSimpleAdapter(item.getOpciones(), context, item, form);
                    break;
                case 3:
                    adapter = new OpcionMultipleAdapter(item.getOpciones(), context, item, form);
                    break;
                default:
                    adapter = new OpcionOpenAdapter(item.getOpciones(), context, item, form);
                    break;
            }
            opciones.setAdapter(adapter);
            tvPregunta.setText(item.getId() + ". " + item.getPregunta());
            idform = form.getId();
            btnGuardarRespuestas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                    resp.put(item.getId()+"", respuesta);
                    respuesta = "";
                }
            });
        }
    }
}
