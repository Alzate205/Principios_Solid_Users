package usuario.modelo.roles;

import usuario.modelo.Rol;

/**
 * Clase Moderador - PRINCIPIO OCP
 * Nuevo rol agregado sin modificar código existente
 */
public class Moderador extends Rol {

    public Moderador() {
        super("Moderador", 2);    // Mismo nivel que usuario regular
    }

    @Override
    public boolean puedeAcceder(String recurso) {
        // Moderador puede gestionar contenido pero no usuarios ni configuración
        return !recurso.equals("user_management") &&     // No puede gestionar usuarios
                !recurso.equals("system_config");         // No puede cambiar configuración
    }
}