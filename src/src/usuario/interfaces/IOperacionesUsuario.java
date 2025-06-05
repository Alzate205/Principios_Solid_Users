package usuario.interfaces;

/**
 * Interfaz IOperacionesUsuario - PRINCIPIO ISP
 * Operaciones que cualquier usuario autenticado puede realizar
 */
public interface IOperacionesUsuario {

    /**
     * Permite al usuario cambiar su contraseña
     */
    void cambiarPassword(String nuevaPassword);

    /**
     * Permite al usuario actualizar su perfil
     */
    void actualizarPerfil(String nombre, String email);
}
