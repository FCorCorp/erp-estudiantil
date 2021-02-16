/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.*;
import Modelo.Maquina;
import Vista.VCaracteristicas;
import Vista.VLinea;
import Vista.VListaRepuestos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Facundo Cordoba
 */
public class CLinea implements ActionListener, MouseListener{
    
    VLinea vistaLinea = new VLinea();
    ArrayList<Maquina> maquinasLinea = new ArrayList<Maquina>();
    VCaracteristicas vistaCaracteristicas = new VCaracteristicas();
    ArrayList<Caracteristica> caracteristicasMaquina = new ArrayList<Caracteristica>();
    ArrayList<Caracteristica> caracteristicasMaquinaTemp = new ArrayList<Caracteristica>();
    String codigoLinea;
    CArbol contArbol;
    Connection con;
    CConnection controladorConexion = new CConnection();
    //VListaRepuestos vRepuesto = new VListaRepuestos();
    String codigoRepuesto;

    String elementoSeleccionado="";
    
    public CLinea(String codigoLinea, CArbol arbol, Connection dbconnection){
        vistaLinea.setTitle("Linea: "+codigoLinea);
        vistaLinea.setVisible(true);
        vistaLinea.getBtnSalir().addActionListener(this);
        vistaLinea.getBtnAgregar().addActionListener(this);
        vistaLinea.getBtnEliminar().addActionListener(this);
        vistaLinea.getBtnEditar().addActionListener(this);
        vistaLinea.getBtnCaracteristicas().addActionListener(this);
        vistaLinea.getBtnBuscar().addActionListener(this);
        contArbol = arbol;
        this.codigoLinea = codigoLinea;
        con = dbconnection;
        crearArbol();
        deshabilitarCampos();
        
        vistaCaracteristicas.getBtnAceptar().addActionListener(alCaracteristicas);
        vistaCaracteristicas.getBtnAgregar().addActionListener(alCaracteristicas);
        vistaCaracteristicas.getBtnCancelar().addActionListener(alCaracteristicas);
        vistaCaracteristicas.getBtnEliminar().addActionListener(alCaracteristicas);

    }
    
    public void crearArbol(){
        cargarMaquinas(this.codigoLinea);
        vistaLinea.getjTree1().setModel(cargarArbol2(this.codigoLinea));
        vistaLinea.getjTree1().addMouseListener(this);
    }
    
    public DefaultTreeModel cargarArbol2(String codigoLinea){
        DefaultTreeModel arbol = new DefaultTreeModel(cargarNodos(codigoLinea));
        return arbol;
    }
    
    public void cargarMaquinas(String codigoLinea){
        
        ArrayList <String[]> maquinas = controladorConexion.obtenerMaquinas(con, codigoLinea);
        maquinasLinea.clear();
        
        for(int i=0; i<maquinas.size(); i++){
            maquinasLinea.add(transformarAMaquina(maquinas.get(i)));
        }
        
    }
    
    public Maquina transformarAMaquina(String[] obj){
        Maquina maquina = new Maquina();
        maquina.setCodigo(obj[1]);
        maquina.setCodigoPadre(obj[2]);
        maquina.setCodigoLinea(obj[3]);
        maquina.setDescripcion(obj[4]);
        maquina.setTipo(obj[5]);
        
        return maquina;
    }
    
    //CREAR ARBOL DE NAVEGACION
    public DefaultMutableTreeNode cargarNodos(String padre){
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(padre);
        for(int i=0; i<maquinasLinea.size(); i++){
            if(maquinasLinea.get(i).getCodigoPadre().equals(padre)){
                nodoPadre.add(cargarNodos(maquinasLinea.get(i).getCodigo()));
            }
        }
        return nodoPadre;
    }
    
    public void cargarCaracteristicas(String codigoLinea, String codigoMaquina, JTable tabla){
        //Buscar caracteristicas en DB y cargar en variable
        caracteristicasMaquina.clear();
        ArrayList <String[]> caracteristicas = controladorConexion.obtenerCaracteristicas(con, codigoLinea, codigoMaquina);
        caracteristicasMaquina.clear();
        for(int i=0; i<caracteristicas.size(); i++){
            caracteristicasMaquina.add(transformarACaracteristica(caracteristicas.get(i)));
        }
        
        //Crear Tabla de caracteristicas con elementos de la variable
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        for(int i=0; i<caracteristicasMaquina.size(); i++){
             model.addRow(new Object[]{
                 caracteristicasMaquina.get(i).getNombre(), 
                 caracteristicasMaquina.get(i).getAbreviacion(), 
                 caracteristicasMaquina.get(i).getValor(), 
                 caracteristicasMaquina.get(i).getUnidad()
             });
        }
    }
    
    
    public Caracteristica transformarACaracteristica(String[] obj){
        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setCodigoLinea(obj[1]);
        caracteristica.setCodigoMaquina(obj[2]);
        caracteristica.setNombre(obj[3]);
        caracteristica.setAbreviacion(obj[4]);
        caracteristica.setValor(obj[5]);
        caracteristica.setUnidad(obj[6]);
        
        return caracteristica;
    }
    
