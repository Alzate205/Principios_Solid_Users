package usuario.interfaces;

import usuario.modelo.Usuario;

/**
 * Interfaz IRepositorioUsuario - PRINCIPIO DIP
 * Abstrae las operaciones de persistencia de usuarios
 */
public interface IRepositorioUsuario {

    /**
     * Busca un usuario por su email
     * @param email - Email a buscar
     * @return Usuario encontrado o null si no existe
     */
    Usuario buscarPorEmail(String email);

    /**
     * Guarda un usuario en el sistema de persistencia
     * @param usuario - Usuario a guardar
     */
    void guardar(Usuario usuario);

    /**
     * Verifica si existe un email en el sistema
     * @param email - Email a verificar
     * @return true si el email ya está registrado
     */
    boolean existeEmail(String email);
}