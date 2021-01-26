package com.example.appformularios.Adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

public class OpcionOpenAdapter extends RecyclerView.Adapter<OpcionOpenAdapter.ViewHolder> {

    private List<String> items;
    private LayoutInflater inflater;
    private Context context;
    private int lastCheckedPosition = -1;
    private Pregunta pregunta;
    private Formulario formulario;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;


    public OpcionOpenAdapter(ArrayList<String> opciones, Context context, Pregunta preg, Formulario form) {
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
    public OpcionOpenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pregunta_abierta, null);
        return new OpcionOpenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }


    public void setItems(List<String> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText txtOpcion;
        final String id = new String(auth.getCurrentUser().getUid());


        ViewHolder(View view) {
            super(view);
            txtOpcion = view.findViewById(R.id.txtRespuestaPA);
        }

        void bindData(final String item) {
            txtOpcion.setText("");
            txtOpcion.setHint("Escribe aquí tu respuesta");
            txtOpcion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE
                            || actionId == KeyEvent.ACTION_DOWN
                            || actionId == KeyEvent.KEYCODE_ENTER) {
                        PreguntasAdapter.respuesta = txtOpcion.getText()+"";
                        //Toast.makeText(context, txtOpcion.getText(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}