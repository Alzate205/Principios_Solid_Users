package usuario.implementaciones;

import usuario.interfaces.IAutenticacion;
import usuario.interfaces.IRepositorioUsuario;
import usuario.modelo.Usuario;

/**
 * Autenticación OAuth - PRINCIPIO OCP
 * Nueva implementación sin modificar código existente
 */
public class AutenticacionOAuth implements IAutenticacion {

    private IRepositorioUsuario repositorio;
    private Usuario usuarioActual;

    public AutenticacionOAuth(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public boolean autenticar(String email, String token) {
        // Busca usuario por email
        Usuario usuario = repositorio.buscarPorEmail(email);

        // Valida el token OAuth en lugar de contraseña
        if (usuario != null && validarToken(token)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    /**
     * Metodo privado para validar tokens OAuth
     */
    private boolean validarToken(String token) {
        // Simulación simple: token debe empezar con "oauth_"
        return token != null && token.startsWith("oauth_");
    }

    @Override
    public Usuario obtenerUsuarioActual() {
        return usuarioActual;
    }
}