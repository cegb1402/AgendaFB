package com.trabajo.alumnos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trabajo.alumnos.Firebase.Adapter;
import com.trabajo.alumnos.Firebase.AgregarAlumno;
import com.trabajo.alumnos.Firebase.Agenda;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView svBuscar;
    private Adapter contactosAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private List<Agenda> alumnoLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnLogout = findViewById(R.id.btnLogoutFB);
        FloatingActionButton btnAgregarProducto = findViewById(R.id.btnAgregarProductoFB);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutFB);

        Toolbar toolbar = findViewById(R.id.toolbarFB);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recIdFB);

        svBuscar = findViewById(R.id.searchFB);
        svBuscar.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        alumnoLista = new ArrayList<>();
        contactosAdapter = new Adapter(MainActivity.this, alumnoLista);
        recyclerView.setAdapter(contactosAdapter);

        if (verificarInternet() == false) {
            Toast.makeText(this, "No tienes acceso a internet para mostrar los productos", Toast.LENGTH_SHORT).show();
        }
        obtenerDatosDeFirebase();

        svBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (verificarInternet()) {
                    obtenerDatosDeFirebase();
                } else {
                    Toast.makeText(MainActivity.this, "Debes tener conexión a internet para mostrar los productos", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAgregarAlumno = new Intent(MainActivity.this, AgregarAlumno.class);
                startActivity(intentAgregarAlumno);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
        confirmar.setTitle("Salir");
        confirmar.setMessage("¿Desea salir de la aplicación?");
        confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        confirmar.show();
    }

    private boolean verificarInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void searchList(String text) {
        ArrayList<Agenda> searchList = new ArrayList<>();
        for (Agenda dataClass : alumnoLista) {
            if (dataClass.getNombre().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        contactosAdapter.searchDataList(searchList);
    }
    public void obtenerDatosDeFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("agenda");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alumnoLista.clear();

                if (snapshot.child("contacto").exists()) {
                    for (DataSnapshot itemSnapshot : snapshot.child("contacto").getChildren()) {
                        Agenda contacto = itemSnapshot.getValue(Agenda.class);
                        if (contacto != null) {
                            contacto.set_ID(itemSnapshot.getKey());
                            alumnoLista.add(contacto);
                        }
                    }

                    // Notifica al adaptador que los datos han cambiado
                    contactosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error si es necesario
            }
        });
    }



}