    public void completarCampos(){
        limpiarCampos();
        for(Maquina maquina: maquinasLinea){
            if(maquina.getCodigo().equals(elementoSeleccionado)){
                vistaLinea.getCampoCodMaquina().setText(maquina.getCodigo());
                vistaLinea.getCampoCodPadre().setText(maquina.getCodigoPadre());
                vistaLinea.getjComboBox1().setSelectedItem(maquina.getTipo());
                vistaLinea.getCampoCodRepuesto().setText(maquina.getCodigoRepuesto());
                vistaLinea.getCampoDescripcion().setText(maquina.getDescripcion());
            }
        }
        cargarCaracteristicas(codigoLinea, elementoSeleccionado, vistaLinea.getTablaCaracteristicas());
        cargarRepuesto();
    }
    
    public void cargarRepuesto(){
        ArrayList <String[]> listaAsignaciones = controladorConexion.buscarRepuestoAsignado(con, codigoLinea, elementoSeleccionado);
        codigoRepuesto = listaAsignaciones.get(0)[0];
        vistaLinea.getCampoCodRepuesto().setText(codigoRepuesto);
    };
    
    
    public void limpiarCampos(){
        vistaLinea.getCampoCodMaquina().setText("");
        vistaLinea.getCampoCodPadre().setText("");
        vistaLinea.getjComboBox1().setSelectedIndex(0);
        vistaLinea.getCampoCodRepuesto().setText("");
        vistaLinea.getCampoDescripcion().setText("");
    }
    
    public void deshabilitarCampos (){
        vistaLinea.getCampoCodMaquina().setEnabled(false);
        vistaLinea.getCampoCodPadre().setEnabled(false);
        vistaLinea.getjComboBox1().setEnabled(false);
        vistaLinea.getCampoCodRepuesto().setEnabled(false);
        vistaLinea.getCampoDescripcion().setEnabled(false);
    }
    
