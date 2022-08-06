package control.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.conexion.ConnectDB;
import models.EmpleadoVO;

public class EmpleadoDAO {
    private Connection con;
	private Statement st; //Para realizar consultas y enviar de la BD
	private ResultSet rs; //Para traer de la BD a partir de una consulta

    public void EmpleadoDAO(){
        con = null;
        st = null;
        rs = null;
    }

	public ArrayList<EmpleadoVO> obtenerEmpleadosAux(){
		ArrayList<EmpleadoVO> empleadosAux = new ArrayList<EmpleadoVO>();
		String consulta = "select e.codempleado cod_empleado, e.nomempleado||' '||e.apellempleado empleado, tc.desctipocargo cargo\n"+
			"from empleado e, tipocargo tc, historicargo hc\n"+
			"where tc.idtipocargo = hc.idtipocargofk and\n"+
				"e.codempleado = hc.codempleadofk and\n"+
				"lower(tc.desctipocargo) like 'aux%';";
		try {
			con = ConnectDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(consulta); // las consultas van con Query
			while (rs.next()) {
				EmpleadoVO empAux = new EmpleadoVO();
				empAux.setCodEmple(rs.getString("cod_empleado"));
				empAux.setNombreApell(rs.getString("empleado"));
				empAux.setCargo(rs.getString("cargo"));
				empleadosAux.add(empAux);
			}
			st.close();
			ConnectDB.dissconect();
		} catch (SQLException ex) {
			System.out.println("No se pudo realizar la consulta");
			ex.printStackTrace();
		}
		return empleadosAux;
	}
	

    // public boolean insertEmpleado(EmpleadoVO empleado){
    //     String sentenceSQL = "INSERT INTO empleado (idempleado,idtienda,nombre,cedula,telefono,direccion,correo,salario) " + 
	// 						"VALUES ('"+empleado.getIdEmpleado()+"','"+empleado.getIdTienda()+"','"+empleado.getNombre()+
	// 						"','"+empleado.getCedula()+"','"+empleado.getTelefono()+"','"+empleado.getDireccion()+
	// 						"','"+empleado.getCorreo()+"',"+empleado.getSalario()+")";
    //     try {
    //         con = ConnectDB.getConnection();
    //         st = con.createStatement();
    //         st.executeUpdate(sentenceSQL);
    //         st.close();
    //         ConnectDB.dissconect();
    //         return true;
    //     } catch (SQLException e) {
    //         System.out.println("No se pudo realizar la insercion");
    //     }
    //     return false;
    // }

    // public boolean deleteEmpleado(String idEmpleado) {
	// 	String sentenceSQL = "DELETE FROM empleado WHERE idEmpleado='"+idEmpleado+"'";
	// 	try {
	// 		con = ConnectDB.getConnection();
	// 		st = con.createStatement();
	// 		st.executeUpdate(sentenceSQL); // Eliminar , agregar o modificar va con update
	// 		st.close();
	// 		ConnectDB.dissconect();
	// 		return true;
	// 	} catch (SQLException ex) {
	// 		System.out.println("No se pudo realizar la consulta");
	// 	}
	// 	return false;
	// }

    // public boolean modifyEmpleado(double newSalario, String idEmpleado) {
	// 	String sentenceSQL = "UPDATE empleado SET salario="+newSalario+" WHERE idempleado='"+idEmpleado+"'";
	// 	try {
	// 		con = ConnectDB.getConnection();
	// 		st = con.createStatement();
	// 		st.executeUpdate(sentenceSQL);
	// 		st.close();
	// 		ConnectDB.dissconect();
	// 		return true;
	// 	} catch (SQLException ex) {
	// 		System.out.println("No se pudo realizar la modifcacion");
	// 	}
	// 	return false;
	// }


}
