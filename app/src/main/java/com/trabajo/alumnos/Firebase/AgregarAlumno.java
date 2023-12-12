package com.trabajo.alumnos.Firebase;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trabajo.alumnos.R;

public class AgregarAlumno extends AppCompatActivity {
    private EditText etNombre, etTel1, etTel2,etDireccion,etNotas,etId;
    private CheckBox cbFavorito;
    private Button   btnGuardar,btnActualizar,btnEliminar;
    private String urlFoto;
    int REQUEST_CODE = 200;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_alumno);

        etId = findViewById(R.id.editTextId);
        etTel1 = findViewById(R.id.editTextNumero1);
        etNombre = findViewById(R.id.editTextNombre);
        etTel2 = findViewById(R.id.editTextNumero2);
        etDireccion = findViewById(R.id.editTextDireccion);
        etNotas = findViewById(R.id.editTextNotas);
        cbFavorito=findViewById(R.id.checkBoxFavorito);

        btnGuardar=findViewById(R.id.botonGuardar);
        btnActualizar = findViewById(R.id.botonActualizar);
        btnEliminar=findViewById(R.id.botonEliminar);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String id = intent.getStringExtra("Id");
            String nombre = intent.getStringExtra("Nombre");
            String tel1 = intent.getStringExtra("Tel1");
            String tel2 = intent.getStringExtra("Tel2");
            String direccion = intent.getStringExtra("Direccion");
            String favorito = intent.getStringExtra("Favorito");
            String notas = intent.getStringExtra("Nota");

            if (favorito=="1"){
                cbFavorito.setChecked(true);
            }else {
                cbFavorito.setChecked(false);
            }

            etId.setText(id);
            etNombre.setText(nombre);
            etTel1.setText(tel1);
            etTel2.setText(tel2);
            etDireccion.setText(direccion);
            etNotas.setText(notas);
            btnGuardar.setVisibility(View.INVISIBLE);
            btnActualizar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
        }
        else {
            btnGuardar.setVisibility(View.VISIBLE);
            btnActualizar.setVisibility(View.INVISIBLE);
            btnEliminar.setVisibility(View.INVISIBLE);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=etId.getText().toString();
                String nombre = etNombre.getText().toString();
                String tel1 = etTel1.getText().toString();
                String tel2 = etTel2.getText().toString();
                String direccion =etDireccion.getText().toString();
                String favorito;
                String notas = etNotas.getText().toString();

                if(cbFavorito.isChecked()){
                    favorito="1";
                }else {
                    favorito="0";
                }


                if (nombre.isEmpty() || tel1.isEmpty() || tel2.isEmpty() || direccion.isEmpty() || notas.isEmpty()||id.isEmpty()) {
                    Toast.makeText(AgregarAlumno.this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                } else {
                if (verificarInternet()){
                    insertarProducto(id,nombre,direccion,tel1, tel2,notas,favorito);
                    limpiarTF();
                    Toast.makeText(AgregarAlumno.this, "Producto Subido Exitosamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AgregarAlumno.this, "No hay conexión a Internet para subir el producto", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=etId.getText().toString();
                String nombre = etNombre.getText().toString();
                String tel1 = etTel1.getText().toString();
                String tel2 = etTel2.getText().toString();
                String direccion =etDireccion.getText().toString();
                String favorito;
                String notas = etNotas.getText().toString();

                if(cbFavorito.isChecked()){
                    favorito="1";
                }else {
                    favorito="0";
                }

                if (nombre.isEmpty() || tel1.isEmpty() || tel2.isEmpty() || direccion.isEmpty() || notas.isEmpty()||id.isEmpty()) {
                    Toast.makeText(AgregarAlumno.this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                }else {
                    if (verificarInternet()){
                        insertarProducto(id,nombre,direccion,tel1, tel2,notas,favorito);
                        limpiarTF();
                        Toast.makeText(AgregarAlumno.this, "Producto Actualizado Excitosamente", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AgregarAlumno.this, "No hay conexión a Internet para modificar el producto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = etId.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(AgregarAlumno.this);
                builder.setMessage("¿Desea eliminar este producto del servido?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (verificarInternet()) {
                                    eliminarProducto(codigo);
                                    finish();
                                    Toast.makeText(AgregarAlumno.this, "Producto Eliminado Exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AgregarAlumno.this, "No hay conexión a Internet para eliminar el producto", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });


    }

    public void limpiarTF() {
        etId.setText("");
        etNombre.setText("");
        etTel1.setText("");
        etTel2.setText("");
        etDireccion.setText("");
        etNotas.setText("");
        cbFavorito.setChecked(false);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
    private boolean verificarInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void insertarProducto(String id,String nombre,String direccion,String tel1,String tel2,String notas,String favorito) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaRef = database.getReference("agenda");
        DatabaseReference contactoRef = agendaRef.child("contacto").child(id);
        Agenda nuevoContacto = new Agenda(id, nombre, direccion, tel1, tel2, notas,favorito);
        contactoRef.setValue(nuevoContacto);
    }


    private void eliminarProducto(String codigo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaRef = database.getReference("agenda");
        DatabaseReference contactoRef = agendaRef.child("contacto");
        DatabaseReference registro = contactoRef.child(codigo);
        registro.removeValue();
    }

}