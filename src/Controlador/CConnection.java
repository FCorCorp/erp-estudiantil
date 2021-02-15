/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Caracteristica;
import Modelo.Maquina;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/**
 *
 * @author Facundo Cordoba
 */
public class CConnection {
    public static final String ULR = "jdbc:mysql://localhost:3306/MaquinasDB";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    //EN CASO QUE EXISTA UN ERROR EN LA HORA DEL SERVIDOR; EJECUTAR LA SIGUIENTE QUERY MANUALMENTE
    //
    //           SET GLOBAL time_zone = '-3:00';

    public static Connection getConection(){
         Connection con = null;
         try{
             Class.forName("com.mysql.cj.jdbc.Driver");
             con = DriverManager.getConnection(ULR,USERNAME,PASSWORD); //si da problemas al conectar ejecutar la siguiente query en tu db: SET GLOBAL time_zone = '-3:00';
             System.out.println("Conexion exitosa");
         }catch(Exception e){
             System.out.println(e);
         }
         
         return con;
    }
    
    PreparedStatement ps;
    ResultSet res;

    
    public ArrayList <String[]> obtenerMaquinas(Connection con, String codigoLinea){
        
        ArrayList <String[]> results = new ArrayList<String[]>();
        try{
            ps = con.prepareStatement("SELECT * FROM maquina WHERE codigoLinea='"+codigoLinea+"'");
            res = ps.executeQuery();
            ResultSetMetaData rsMd = res.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            while(res.next()){
                String[] fila = new String[cantidadColumnas];
                for(int i=0; i<cantidadColumnas; i++){
                    fila[i] = res.getString(i+1);
                }
                results.add(fila);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return results;
    }
    
    public void agregarMaquina(Connection con, Maquina maquina){
        try{
            //System.out.println(maquina.getCodigo().length());
            //System.out.println(maquina.getCodigo());
            ps = con.prepareStatement("INSERT INTO maquina (codigo, codigoPadre, codigoLinea, descripcion, caracteristicas, tipo) VALUES(?,?,?,?,?,?)");
            ps.setString(1, maquina.getCodigo());
            ps.setString(2, maquina.getCodigoPadre());
            ps.setString(3, maquina.getCodigoLinea());
            ps.setString(4, maquina.getDescripcion());
            ps.setString(5, "");
            ps.setString(6, maquina.getTipo());
            
            int res = ps.executeUpdate();
            if(res>0){
                System.out.println("Maquina guardada correctamente");
            } else{
                System.out.println("Error al guardar maquina");
            }
            
            
        } catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void eliminarMaquinas(Connection con, String codigoLinea, ArrayList<String> listaCodigosMaquinas){
        try {
            for(int i=0; i<listaCodigosMaquinas.size(); i++){
                ps = con.prepareStatement("DELETE FROM maquina WHERE codigoLinea=? AND codigo=?");
                ps.setString(1, codigoLinea);
                ps.setString(2, listaCodigosMaquinas.get(i));
                
                int res = ps.executeUpdate();
                if(res>0){
                    System.out.println("Maquina eliminada correctamente: "+listaCodigosMaquinas.get(i));
                } else{
                    System.out.println("Error al eliminar maquina: "+listaCodigosMaquinas.get(i));
                }
                
                eliminarCaracteristicas(con, codigoLinea, listaCodigosMaquinas.get(i));
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void actualizarMaquina(Connection con, Maquina maquina){
        try {
            ps= con.prepareStatement("UPDATE maquina SET descripcion=?, caracteristicas=?, tipo=? WHERE codigo=?");
            ps.setString(1, maquina.getDescripcion());
            ps.setString(2, "");
            ps.setString(3, maquina.getTipo());
            ps.setString(4, maquina.getCodigo());
            
            int res = ps.executeUpdate();
            if(res>0){
                System.out.println("Maquina modificada correctamente");
            } else{
                System.out.println("Error al modificar maquina");
            }
                
        } catch (Exception e) {
        }
    }
    
    
    public ArrayList <String[]> obtenerCaracteristicas(Connection con, String codigoLinea, String codigoMaquina){
        //System.out.println("Query: "+"SELECT * FROM caracteristica WHERE codigoLinea='"+codigoLinea+"' AND codigoMaquina='"+codigoMaquina+"'");
        
        ArrayList <String[]> results = new ArrayList<String[]>();
        try{
            ps = con.prepareStatement("SELECT * FROM caracteristica WHERE codigoLinea='"+codigoLinea+"' AND codigoMaquina='"+codigoMaquina+"'");
            res = ps.executeQuery();
            ResultSetMetaData rsMd = res.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            while(res.next()){
                String[] fila = new String[cantidadColumnas];
                for(int i=0; i<cantidadColumnas; i++){
                    fila[i] = res.getString(i+1);
                }
                results.add(fila);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return results;
    }
    
    public void agregarCaracteristicas(Connection con, ArrayList<Caracteristica> nuevasCaracteristicas){
        try{
            
            for(int i=0;i<nuevasCaracteristicas.size();i++){
                ps = con.prepareStatement("INSERT INTO caracteristica (codigoLinea, codigoMaquina, nombre, abreviacion, valor, unidad) VALUES(?,?,?,?,?,?)");
                ps.setString(1, nuevasCaracteristicas.get(i).getCodigoLinea());
                ps.setString(2, nuevasCaracteristicas.get(i).getCodigoMaquina());
                ps.setString(3, nuevasCaracteristicas.get(i).getNombre());
                ps.setString(4, nuevasCaracteristicas.get(i).getAbreviacion());
                ps.setString(5, nuevasCaracteristicas.get(i).getValor());
                ps.setString(6, nuevasCaracteristicas.get(i).getValor());
                int res = ps.executeUpdate();
                if(res>0){
                    System.out.println("Caracteristicas guardadas correctamente");
                } else{
                    System.out.println("Error al agregar caracteristica");
                }
            }
            
        } catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void eliminarCaracteristicas(Connection con, String codigoLinea, String codigoMaquina){
        try {
            
            ps = con.prepareStatement("DELETE FROM caracteristica WHERE codigoLinea='"+codigoLinea+"' AND codigoMaquina='"+codigoMaquina+"'");

            int res = ps.executeUpdate();
            if(res>0){
                System.out.println("Caracteristicas eliminadas");
            } else{
                System.out.println("Error al eliminar caracteristicas");
            }
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
    };
    
    
}


