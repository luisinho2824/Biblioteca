package Biblioteca;

public class AlmacenVariables {
    private static AlmacenVariables instance;
    private String idUsuario;

    // Constructor privado para evitar instanciación directa
    private AlmacenVariables() {}

    // Método para obtener la instancia única de SessionManager
    public static AlmacenVariables getInstance() {
        if (instance == null) {
            instance = new AlmacenVariables();
        }
        return instance;
    }

    // Setter para almacenar el idUsuario
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getter para obtener el idUsuario
    public String getIdUsuario() {
        return idUsuario;
    }
}
