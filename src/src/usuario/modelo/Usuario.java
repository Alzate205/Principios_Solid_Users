package usuario.modelo;

/**
 * Clase Usuario - PRINCIPIO SRP
 * Responsabilidad única: Almacenar y gestionar datos del usuario
 */
public class Usuario {
    // Atributos privados para encapsulación
    private String id;          // Identificador único del usuario
    private String nombre;      // Nombre completo del usuario
    private String email;       // Email para autenticación
    private String password;    // Contraseña del usuario
    private Rol rol;           // Rol asignado al usuario

    /**
     * Constructor para crear un nuevo usuario
     * @param id - Identificador único
     * @param nombre - Nombre del usuario
     * @param email - Email del usuario
     * @param password - Contraseña
     * @param rol - Rol asignado
     */
    public Usuario(String id, String nombre, String email, String password, Rol rol) {
        this.id = id;              // Asigna el ID del usuario
        this.nombre = nombre;      // Asigna el nombre del usuario
        this.email = email;        // Asigna el email del usuario
        this.password = password;  // Asigna la contraseña del usuario
        this.rol = rol;           // Asigna el rol del usuario
    }

    // Métodos getter - Solo para acceder a los datos (SRP)
    public String getId() {
        return id;                 // Retorna el ID del usuario
    }

    public String getNombre() {
        return nombre;             // Retorna el nombre del usuario
    }

    public String getEmail() {
        return email;              // Retorna el email del usuario
    }

    public String getPassword() {
        return password;           // Retorna la contraseña del usuario
    }

    public Rol getRol() {
        return rol;                // Retorna el rol del usuario
    }

    // Método setter solo para rol (permite cambio de permisos)
    public void setRol(Rol rol) {
        this.rol = rol;           // Actualiza el rol del usuario
    }
}