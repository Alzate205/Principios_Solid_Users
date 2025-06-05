package usuario.modelo;

/**
 * Clase abstracta Rol - PRINCIPIOS SRP, OCP, LSP
 * SRP: Responsabilidad única de manejar permisos
 * OCP: Abierta para extensión (nuevos roles), cerrada para modificación
 * LSP: Las subclases deben ser intercambiables
 */
public abstract class Rol {
    // Atributos protegidos para que las subclases puedan acceder
    protected String nombre;        // Nombre del rol (ej: "Administrador")
    protected int nivelAcceso;      // Nivel numérico de acceso (1=bajo, 3=alto)

    /**
     * Constructor protegido para roles
     * @param nombre - Nombre descriptivo del rol
     * @param nivelAcceso - Nivel numérico de permisos
     */
    protected Rol(String nombre, int nivelAcceso) {
        this.nombre = nombre;           // Establece el nombre del rol
        this.nivelAcceso = nivelAcceso; // Establece el nivel de acceso
    }

    // Métodos getter públicos
    public String getNombre() {
        return nombre;                  // Retorna el nombre del rol
    }

    public int getNivelAcceso() {
        return nivelAcceso;             // Retorna el nivel de acceso
    }

    /**
     * Método abstracto - PRINCIPIO LSP
     * Cada subclase debe implementar su propia lógica de permisos
     * pero manteniendo el mismo contrato
     * @param recurso - Recurso al que se quiere acceder
     * @return true si tiene permiso, false si no
     */
    public abstract boolean puedeAcceder(String recurso);
}