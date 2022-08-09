package control.logica;

import control.dao.ClienteDAO;
import control.dao.EmpleadoDAO;
import control.dao.EnsambleDAO;
import control.dao.FacturaDAO;
import control.dao.ItemEnsambleDao;
import control.dao.TipoDetalleDAO;
import models.ClienteVO;
import models.EmpleadoVO;
import models.Ensamble;
import models.Factura;
import models.ItemEnsamble;
import models.TipoDetalle;
import vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneId;


public class Controlador implements ActionListener{
    private EmpleadoDAO empDao;
    private ClienteDAO clienteDao;
    private Ventana vtn;
    private String idTipoPersona;
    private String idPersonaFk;
    private String codEmpleado;
    private Double valTotal;
    private int nuevoIdFactura;

    public Controlador(){
        this.vtn = new Ventana(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Registrar")){
            String codEmplea = vtn.getPnlReg().getTxfCodEmp().getText();
            if(validarEmpleado(codEmplea)){
                vtn.renderConsulta();
                vtn.getPnlReg().limpiarCampo();
            }else{
                vtn.mostrarJoptionPane("Empleado NO Registrado");
                vtn.getPnlReg().limpiarCampo();
            }
            
        }

        if(e.getActionCommand().equals("Consultar")){
            String cedCliente = vtn.getPnlCon().getTxfCedClie().getText();
            String nombreCliente = validarCliente(cedCliente);

            if(nombreCliente.equals("")){
                vtn.mostrarJoptionPane("El cliente no se encuentra registrado");
                vtn.getPnlCon().limpiarCampo();
            }else{
                vtn.mostrarJoptionPane("Cliente registrado: " + nombreCliente);
                vtn.getPnlCon().limpiarCampo();
                try {
                    TipoDetalleDAO tipoDetalleDao = new TipoDetalleDAO();
                    ArrayList<TipoDetalle> listaTipoDetalle = tipoDetalleDao.getTipoDetalle();
                    vtn.getPnlEnsamble().getCbxTipoDetalle().removeAllItems();
                    for (TipoDetalle tipoDetalle : listaTipoDetalle) {
                        vtn.getPnlEnsamble().getCbxTipoDetalle().addItem(tipoDetalle.getDescTipoDetalle());
                    }
                } catch (Exception ex) {
                    System.out.println("Error al cargar el combo");
                }
                vtn.renderEnsamble();
                
            }
        }

        if(e.getActionCommand().equals("TipoDetalleSelecc")){
            try {
                if(((String)vtn.getPnlEnsamble().getCbxTipoDetalle().getSelectedItem()).equals("Ensamble")){
                    vtn.getPnlEnsamble().getCbxEnsamNoFact().removeAllItems();
                    EnsambleDAO ensambleDao = new EnsambleDAO();
                    ArrayList<Ensamble> listaEnsamble = ensambleDao.getEnsambleNoFacturado();
                    for (Ensamble ensamble : listaEnsamble) {
                        vtn.getPnlEnsamble().getCbxEnsamNoFact().addItem(ensamble.getConsecc());
                    }
                }else{
                    System.out.println("No entro");
                }
            } catch (Exception exe) {
                System.out.println("Error al cargar el combo: "+exe.getMessage());
            }

        }
        if(e.getActionCommand().equals("ConsultarEnsamble")){
            try {
                int EnsambleSelec = ((Integer)vtn.getPnlEnsamble().getCbxEnsamNoFact().getSelectedItem());
                ItemEnsambleDao itemEnsambleDao = new ItemEnsambleDao();
                ArrayList<ItemEnsamble> listaEnsamble = itemEnsambleDao.getItemEnsamble(EnsambleSelec);
                vtn.getPnlEnsamble().limpiarTabla();
                vtn.getPnlEnsamble().setTabla(listaEnsamble);
            } catch (Exception ex) {
                System.out.println("Error al consultar los detalles del ensamble: "+ex.getMessage());
            }
        }
        if(e.getActionCommand().equals("VolverConsultaCli")){
            vtn.getPnlEnsamble().limpiarTabla();
            vtn.renderConsulta();
        }
        if(e.getActionCommand().equals("Terminar")){
            Double valFactura = calcularFactura();
            Double valIva = calcIVA(valFactura);
            valTotal = valFactura + valIva;
            vtn.renderTerminar();
            vtn.getPnlTerminar().getTxfTotalProSer().setText(valFactura.toString());
            vtn.getPnlTerminar().getTxfIva().setText(valIva.toString());
            vtn.getPnlTerminar().getTxfTotalFact().setText(valTotal.toString());
        }
        if(e.getActionCommand().equals("Aceptar")){
            GenerarFactura();

            System.out.println("Aceptar Final");   
        }
        if(e.getActionCommand().equals("VolverEnsamble")){
            vtn.renderEnsamble();
            vtn.getPnlTerminar().limpiarCampos();  
        }
        if(e.getActionCommand().equals("Salir")){
            System.exit(0);
        }
        
    }

    //se genera fecha actual
    public Date fechaActual(){

        java.util.Date fechaActual= new java.util.Date(System.currentTimeMillis());
        java.sql.Date fechaSql= java.sql.Date.valueOf(fechaActual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        return fechaSql;
    }

    public boolean validarEmpleado(String codEmplea){
        empDao = new EmpleadoDAO();
        ArrayList<EmpleadoVO> empleadosAux = empDao.obtenerEmpleadosAux();

        for (EmpleadoVO empleAux : empleadosAux) {
            if(empleAux.getCodEmple().equals(codEmplea)){
                codEmpleado = empleAux.getCodEmple();
                return true;
            }
        }
        return false;
    }

    public String validarCliente(String cedula){
        clienteDao = new ClienteDAO();
        ArrayList<ClienteVO> clientes = clienteDao.obtenerClientes();

        for (ClienteVO cliente : clientes) {
            if(cliente.getCedula().equals(cedula)){
                idTipoPersona = cliente.getIdTipoPersonaFk();
                idPersonaFk = cliente.getCodCliente();
                return cliente.getNombreApell();
            }
        }
        return "";
    }

    public Double calcularFactura(){
        Double total = 0.0;
        for (int i = 0; i < vtn.getPnlEnsamble().getTblDetallEnsam().getRowCount(); i++) {
            total += Double.parseDouble(vtn.getPnlEnsamble().getTblDetallEnsam().getValueAt(i, 4).toString());
        }
        return total;
    }

    public Double calcIVA(Double total){
        return (Double) (total*0.16);
    }

    public EmpleadoDAO getEmpDao() {
        return empDao;
    }

    public Ventana getVtn() {
        return vtn;
    }
    
    public void GenerarFactura(){
        FacturaDAO facturaDao = new FacturaDAO();
        int ultimaFactura = facturaDao.obtenerUltimaFactura();
        nuevoIdFactura = ultimaFactura + 1;
        //se crea nueva factura
        Factura factura = new Factura();
        factura.setnFactura(nuevoIdFactura);
        factura.setIdFormaPagoFk("4");  
        factura.setIdTipoPersonaFk(idTipoPersona);
        factura.setIdPersonaFk(idPersonaFk);
        factura.setIdTipoFacturaFk("1");
        factura.setCodEmpleadoFk(codEmpleado);
        factura.setFechaFactura(fechaActual());
        factura.setValorFactura(valTotal);

        facturaDao.insertarFactura(factura);
    }

    public void InsertarDetallesFactura(){
        
    }
}
