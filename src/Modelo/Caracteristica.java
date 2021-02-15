/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Facundo Cordoba
 */
public class Caracteristica {
    
    private String codigoLinea;
    private String codigoMaquina;
    private String nombre;
    private String abreviacion;    
    private String valor;
    private String unidad;

    public Caracteristica() {
    }

    public Caracteristica(String codigoLinea, String codigoMaquina, String nombre, String abreviacion, String valor, String unidad) {
        this.codigoLinea = codigoLinea;
        this.codigoMaquina = codigoMaquina;
        this.nombre = nombre;
        this.abreviacion = abreviacion;
        this.valor = valor;
        this.unidad = unidad;
    }

    public String getCodigoLinea() {
        return codigoLinea;
    }

    public void setCodigoLinea(String codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    public String getCodigoMaquina() {
        return codigoMaquina;
    }

    public void setCodigoMaquina(String codigoMaquina) {
        this.codigoMaquina = codigoMaquina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    
    
    
}
