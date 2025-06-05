package usuario.interfaces;

/**
 * Interfaz IValidadorPassword - PRINCIPIO DIP
 * Abstrae la lógica de validación de contraseñas
 */
public interface IValidadorPassword {

    /**
     * Valida si una contraseña cumple los requisitos de seguridad
     * @param password - Contraseña a validar
     * @return true si cumple los requisitos
     */
    boolean validar(String password);
}
