package com.example.appformularios.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appformularios.Entidades.Formulario;
import com.example.appformularios.InformeActivity;
import com.example.appformularios.R;
import com.example.appformularios.VistaFormularioActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FormularioAdapter extends RecyclerView.Adapter<FormularioAdapter.ViewHolder> {

    private List<Formulario> items;
    private LayoutInflater inflater;
    private Context context;
    private int position;


    public FormularioAdapter(List<Formulario> formularios, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setItems(formularios);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @NonNull
    @Override
    public FormularioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rv_forms, null);
        return new FormularioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormularioAdapter.ViewHolder holder, int position) {
        holder.bindData(items.get(position));
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                setSelectedItem(pos);
            }
        });
    }

    private void setSelectedItem(int pos) {
        this.position = pos;
    }

    private Formulario getSelectedItem() {
        return items.get(this.position);
    }

    public Context getContext() {
        return context;
    }

    public void setItems(List<Formulario> items) {
        this.items = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {
        TextView tvFormulario;
        TextView tvDescripcion;
        LongClickListener longClickListener;
        private FirebaseAuth auth;
        private DatabaseReference mDatabase;

        ViewHolder(View view) {
            super(view);
            auth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            tvFormulario = view.findViewById(R.id.tvTituloFormulario);
            tvDescripcion = view.findViewById(R.id.tvDescripcionFormulario);
            view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        void bindData(final Formulario item) {
            tvFormulario.setText(item.getId() + ". " + item.getTitulo());
            tvDescripcion.setText(item.getDescripcion());
        }

        public void setLongClickListener(LongClickListener longClickListener) {
            this.longClickListener = longClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            return false;
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selecciona una opción");
            MenuItem edit = menu.add(this.getAdapterPosition(), 1, 1, "Editar");
            MenuItem delete = menu.add(this.getAdapterPosition(), 2, 2, "Eliminar");
            MenuItem see = menu.add(this.getAdapterPosition(), 3, 3, "Visualizar");
            MenuItem share = menu.add(this.getAdapterPosition(), 4, 4, "Compartir");
            MenuItem report = menu.add(this.getAdapterPosition(), 5, 5, "Crear informe");
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
            see.setOnMenuItemClickListener(onEditMenu);
            share.setOnMenuItemClickListener(onEditMenu);
            report.setOnMenuItemClickListener(onEditMenu);

        }
        final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final String user_id;
                switch (item.getItemId()){
                    case 1:
                        Toast.makeText(FormularioAdapter.this.context,
                                "Editar: " + FormularioAdapter.this.getSelectedItem().getTitulo(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        user_id = new String(auth.getCurrentUser().getUid());
                        String id = FormularioAdapter.this.getSelectedItem().getId();
                        mDatabase.child("Usuarios").child(user_id).child("Formularios").child(id).removeValue();
                        Toast.makeText(FormularioAdapter.this.context,
                                FormularioAdapter.this.getSelectedItem().getTitulo() + " eliminado",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(FormularioAdapter.this.context,
                                "Visualizar: " + FormularioAdapter.this.getSelectedItem().getTitulo(),
                                Toast.LENGTH_SHORT).show();
                        int id_form = Integer.parseInt(FormularioAdapter.this.getSelectedItem().getId());
                        Intent intent = new Intent(getContext(), VistaFormularioActivity.class);
                        intent.putExtra("id_form", id_form);
                        getContext().startActivity(intent);
                        break;
                    case 4:
                        user_id = new String(auth.getCurrentUser().getUid());
                        String codigo = "Para acceder al formulario: "
                                + FormularioAdapter.this.getSelectedItem().getTitulo()
                                + " ingrese el siguiente código: " + user_id + "-"
                                + FormularioAdapter.this.getSelectedItem().getId() + "-"
                                + FormularioAdapter.this.getSelectedItem().getTitulo();
                        //Toast.makeText(FormularioAdapter.this.context, "ID: " + codigo, Toast.LENGTH_SHORT).show();
                        Intent compartir = new Intent(Intent.ACTION_SEND);
                        compartir.setType("text/plain");
                        compartir.putExtra(Intent.EXTRA_SUBJECT, "WhatsApp");
                        compartir.putExtra(Intent.EXTRA_TEXT, codigo);
                        context.startActivity(compartir);
                        break;
                    case 5:
                        Toast.makeText(FormularioAdapter.this.context,
                                "Crear informe: " + FormularioAdapter.this.getSelectedItem().getTitulo(),
                                Toast.LENGTH_SHORT).show();
                        int id_form2 = Integer.parseInt(FormularioAdapter.this.getSelectedItem().getId());
                        Intent informe = new Intent(getContext(), InformeActivity.class);
                        informe.putExtra("id_form", id_form2);
                        getContext().startActivity(informe);
                        break;
                    default:
                        break;
                }
                return true;
            }
        };

    }
}
