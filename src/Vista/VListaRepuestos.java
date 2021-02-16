/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.Repuesto;
import modelo.StockRepuesto;
import presentador.PListaRepuestos;
import presentador.interfaces.IListaRepuestos;

/**
 *
 * @author usuario
 */
public class VListaRepuestos extends javax.swing.JDialog implements IListaRepuestos {

    /**
     * Creates new form VListaRepuestos
     */
    int cantidadRegistrosAFiltrar = 0;
    PListaRepuestos presentador;

    public VListaRepuestos() {
        initComponents();
    }

    public VListaRepuestos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.presentador = new PListaRepuestos(this);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public VListaRepuestos(java.awt.Frame parent, boolean modal, int idLote) {
        super(parent, modal);
        initComponents();
        this.presentador = new PListaRepuestos(this, idLote);
        this.btnEliminar.setVisible(false);
        this.btnRegistrar.setVisible(false);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void cargarTablaRepuestos(ArrayList<Repuesto> repuestos) {
        DefaultTableModel modelo;
        String cabecera[] = {"CÓDIGO", "DESCRIPCIÓN", "FAMILIA", "CATEGORÍA", "UNIDAD", "COD FABRIC", "NACIONAL", "PRECIO", "EAN"};

        String matriz[][] = new String[this.cantidadRegistrosAFiltrar][9];
        int i = 0;
        for (Repuesto r : repuestos) {
            if (r.isSeFiltra()) {
                matriz[i][0] = r.getCodigo();
                matriz[i][1] = r.getDescripcion();
                matriz[i][2] = r.getClaseFamilia();
                matriz[i][3] = r.getCategoria().getDescripcion();
                matriz[i][4] = r.getUnidad().name();
                matriz[i][5] = String.valueOf(r.getCodFabricante());
                if (r.isNacional()) {
                    matriz[i][6] = "Nacional";
                } else {
                    matriz[i][6] = "Importado";
                }
                matriz[i][7] = String.valueOf(r.getPrecio());
                matriz[i][8] = r.getDimension().getEAN();
                i++;
            }

        }
        modelo = new DefaultTableModel(matriz, cabecera) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tbLista.setModel(modelo);
        this.tbLista.setRowSorter(new TableRowSorter(modelo));
        this.tbLista.requestFocus();
        if (repuestos.isEmpty()) {
            this.tbLista.changeSelection(-1, 0, false, false);
        } else {
            this.tbLista.changeSelection(0, 0, false, false);
        }
        //definicion de columnas de tabla y centrado de valores
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        for (int j = 0; j < this.tbLista.getColumnModel().getColumnCount(); j++) {
            cr.setHorizontalAlignment(SwingConstants.CENTER);
            this.tbLista.getColumnModel().getColumn(j).setCellRenderer(cr);
            this.tbLista.getColumnModel().getColumn(j).setPreferredWidth(146);
        }
    }

    @Override
    public void cargarTablaStockRepuestos(ArrayList<StockRepuesto> stock) {
        DefaultTableModel modelo;
        String cabecera[] = {"CÓDIGO", "DESCRIPCIÓN", "FAMILIA", "CATEGORÍA","CANTIDAD","UNIDAD"};

        String matriz[][] = new String[this.cantidadRegistrosAFiltrar][6];
        int i=0;
        for (StockRepuesto s  : stock) {
            if(s.getRepuesto().isSeFiltra()){
                matriz[i][0] = s.getRepuesto().getCodigo();
                matriz[i][1] = s.getRepuesto().getDescripcion();
                matriz[i][2] = s.getRepuesto().getClaseFamilia();
                matriz[i][3] = s.getRepuesto().getCategoria().getDescripcion();
                matriz[i][4] = String.valueOf(s.getCantidad());
                matriz[i][5] = s.getUnidadAlmacenamiento().name();
                i++;
            }
            
        }

        modelo = new DefaultTableModel(matriz, cabecera) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tbLista.setModel(modelo);
        this.tbLista.setRowSorter(new TableRowSorter(modelo));
        this.tbLista.requestFocus();
        if (stock.isEmpty()) {
            this.tbLista.changeSelection(-1, 0, false, false);
        } else {
            this.tbLista.changeSelection(0, 0, false, false);
        }
        //definicion de columnas de tabla y centrado de valores
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        for (int j = 0; j < this.tbLista.getColumnModel().getColumnCount(); j++) {
            cr.setHorizontalAlignment(SwingConstants.CENTER);
            this.tbLista.getColumnModel().getColumn(j).setCellRenderer(cr);
            this.tbLista.getColumnModel().getColumn(j).setPreferredWidth(146);
        }
    }
    @Override
    public void cargarCombos(ArrayList<String> categorias, ArrayList<String> familias) {
        //Combo Box de Categorias
        this.cbxCategoria.removeAllItems();
        this.cbxCategoria.addItem("TODOS");
        for (String i : categorias) {
            this.cbxCategoria.addItem(i);
        }

        //Combo Box de Familias
        this.cbxFamilia.removeAllItems();
        this.cbxFamilia.addItem("TODOS");
        for (String i : familias) {
            this.cbxFamilia.addItem(i);
        }
    }

    public void filtrarRepuestos() {
        String cod = this.txtCodigo.getText().toUpperCase();
        String desc = this.txtDescripcion.getText().toUpperCase();
        String cat = this.cbxCategoria.getSelectedItem().toString();
        String fam = this.cbxFamilia.getSelectedItem().toString();
        this.presentador.filtrarRepuestos(cod, desc, cat, fam);

    }

    public void limpiarFiltroBusqueda() {
        this.txtCodigo.setText("");
        this.txtDescripcion.setText("");
        this.cbxCategoria.setSelectedIndex(0);
        this.cbxFamilia.setSelectedIndex(0);
        this.presentador.filtrarTodos();
    }

    @Override
    public void borrarFiltro() {
        JOptionPane.showMessageDialog(this, "No se encontraron coincidencias", "Error", JOptionPane.ERROR_MESSAGE);
        limpiarFiltroBusqueda();
    }

    public int getCantidadRegistrosAFiltrar() {
        return cantidadRegistrosAFiltrar;
    }

    @Override
    public void setCantidadRegistrosAFiltrar(int cantidadRegistrosAFiltrar) {
        this.cantidadRegistrosAFiltrar = cantidadRegistrosAFiltrar;
    }

    public boolean esNumerico(String texto) {
        try {
            if (texto != null) {
                Double.parseDouble(texto);
                return true;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxCategoria = new javax.swing.JComboBox<String>();
        cbxFamilia = new javax.swing.JComboBox<String>();
        txtDescripcion = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbLista = new javax.swing.JTable();
        btnSalir = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lista de Repuestos");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Buscar por");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Categoría");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Familia");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Descripción");

        cbxCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxFamilia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Lista de Repuestos");

        tbLista = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbLista);

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Código");

        btnEliminar.setText("Quitar repuesto");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnRegistrar.setText("Seleccionar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(43, 43, 43)
                                .addComponent(cbxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(58, 58, 58)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(106, 106, 106)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(340, 340, 340)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(205, 205, 205))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cbxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnLimpiar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalir)
                    .addComponent(btnEliminar)
                    .addComponent(btnRegistrar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        filtrarRepuestos();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFiltroBusqueda();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        VRegistrarRepuesto vista = new VRegistrarRepuesto(null, true);
        if (vista.presentador.getR() != null) {
            this.presentador.agregarRepuesto(vista.presentador.getR());
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (this.tbLista.getSelectedRowCount() == 1) {
            if (JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el Repuesto?",
                "Eliminar Repuesto", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String cod = this.tbLista.getValueAt(this.tbLista.getSelectedRow(), 0).toString();
            String EAN = this.tbLista.getValueAt(this.tbLista.getSelectedRow(), 8).toString();
            boolean estaEnUso = this.presentador.repuestoEnUso(cod);
            if (estaEnUso) {
                JOptionPane.showMessageDialog(this, "No puede eliminar un repuesto que fue utilizado para algún movimiento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean sinStock = this.presentador.repuestoSinStock(cod);
            if (sinStock) {
                this.presentador.eliminarRepuesto(cod, EAN);
            } else {
                JOptionPane.showMessageDialog(this, "No puede eliminar un repuesto que tiene almacenado stock.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxCategoria;
    private javax.swing.JComboBox<String> cbxFamilia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tbLista;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables

    public JTable getTbLista() {
        return tbLista;
    }

    public void setTbLista(JTable tbLista) {
        this.tbLista = tbLista;
    }
}
