package control.logica;

import control.dao.EmpleadoDAO;
import vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener{
    private EmpleadoDAO empDao;
    private Ventana vtn;

    public Controlador(){
        this.vtn = new Ventana(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Registrar")){
            vtn.renderConsulta();
            vtn.getPnlReg().limpiarCampo();
            System.out.println("Aca va la logica de registro del empleado");
        }
        if(e.getActionCommand().equals("Consultar")){
            vtn.renderEnsamble();
            vtn.getPnlCon().limpiarCampo();
            System.out.println("Aca va la logica de consulta del cliente");    
        }
        if(e.getActionCommand().equals("TipoDetalleSelecc")){
            System.out.println("TipoDetalleSelecc");    
        }
        if(e.getActionCommand().equals("ConsultarEnsamble")){
            System.out.println("ConsultarEnsamble");   
        }
        if(e.getActionCommand().equals("VolverConsultaCli")){
            vtn.getPnlEnsamble().limpiarTabla();
            vtn.renderConsulta();
        }
        if(e.getActionCommand().equals("Terminar")){
            vtn.renderTerminar();
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

    public EmpleadoDAO getEmpDao() {
        return empDao;
    }

    public Ventana getVtn() {
        return vtn;
    }
    
}
