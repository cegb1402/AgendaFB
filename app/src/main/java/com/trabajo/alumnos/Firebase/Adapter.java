package com.trabajo.alumnos.Firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.alumnos.R;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ProductosViewHolder> {

    private Context context;
    private List<Agenda> alumnoList;

    public Adapter(Context context, List<Agenda> alumnoList) {
        this.context = context;
        this.alumnoList = alumnoList;
    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alumnos_card, parent, false);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
        final int currentPosition = position;
        holder.txtNombre.setText(alumnoList.get(currentPosition).getNombre());
        holder.txtTel1.setText(alumnoList.get(currentPosition).getTelefono1());
        holder.txtTel2.setText(alumnoList.get(currentPosition).getTelefono2());
        holder.txtNota.setText(alumnoList.get(currentPosition).getNotas());
        holder.txtDireccion.setText(alumnoList.get(currentPosition).getDomicilio());
        if ("0".equals(alumnoList.get(currentPosition).getFavorito())) {
            holder.ivFavorito.setImageResource(R.drawable.outline_favorite_border_black_24dp);
        } else {
            holder.ivFavorito.setImageResource(R.drawable.outline_favorite_black_24dp);
        }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AgregarAlumno.class);
                intent.putExtra("Id", alumnoList.get(currentPosition).get_ID());
                intent.putExtra("Nombre", alumnoList.get(currentPosition).getNombre());
                intent.putExtra("Tel1", alumnoList.get(currentPosition).getTelefono1());
                intent.putExtra("Tel2", alumnoList.get(currentPosition).getTelefono2());
                intent.putExtra("Nota", alumnoList.get(currentPosition).getNotas());
                intent.putExtra("Direccion", alumnoList.get(currentPosition).getDomicilio());
                intent.putExtra("Favorito", alumnoList.get(currentPosition).getFavorito());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alumnoList.size();
    }
    public void searchDataList(ArrayList<Agenda> searchList){
        alumnoList = searchList;
        notifyDataSetChanged();
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtTel1, txtTel2, txtNota,txtDireccion;
        ImageView ivFavorito;
        CardView cardView;

        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFavorito = itemView.findViewById(R.id.imageViewFavorito);
            cardView = itemView.findViewById(R.id.cv);
            txtNombre = itemView.findViewById(R.id.textViewNombre);
            txtTel1 = itemView.findViewById(R.id.textViewTelefono1);
            txtTel2 = itemView.findViewById(R.id.textViewTelefono2);
            txtNota = itemView.findViewById(R.id.textViewNotas);
            txtDireccion = itemView.findViewById(R.id.textViewDireccion);
        }
    }
}


