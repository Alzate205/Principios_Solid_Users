package usuario.implementaciones;

import usuario.interfaces.IValidadorPassword;

/**
 * Validador de contraseñas seguras - PRINCIPIOS DIP y SRP
 */
public class ValidadorPasswordSeguro implements IValidadorPassword {

    @Override
    public boolean validar(String password) {
        // Validación múltiple usando operador AND (&&)
        return password != null &&                           // No debe ser null
                password.length() >= 8 &&                     // Mínimo 8 caracteres
                password.matches(".*[A-Z].*") &&              // Al menos una mayúscula
                password.matches(".*[0-9].*");                // Al menos un número

        // matches() usa expresiones regulares:
        // ".*[A-Z].*" = cualquier texto + mayúscula + cualquier texto
        // ".*[0-9].*" = cualquier texto + dígito + cualquier texto
    }
}