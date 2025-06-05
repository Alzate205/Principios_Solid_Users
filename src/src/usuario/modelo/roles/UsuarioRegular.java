package usuario.modelo.roles;

import usuario.modelo.Rol;

/**
 * Clase UsuarioRegular - PRINCIPIOS OCP y LSP
 * Implementa permisos específicos para usuarios normales
 */
public class UsuarioRegular extends Rol {

    /**
     * Constructor del Usuario Regular
     */
    public UsuarioRegular() {
        super("Usuario", 2);    // Nombre: "Usuario", Nivel: 2 (medio)
    }

    /**
     * Lógica de permisos para usuario regular
     * @param recurso - Recurso solicitado
     * @return true si puede acceder, false si está restringido
     */
    @Override
    public boolean puedeAcceder(String recurso) {
        // Usuario regular NO puede acceder a funciones administrativas
        // equals() compara el contenido de las cadenas
        return !recurso.equals("admin_panel") &&      // No puede acceder al panel admin
                !recurso.equals("user_management");    // No puede gestionar usuarios
    }
}