    public void habilitarCampos (){
        vistaLinea.getCampoCodMaquina().setEnabled(true);
        vistaLinea.getCampoCodPadre().setEnabled(true);
        vistaLinea.getjComboBox1().setEnabled(true);
        vistaLinea.getCampoCodRepuesto().setEnabled(true);
        vistaLinea.getCampoDescripcion().setEnabled(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Salir"){
            vistaLinea.setVisible(false);
            contArbol.getvPrincipal().enable(true);
            contArbol.getvPrincipal().setVisible(true);
        }
        if(e.getActionCommand() == "Agregar"){
            habilitarCampos();
            vistaLinea.getjTree1().setEnabled(false);
            vistaLinea.getBtnAgregar().setText("Aceptar");
            vistaLinea.getBtnEliminar().setText("Cancelar");
            vistaLinea.getBtnEditar().setEnabled(false);
            vistaLinea.getBtnCaracteristicas().setEnabled(false);
            vistaLinea.getBtnBuscar().setEnabled(false);
            
            limpiarCampos();
            vistaLinea.getCampoCodPadre().setText(elementoSeleccionado);
            vistaLinea.getCampoCodPadre().setEnabled(false);
            vistaLinea.getCampoCodRepuesto().setEnabled(false);
            vistaLinea.getBtnEliminar().setEnabled(true);
        }
        if(e.getActionCommand()=="Cancelar"){
            deshabilitarCampos();
            vistaLinea.getjTree1().setEnabled(true);
            vistaLinea.getBtnAgregar().setText("Agregar");
            vistaLinea.getBtnEliminar().setText("Eliminar");
            vistaLinea.getBtnEditar().setText("Editar");
            vistaLinea.getBtnEditar().setEnabled(true);
            vistaLinea.getBtnAgregar().setEnabled(true);
            vistaLinea.getBtnCaracteristicas().setEnabled(true);
            vistaLinea.getBtnBuscar().setEnabled(true);
            if(elementoSeleccionado.equals(codigoLinea)){
                vistaLinea.getBtnEliminar().setEnabled(false);
                vistaLinea.getBtnEditar().setEnabled(false);
            }
            completarCampos();
            
        }
        if(e.getActionCommand()=="Aceptar"){
            deshabilitarCampos();
            //System.out.println(vistaLinea.getCampoCodMaquina().toString());
            Maquina maquina = new Maquina(vistaLinea.getCampoCodMaquina().getText(),
                                          vistaLinea.getCampoCodPadre().getText(),
                                          codigoLinea,
                                          vistaLinea.getCampoDescripcion().getText(),
                                          vistaLinea.getjComboBox1().getSelectedItem().toString(),
                                          vistaLinea.getCampoCodRepuesto().getText());
            
            controladorConexion.agregarMaquina(con, maquina);
            limpiarCampos();
            elementoSeleccionado="";
            crearArbol();
            vistaLinea.getjTree1().setEnabled(true);
            vistaLinea.getBtnBuscar().setEnabled(true);
            vistaLinea.getBtnAgregar().setText("Agregar");
            vistaLinea.getBtnEliminar().setText("Eliminar");
            vistaLinea.getCampoCodPadre().setEnabled(false);
            
        }
        if(e.getActionCommand()=="Editar"){
            habilitarCampos();
            System.out.println("Entra a editar");
            vistaLinea.getjTree1().setEnabled(false);
            vistaLinea.getBtnAgregar().setEnabled(false);
            vistaLinea.getBtnCaracteristicas().setEnabled(false);
            vistaLinea.getBtnBuscar().setEnabled(false);
            vistaLinea.getBtnEditar().setText("Guardar");
            vistaLinea.getBtnEliminar().setText("Cancelar");
            vistaLinea.getCampoCodPadre().setEnabled(false);
            vistaLinea.getCampoCodMaquina().setEnabled(false);
            vistaLinea.getCampoCodRepuesto().setEnabled(false);
            
            
            
        }
        if(e.getActionCommand()=="Eliminar"){
            System.out.println(listaMaquinas(elementoSeleccionado));
            controladorConexion.eliminarMaquinas(con, codigoLinea, listaMaquinas(elementoSeleccionado));
            controladorConexion.eliminarAsignacionRepuesto(con, codigoLinea, elementoSeleccionado, codigoRepuesto);
            crearArbol();
            
            
            
        }
        if(e.getActionCommand()=="Guardar"){
            deshabilitarCampos();
            Maquina maquina = new Maquina(vistaLinea.getCampoCodMaquina().getText(),
                                          vistaLinea.getCampoCodPadre().getText(),
                                          codigoLinea,
                                          vistaLinea.getCampoDescripcion().getText(),
                                          vistaLinea.getjComboBox1().getSelectedItem().toString(),
                                          vistaLinea.getCampoCodRepuesto().getText());
            
            controladorConexion.actualizarMaquina(con, maquina);
            crearArbol();
            vistaLinea.getjTree1().setEnabled(true);
            vistaLinea.getBtnEditar().setText("Editar");
            vistaLinea.getBtnEliminar().setText("Eliminar");
            vistaLinea.getBtnAgregar().setEnabled(true);
            vistaLinea.getBtnCaracteristicas().setEnabled(true);
            vistaLinea.getBtnBuscar().setEnabled(true);
            
        }
        if(e.getActionCommand()=="Gestionar Caracteristicas"){
            DefaultTableModel dtm = (DefaultTableModel) vistaCaracteristicas.getjTable1().getModel();
            dtm.setRowCount(0);
            vistaCaracteristicas.getFieldNombre().setText("");
            vistaCaracteristicas.getFieldAbreviacion().setText("");
            vistaCaracteristicas.getFieldValor().setText("");
            vistaCaracteristicas.getFieldUnidad().setText("");
            cargarCaracteristicas(codigoLinea, elementoSeleccionado, vistaCaracteristicas.getjTable1());
            vistaCaracteristicas.setVisible(true);
            vistaLinea.setEnabled(false);
        }
        if(e.getActionCommand()=="Buscar"){
            System.out.println("Buscar repuesto");
            vistaLinea.setEnabled(false);
            vRepuesto.setVisible(true);
        }
        
                
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    ActionListener alCaracteristicas = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(e.getActionCommand()=="Agregar"){

                DefaultTableModel model = (DefaultTableModel) vistaCaracteristicas.getjTable1().getModel();
                model.addRow(new Object[]{  vistaCaracteristicas.getFieldNombre().getText(),
                                            vistaCaracteristicas.getFieldAbreviacion().getText(), 
                                            vistaCaracteristicas.getFieldValor().getText(),
                                            vistaCaracteristicas.getFieldUnidad().getText()
                                            });
            }
            if(e.getActionCommand()=="Eliminar"){
                //System.out.println("Eliminar");
                //System.out.println(vistaCaracteristicas.getjTable1().getSelectedRow());
                ((DefaultTableModel)vistaCaracteristicas.getjTable1().getModel()).removeRow(vistaCaracteristicas.getjTable1().getSelectedRow());

            }
            if(e.getActionCommand()=="Aceptar"){
                System.out.println("Aceptar");
                
                //Crear lista de caracteristicas a partir de tabla
                caracteristicasMaquina.clear();
                int numeroFilas = vistaCaracteristicas.getjTable1().getRowCount();
                for(int i=0; i<numeroFilas; i++){
                    
                    Caracteristica carNueva= new Caracteristica(
                            codigoLinea,
                            elementoSeleccionado,
                            vistaCaracteristicas.getjTable1().getValueAt(i, 0).toString(),
                            vistaCaracteristicas.getjTable1().getValueAt(i, 1).toString(),
                            vistaCaracteristicas.getjTable1().getValueAt(i, 2).toString(),
                            vistaCaracteristicas.getjTable1().getValueAt(i, 3).toString()
                    );
                    caracteristicasMaquina.add(carNueva);
                }
                //Eliminar caracteristicas viejas
                controladorConexion.eliminarCaracteristicas(con, codigoLinea, elementoSeleccionado);
                //Guardar las nuevas caracteristicas
                controladorConexion.agregarCaracteristicas(con, caracteristicasMaquina);
                //Cargar caracteristicas en tabla de lista de maquinas
                //Mostrar-habilitar vista de arbol de maquinas
                vistaCaracteristicas.setVisible(false);
                cargarCaracteristicas(codigoLinea, elementoSeleccionado, vistaLinea.getTablaCaracteristicas());
                vistaLinea.setEnabled(true);
                vistaLinea.setVisible(true);
            }
            if(e.getActionCommand()=="Cancelar"){
                System.out.println("Cancelar");
                vistaCaracteristicas.setVisible(false);
                vistaLinea.setEnabled(true);
                vistaLinea.setVisible(true);
            }
            
            
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
    ActionListener alRepuestos  = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(e.getActionCommand().equals("Seleccionar")){
                //Guardar en DB asociacion entre la maquina y el repuesto
                int fila = vRepuesto.getTbLista().getSelectedRow();
                String codRepuesto = vRepuesto.getTbLista().getModel().getValueAt(fila, 0).toString();
                controladorConexion.asignarRepuesto(con, codigoLinea, elementoSeleccionado, codigoRepuesto);
                //En campo del repuesto mostrar el codigo o nombre del repuesto y habilitar vista de arbol de maquinas
                vRepuesto.setVisible(false);
                vistaLinea.setEnabled(true);
                vistaLinea.getCampoCodRepuesto().setText(codRepuesto);
            }
            
            if(e.getActionCommand().equals("Quitar repuesto")){
                //Eliminar asignacion de DB
                controladorConexion.eliminarAsignacionRepuesto(con, codigoLinea, elementoSeleccionado, codigoRepuesto);
                //Mostrar vista de arbol de maquinas
                vRepuesto.setVisible(false);
                vistaLinea.setEnabled(true);
                vistaLinea.getCampoCodRepuesto().setText("");
            }
            
            if(e.getActionCommand().equals("Salir")){
                vRepuesto.setVisible(false);
                vistaLinea.setEnabled(true);
            }
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    };
    
    
    public ArrayList<String> listaMaquinas(String padre){
        ArrayList<String> listaCodigosMaquinas = new ArrayList<>();
        listaCodigosMaquinas.add(padre);
        for(int i=0; i<maquinasLinea.size(); i++){
            if(maquinasLinea.get(i).getCodigoPadre().equals(padre)){
                listaCodigosMaquinas.addAll(listaMaquinas(maquinasLinea.get(i).getCodigo()));
                
            }
        }
        return listaCodigosMaquinas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = vistaLinea.getjTree1().getRowForLocation(e.getX(), e.getY());
        TreePath selPath = vistaLinea.getjTree1().getPathForLocation(e.getX(), e.getY());
        elementoSeleccionado = selPath.getLastPathComponent().toString();
        //System.out.println(selPath.getLastPathComponent().toString());
        if(!elementoSeleccionado.equals(codigoLinea)){
            completarCampos();
            vistaLinea.getBtnEliminar().setEnabled(true);
            vistaLinea.getBtnEditar().setEnabled(true);
        }else{
            limpiarCampos();
            cargarCaracteristicas(codigoLinea, elementoSeleccionado,vistaLinea.getTablaCaracteristicas());
            vistaLinea.getCampoCodMaquina().setText(codigoLinea);
            vistaLinea.getCampoCodPadre().setText(codigoLinea);
            vistaLinea.getBtnEliminar().setEnabled(false);
            vistaLinea.getBtnEditar().setEnabled(false);
            cargarRepuesto();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    //Action listener de vista de gestion de repuestos
    

}
