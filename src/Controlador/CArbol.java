/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Vista.*;
import Modelo.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
/**
 *
 * @author Facundo Cordoba
 */
public class CArbol implements MouseListener, ActionListener{
    //
    String nombreLinea = "";
    //
    Vmain vPrincipal = new Vmain();
    ArrayList<Planta> plantas = new ArrayList();
    Connection con;
    
    
    public CArbol(ArrayList<Planta> plantas,Connection dbconnection){
        this.plantas = plantas;
        vPrincipal.cargarArbol(cargarArbol());
        vPrincipal.getjTree1().addMouseListener(this);
        vPrincipal.getBtnInspeccionar().addActionListener(this);
        vPrincipal.getBtnInspeccionar().setEnabled(false);
        vPrincipal.getBtnSalir().addActionListener(this);
        con = dbconnection;
    }

    public Vmain getvPrincipal() {
        return vPrincipal;
    }

    public void setvPrincipal(Vmain vPrincipal) {
        this.vPrincipal = vPrincipal;
    }
    
    
    public JTree cargarArbol(){
        
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode("Plantas");
        
        for(Planta planta : plantas){
            nodoPadre.add(cargarNodoPlanta(planta));
        }
        
        DefaultTreeModel arbol = new DefaultTreeModel(nodoPadre);
        JTree tree = new JTree(arbol);
        return tree;
    }
    
    public DefaultMutableTreeNode cargarNodoPlanta(Planta planta){
        
        DefaultMutableTreeNode nodoPlanta = new DefaultMutableTreeNode(planta.getCodigo());
        
        for(Proceso proceso : planta.getProcesos()){
            DefaultMutableTreeNode nodoProceso = new DefaultMutableTreeNode(proceso.getCodigo());
            
            for(Tarea tarea : proceso.getTareas()){
                DefaultMutableTreeNode nodoTarea = new DefaultMutableTreeNode(tarea.getCodigo());
                
                for(Linea linea : tarea.getLineas()){
                    DefaultMutableTreeNode nodoLinea = new DefaultMutableTreeNode(linea.getCodigo());
                    nodoTarea.add(nodoLinea);
                }
                
                nodoProceso.add(nodoTarea);
            }
            
            nodoPlanta.add(nodoProceso);
        }
        
        return nodoPlanta;
    }
    
    public String obtenerElemento(String codigoDelElemento){
        //Obtencion de la instancia correspondiente a la linea a inspeccionar
        if(codigoDelElemento.indexOf("-") != 0){
            codigoDelElemento.substring(0, codigoDelElemento.indexOf("-"));
            
        }
        return codigoDelElemento.substring(0, codigoDelElemento.indexOf("-"));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int selRow = vPrincipal.getjTree1().getRowForLocation(e.getX(), e.getY());
        TreePath selPath = vPrincipal.getjTree1().getPathForLocation(e.getX(), e.getY());
        //System.out.println(selPath);
        String[] tipoElementos =new String[]{"Planta", "Proceso", "Tarea", "Linea"};
        if(selPath.getPathCount()>1){
            //System.out.println(selPath.getPathCount());
            vPrincipal.getjTextField1().setText(tipoElementos[selPath.getPathCount()-2]);
            vPrincipal.getjTextField2().setText(selPath.getLastPathComponent().toString());
            if(selPath.getPathCount()-2 == 3){
                nombreLinea = vPrincipal.getjTextField2().getText();
                vPrincipal.getBtnInspeccionar().setEnabled(true);
            }
            else{
                nombreLinea = "";
            vPrincipal.getBtnInspeccionar().setEnabled(false);
            }
        }
        //System.out.println(obtenerElemento(selPath.getLastPathComponent().toString()));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e);
        if(e.getActionCommand()=="Inspeccionar"){
            //vPrincipal.setEnabled(false);
            
            CLinea controladorLinea = new CLinea(nombreLinea, this, con);
            vPrincipal.enable(false);
            
        }
        
        if(e.getActionCommand()=="Salir"){
            //vPrincipal.setEnabled(false);
            
            vPrincipal.setVisible(false);
            System.exit(1);
            
        }
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
