/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.Caracteristica;
import java.util.ArrayList;

/**
 *
 * @author Facundo Cordoba
 */
public class Maquina {
    private String codigo;
    private String codigoPadre;
    private String codigoLinea;
    private String descripcion;
    private String tipo;
    private String codigoRepuesto;
    private ArrayList<Maquina> maquinas = new ArrayList<Maquina>();
    private ArrayList<Caracteristica> listaCaracteristica = new ArrayList<Caracteristica>();

    public Maquina() {
    }

    public Maquina(String codigo, String codigoPadre, String codigoLinea, String descripcion, String tipo, String codigoRepuesto) {
        this.codigo = codigo;
        this.codigoPadre = codigoPadre;
        this.codigoLinea = codigoLinea;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.codigoRepuesto = codigoRepuesto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPadre() {
        return codigoPadre;
    }

    public void setCodigoPadre(String codigoPadre) {
        this.codigoPadre = codigoPadre;
    }

    public String getCodigoLinea() {
        return codigoLinea;
    }

    public void setCodigoLinea(String codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Maquina> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(ArrayList<Maquina> maquinas) {
        this.maquinas = maquinas;
    }
    
    public void addMaquina(Maquina maquina){
        maquinas.add(maquina);
    }

    public String getCodigoRepuesto() {
        return codigoRepuesto;
    }

    public void setCodigoRepuesto(String codigoRepuesto) {
        this.codigoRepuesto = codigoRepuesto;
    }

    public ArrayList<Caracteristica> getListaCaracteristica() {
        return listaCaracteristica;
    }

    public void setListaCaracteristica(ArrayList<Caracteristica> listaCaracteristica) {
        this.listaCaracteristica = listaCaracteristica;
    }
    
    public void addListaCaracteristica(Caracteristica caract){
        this.listaCaracteristica.add(caract);
    }
    

    @Override
    public String toString() {
        return "Maquina{" + "codigo=" + codigo + ", codigoPadre=" + codigoPadre + ", codigoLinea=" + codigoLinea + ", descripcion=" + descripcion + ", tipo=" + tipo + '}';
    }
    
}
