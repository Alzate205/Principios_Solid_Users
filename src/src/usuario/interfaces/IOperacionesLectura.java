package usuario.interfaces;

import usuario.modelo.Usuario;

/**
 * Interfaz IOperacionesLectura - PRINCIPIO ISP
 * Operaciones de solo lectura, disponibles para todos
 */
public interface IOperacionesLectura {

    /**
     * Permite ver el perfil del usuario actual
     */
    Usuario verPerfil();

    /**
     * Verifica si el usuario puede acceder a un recurso
     */
    boolean puedeAcceder(String recurso);
}