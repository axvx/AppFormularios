
package com.example.appformularios.Adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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

public class OpcionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items;
    private LayoutInflater inflater;
    private Context context;
    private int lastCheckedPosition = -1;
    private Pregunta pregunta;
    private Formulario formulario;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private String respuesta = "";


    public OpcionesAdapter(ArrayList<String> opciones, Context context, Pregunta preg, Formulario form) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 2:
                View viewSimple = inflater.inflate(R.layout.item_rv_opcion_simple, parent, false);
                return new ViewHolderSimple(viewSimple);
            case 3:
                View viewMultiple = inflater.inflate(R.layout.item_opcion_multiple, parent, false);
                return new ViewHolderMultiple(viewMultiple);
            default:
                View viewOpen = inflater.inflate(R.layout.item_pregunta_abierta, parent, false);
                return new ViewHolderOpen(viewOpen);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 2:
                ViewHolderSimple viewHolderSimple = (ViewHolderSimple) holder;
                viewHolderSimple.bindData(items.get(position));
                break;
            case 3:
                ViewHolderMultiple viewHolderMultiple = (ViewHolderMultiple) holder;
                viewHolderMultiple.bindData(items.get(position));
                break;
            case 0:
            default:
                ViewHolderOpen viewHolderOpen = (ViewHolderOpen) holder;
                viewHolderOpen.bindData(items.get(position));
        }
    }


    public void setItems(List<String> items) {
        this.items = items;
    }

    public int getItemViewType(int position) {
        if (pregunta.getTipo() == 2) {
            return 2;
        } else if(pregunta.getTipo() == 3) {
            return 3;
        } else {
            return 0;
        }
    }

    class ViewHolderSimple extends RecyclerView.ViewHolder {
        RadioButton rdbOpcion;
        final String id = new String(auth.getCurrentUser().getUid());

        ViewHolderSimple(View view) {
            super(view);
            rdbOpcion = view.findViewById(R.id.rdbOpcion);
        }

        void bindData(final String item) {
            rdbOpcion.setText(item);
            rdbOpcion.setChecked(false);
            rdbOpcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);
                    Toast.makeText(context, rdbOpcion.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class ViewHolderMultiple extends RecyclerView.ViewHolder {
        CheckBox chkOpcion;

        final String id = new String(auth.getCurrentUser().getUid());

        ViewHolderMultiple(View view) {
            super(view);
            chkOpcion = view.findViewById(R.id.chkOpcion);
        }

        void bindData(final String item) {
            chkOpcion.setText(item);
            chkOpcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    respuesta += chkOpcion.getText()+" | ";
                    PreguntasAdapter.respuesta = respuesta;
                    Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class ViewHolderOpen extends RecyclerView.ViewHolder {
        EditText txtOpcion;
        final String id = new String(auth.getCurrentUser().getUid());


        ViewHolderOpen(View view) {
            super(view);
            txtOpcion = view.findViewById(R.id.txtRespuestaPA);
        }

        void bindData(final String item) {
            txtOpcion.setText("");
            txtOpcion.setHint("Escribe aquí tu respuesta");
            txtOpcion.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Toast.makeText(context, txtOpcion.getText(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}