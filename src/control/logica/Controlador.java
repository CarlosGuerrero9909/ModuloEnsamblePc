package control.logica;

import control.dao.ClienteDAO;
import control.dao.EmpleadoDAO;
import control.dao.EnsambleDAO;
import control.dao.ItemEnsambleDao;
import control.dao.TipoDetalleDAO;
import models.ClienteVO;
import models.EmpleadoVO;
import models.Ensamble;
import models.ItemEnsamble;
import models.TipoDetalle;
import vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Controlador implements ActionListener{
    private EmpleadoDAO empDao;
    private ClienteDAO clienteDao;
    private Ventana vtn;

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
            Double valTotal = valFactura + valIva;
            vtn.renderTerminar();
            vtn.getPnlTerminar().getTxfTotalProSer().setText(valFactura.toString());
            vtn.getPnlTerminar().getTxfIva().setText(valIva.toString());
            vtn.getPnlTerminar().getTxfTotalFact().setText(valTotal.toString());
        }
        if(e.getActionCommand().equals("Aceptar")){
            System.out.println("Aceptar");   
        }
        if(e.getActionCommand().equals("VolverEnsamble")){
            vtn.renderEnsamble();
            vtn.getPnlTerminar().limpiarCampos();  
        }
        if(e.getActionCommand().equals("Salir")){
            System.exit(0);
        }
        
    }

    public boolean validarEmpleado(String codEmplea){
        empDao = new EmpleadoDAO();
        ArrayList<EmpleadoVO> empleadosAux = empDao.obtenerEmpleadosAux();

        for (EmpleadoVO empleAux : empleadosAux) {
            if(empleAux.getCodEmple().equals(codEmplea)){
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
    
}
