package Biblioteca;

public class AlmacenVariables {
    private static AlmacenVariables instance;
    private String idUsuario;
    private String nombre;
    private String email;
    private String telefono; // Agregado teléfono
    private String direccion; // Agregada dirección
    private int rol;
    
    

    // Constructor privado para evitar instanciación directa
    private AlmacenVariables() {}

    // Método para obtener la instancia única de AlmacenVariables
    public static AlmacenVariables getInstance() {
        if (instance == null) {
            instance = new AlmacenVariables();
        }
        return instance;
    }

    // Setters y Getters
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getRol() {
        return rol;
    }

    // Método para verificar si el usuario es administrador
    public boolean esAdmin() {
        return rol == 1;
    }
}
