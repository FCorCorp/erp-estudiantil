/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulomaquinas;
import Controlador.*;
import Modelo.*;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Facundo Cordoba
 */
public class ModuloMaquinas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //CREACION DE DATOS PREVIOS
        Planta plantaSM = new Planta("SM" , "Planta San Miguel");
        Planta plantaCBA = new Planta("CBA" , "Planta San Miguel");
        Proceso proLlenado = new Proceso("SM-LLNDO", "Llenado");
        Proceso proLimpieza = new Proceso("CBA-LIMP", "Limpieza");
        Tarea tarea1 = new Tarea("SM-LLND-TRANSP1", "Transporte1");
        Tarea tarea2 = new Tarea("SM-LLND-INY", "Inyeccion de producto");
        Tarea tarea3 = new Tarea("CBA-LIMP-EXT", "Limpieza externa");
        Tarea tarea4 = new Tarea("CBA-LIMP-INT", "Limpieza interna");
        
        tarea1.addLinea(new Linea("SM-LLND-TRANSP1-L01", "Linea1"));
        tarea2.addLinea(new Linea("SM-LLND-INY-L01", "Linea1"));
        tarea3.addLinea(new Linea("CBA-LIMP-EXT-L01", "Linea1"));
        tarea4.addLinea(new Linea("CBA-LIMP-INT-L01", "Linea1"));
        
        proLlenado.addTareas(tarea1); proLlenado.addTareas(tarea2);
        proLimpieza.addTareas(tarea3); proLimpieza.addTareas(tarea4);
        
        plantaSM.addProcesos(proLlenado);
        plantaCBA.addProcesos(proLimpieza);
        
        ArrayList<Planta> plantas = new ArrayList<Planta>();
        plantas.add(plantaSM); plantas.add(plantaCBA);
        
        //Simulacion de la base de datos de las maquinas
        ArrayList<Maquina> maquina = new ArrayList<Maquina>();
        
        //Conexion a base de datos
        CConnection conexion = new CConnection();
        Connection con = conexion.getConection();
        
        
        //INICIALIZACION
        CArbol controlador = new CArbol(plantas, con);
        controlador.getvPrincipal().setVisible(true);
        
        
        
    }
    
}
