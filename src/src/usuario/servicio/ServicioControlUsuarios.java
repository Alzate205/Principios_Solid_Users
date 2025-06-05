package usuario.servicio;

import usuario.interfaces.*;
import usuario.modelo.Usuario;
import usuario.modelo.Rol;
import usuario.modelo.roles.Administrador;

/**
 * Servicio principal - PRINCIPIOS DIP, ISP, SRP
 * DIP: Depende de interfaces, no de implementaciones
 * ISP: Implementa múltiples interfaces específicas
 * SRP: Coordina operaciones de usuarios
 */
public class ServicioControlUsuarios implements IOperacionesAdmin, IOperacionesUsuario, IOperacionesLectura {

    // Dependencias inyectadas (PRINCIPIO DIP)
    private IAutenticacion autenticacion;        // Para manejar autenticación
    private IRepositorioUsuario repositorio;     // Para persistir datos
    private IValidadorPassword validador;        // Para validar contraseñas

    /**
     * Constructor con inyección de dependencias
     */
    public ServicioControlUsuarios(IAutenticacion autenticacion,
                                   IRepositorioUsuario repositorio,
                                   IValidadorPassword validador) {
        this.autenticacion = autenticacion;    // Asigna servicio de autenticación
        this.repositorio = repositorio;        // Asigna repositorio de datos
        this.validador = validador;           // Asigna validador de contraseñas
    }

    // === IMPLEMENTACIÓN DE IOperacionesAdmin ===

    @Override
    public void crearUsuario(Usuario usuario) {
        // Obtiene el usuario actualmente autenticado
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Verifica que hay un usuario logueado Y que es administrador
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            // Lanza excepción de seguridad si no tiene permisos
            throw new SecurityException("Solo administradores pueden crear usuarios");
        }

        // Verifica que el email no esté ya registrado
        if (repositorio.existeEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email ya existe");
        }

        // Valida que la contraseña cumpla los requisitos
        if (!validador.validar(usuario.getPassword())) {
            throw new IllegalArgumentException("Password no cumple los requisitos");
        }

        // Si todo está correcto, guarda el usuario
        repositorio.guardar(usuario);
    }

    @Override
    public void eliminarUsuario(String id) {
        // Verifica permisos de administrador
        Usuario actual = autenticacion.obtenerUsuarioActual();
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            throw new SecurityException("Solo administradores pueden eliminar usuarios");
        }
        // Aquí iría la lógica de eliminación
        System.out.println("Usuario " + id + " eliminado por " + actual.getNombre());
    }

    @Override
    public void cambiarRolUsuario(String id, Rol nuevoRol) {
        // Verifica permisos de administrador
        Usuario actual = autenticacion.obtenerUsuarioActual();
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            throw new SecurityException("Solo administradores pueden cambiar roles");
        }
        // Aquí iría la lógica de cambio de rol
        System.out.println("Rol cambiado para usuario " + id + " por " + actual.getNombre());
    }

    // === IMPLEMENTACIÓN DE IOperacionesUsuario ===

    @Override
    public void cambiarPassword(String nuevaPassword) {
        // Obtiene el usuario actual
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Verifica que hay un usuario autenticado
        if (actual == null) {
            throw new SecurityException("Usuario no autenticado");
        }

        // Valida la nueva contraseña
        if (!validador.validar(nuevaPassword)) {
            throw new IllegalArgumentException("Password no cumple los requisitos");
        }

        // Simula actualización de contraseña
        System.out.println("Password actualizada para: " + actual.getNombre());
    }

    @Override
    public void actualizarPerfil(String nombre, String email) {
        Usuario actual = autenticacion.obtenerUsuarioActual();
        if (actual == null) {
            throw new SecurityException("Usuario no autenticado");
        }
        // Aquí iría la lógica de actualización
        System.out.println("Perfil actualizado para: " + actual.getNombre());
    }

    // === IMPLEMENTACIÓN DE IOperacionesLectura ===

    @Override
    public Usuario verPerfil() {
        // Retorna el usuario actual directamente
        return autenticacion.obtenerUsuarioActual();
    }

    @Override
    public boolean puedeAcceder(String recurso) {
        // Obtiene el usuario actual
        Usuario actual = autenticacion.obtenerUsuarioActual();
        // Verifica que existe Y que su rol permite el acceso
        return actual != null && actual.getRol().puedeAcceder(recurso);
    }
}