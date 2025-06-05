package usuario.modelo.roles;

import usuario.modelo.Rol;

/**
 * Clase Invitado - Acceso más restrictivo
 */
public class Invitado extends Rol {

    public Invitado() {
        super("Invitado", 1);    // Nivel más bajo de acceso
    }

    @Override
    public boolean puedeAcceder(String recurso) {
        // Invitado solo puede ver contenido público y hacer login
        return recurso.equals("public_content") ||    // Puede ver contenido público
                recurso.equals("login");               // Puede acceder al login
    }
}