package usuario.implementaciones;

import usuario.interfaces.IAutenticacion;
import usuario.interfaces.IRepositorioUsuario;
import usuario.modelo.Usuario;

/**
 * Autenticación básica con email y contraseña - PRINCIPIO DIP
 */
public class AutenticacionBasica implements IAutenticacion {

    // Dependencia inyectada (DIP)
    private IRepositorioUsuario repositorio;
    // Usuario actualmente logueado
    private Usuario usuarioActual;

    /**
     * Constructor que recibe dependencias (Inyección de Dependencias)
     */
    public AutenticacionBasica(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;  // Asigna la dependencia inyectada
    }

    @Override
    public boolean autenticar(String email, String password) {
        // Busca el usuario por email usando el repositorio
        Usuario usuario = repositorio.buscarPorEmail(email);

        // Verifica que el usuario existe Y que la contraseña coincide
        if (usuario != null && usuario.getPassword().equals(password)) {
            this.usuarioActual = usuario;    // Guarda el usuario autenticado
            return true;                     // Autenticación exitosa
        }
        return false;                        // Autenticación fallida
    }

    @Override
    public Usuario obtenerUsuarioActual() {
        return usuarioActual;    // Retorna el usuario logueado o null
    }
}