package usuario.interfaces;

import usuario.modelo.Usuario;

/**
 * Interfaz IAutenticacion - PRINCIPIO DIP
 * Define el contrato para servicios de autenticación
 * Permite que las clases dependan de abstracciones, no de implementaciones
 */
public interface IAutenticacion {

    /**
     * Autentica un usuario con email y contraseña
     * @param email - Email del usuario
     * @param password - Contraseña del usuario
     * @return true si la autenticación es exitosa
     */
    boolean autenticar(String email, String password);

    /**
     * Obtiene el usuario actualmente autenticado
     * @return Usuario autenticado o null si no hay sesión activa
     */
    Usuario obtenerUsuarioActual();
}