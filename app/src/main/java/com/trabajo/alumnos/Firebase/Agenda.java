package com.trabajo.alumnos.Firebase;

public class Agenda {
    private String _ID;
    private String nombre;
    private String domicilio;
    private String telefono1;
    private String telefono2;
    private String notas;
    private String favorito;

    public Agenda() {

    }

    public Agenda(String _ID, String nombre, String domicilio, String telefono1, String telefono2, String notas, String favorito) {
        this._ID = _ID;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.notas = notas;
        this.favorito = favorito;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getFavorito() {
        return favorito;
    }

    public void setFavorito(String favorito) {
        this.favorito = favorito;
    }
}
