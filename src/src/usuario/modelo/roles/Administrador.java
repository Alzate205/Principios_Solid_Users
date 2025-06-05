package usuario.modelo.roles;

import usuario.modelo.Rol;

/**
 * Clase Administrador - PRINCIPIOS OCP y LSP
 * OCP: Extiende funcionalidad sin modificar código existente
 * LSP: Puede sustituir a Rol sin romper el comportamiento
 */
public class Administrador extends Rol {

    /**
     * Constructor del Administrador
     * Llama al constructor padre con parámetros específicos
     */
    public Administrador() {
        // super() llama al constructor de la clase padre (Rol)
        super("Administrador", 3);    // Nombre: "Administrador", Nivel: 3 (máximo)
    }

    /**
     * Implementación específica del metodo abstracto - PRINCIPIO LSP
     * El administrador puede acceder all (comportamiento consistente)
     * @param recurso - Cualquier recurso del sistema
     * @return siempre true (acceso total)
     */
    @Override
    public boolean puedeAcceder(String recurso) {
        // Administrador tiene acceso completo a todos los recursos
        return true;    // Retorna true sin importar el recurso solicitado
    }
}