package usuario.interfaces;

import usuario.modelo.Usuario;
import usuario.modelo.Rol;

/**
 * Interfaz IOperacionesAdmin - PRINCIPIO ISP
 * Interfaz específica solo para operaciones administrativas
 * Evita que clases no-admin implementen métodos que no necesitan
 */
public interface IOperacionesAdmin {

    /**
     * Crea un nuevo usuario en el sistema
     * Solo administradores pueden usar esta función
     */
    void crearUsuario(Usuario usuario);

    /**
     * Elimina un usuario del sistema
     * Solo administradores pueden usar esta función
     */
    void eliminarUsuario(String id);

    /**
     * Cambia el rol de un usuario existente
     * Solo administradores pueden usar esta función
     */
    void cambiarRolUsuario(String id, Rol nuevoRol);
}