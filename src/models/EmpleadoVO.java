package models;

public class EmpleadoVO {
    private String idEmpleado;
    private String idtienda;
    private String nombre;
    private String cedula;
    private String telefono;
    private String direccion;
    private String correo;
    private double salario;
    

    public EmpleadoVO(String idEmpleado, String idtienda, String nombre, String cedula, String telefono, String direccion, String correo,
            double salario) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.salario = salario;
        this.idtienda = idtienda;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public double getSalario() {
        return salario;
    }

    public String getIdTienda() {
        return idtienda;
    }

}
