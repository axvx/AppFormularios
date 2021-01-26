package com.example.appformularios.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.Entidades.Pregunta;
import com.example.appformularios.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OpcionMultipleAdapter extends RecyclerView.Adapter<OpcionMultipleAdapter.ViewHolder> {

    private List<String> items;
    private LayoutInflater inflater;
    private Context context;
    private int lastCheckedPosition = -1;
    private Pregunta pregunta;
    private Formulario formulario;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    public OpcionMultipleAdapter(ArrayList<String> opciones, Context context, Pregunta preg, Formulario form) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.pregunta = preg;
        this.formulario = form;
        setItems(opciones);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public OpcionMultipleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_opcion_multiple, null);
        return new OpcionMultipleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(items.get(position));
        holder.chkOpcion.setChecked(position==lastCheckedPosition);
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkOpcion;
        String respuesta = "";
        final String id = new String(auth.getCurrentUser().getUid());


        ViewHolder(View view) {
            super(view);
            chkOpcion = view.findViewById(R.id.chkOpcion);
        }

        void bindData(final String item) {
            chkOpcion.setText(item);
            chkOpcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    respuesta += chkOpcion.getText();
                    PreguntasAdapter.respuesta = PreguntasAdapter.respuesta + " | " + respuesta;
                    //Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}