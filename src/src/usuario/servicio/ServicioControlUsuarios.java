package usuario.servicio;

import usuario.interfaces.*;
import usuario.modelo.Usuario;
import usuario.modelo.Rol;
import usuario.modelo.roles.Administrador;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servicio principal - PRINCIPIOS DIP, ISP, SRP
 * DIP: Depende de interfaces, no de implementaciones
 * ISP: Implementa múltiples interfaces específicas
 * SRP: Coordina operaciones de usuarios
 */
public class ServicioControlUsuarios implements IOperacionesAdmin, IOperacionesUsuario, IOperacionesLectura {

    // Logger para registrar eventos del sistema (recomendación SonarLint)
    private static final Logger logger = Logger.getLogger(ServicioControlUsuarios.class.getName());

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

        // Log de inicialización del servicio
        logger.info("ServicioControlUsuarios inicializado correctamente");
    }

    // === IMPLEMENTACIÓN DE IOperacionesAdmin ===

    @Override
    public void crearUsuario(Usuario usuario) {
        // Obtiene el usuario actualmente autenticado
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log del intento de creación de usuario
        logger.info(String.format("Intento de creación de usuario: %s por usuario: %s",
                usuario.getEmail(),
                actual != null ? actual.getNombre() : "No autenticado"));

        // Verifica que hay un usuario logueado Y que es administrador
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            // Log de seguridad - intento no autorizado
            logger.warning(String.format("Intento no autorizado de crear usuario %s por %s",
                    usuario.getEmail(),
                    actual != null ? actual.getNombre() : "usuario no autenticado"));
            // Lanza excepción de seguridad si no tiene permisos
            throw new SecurityException("Solo administradores pueden crear usuarios");
        }

        // Verifica que el email no esté ya registrado
        if (repositorio.existeEmail(usuario.getEmail())) {
            logger.warning(String.format("Intento de crear usuario con email duplicado: %s", usuario.getEmail()));
            throw new IllegalArgumentException("Email ya existe");
        }

        // Valida que la contraseña cumpla los requisitos
        if (!validador.validar(usuario.getPassword())) {
            logger.warning(String.format("Intento de crear usuario %s con contraseña inválida", usuario.getEmail()));
            throw new IllegalArgumentException("Password no cumple los requisitos");
        }

        // Si all está correcto, guarda el usuario
        repositorio.guardar(usuario);

        // Log exitoso de creación
        logger.info(String.format("Usuario %s creado exitosamente por administrador %s",
                usuario.getEmail(), actual.getNombre()));
    }

    @Override
    public void eliminarUsuario(String id) {
        // Obtiene el usuario actual para logging
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log del intento de eliminación
        logger.info(String.format("Intento de eliminación de usuario ID: %s por usuario: %s",
                id,
                actual != null ? actual.getNombre() : "No autenticado"));

        // Verifica permisos de administrador
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            logger.warning(String.format("Intento no autorizado de eliminar usuario %s por %s",
                    id,
                    actual != null ? actual.getNombre() : "usuario no autenticado"));
            throw new SecurityException("Solo administradores pueden eliminar usuarios");
        }

        // Aquí iría la lógica de eliminación real
        // Por ahora solo registramos la operación
        logger.info(String.format("Usuario con ID %s eliminado exitosamente por administrador %s",
                id, actual.getNombre()));
    }

    @Override
    public void cambiarRolUsuario(String id, Rol nuevoRol) {
        // Obtiene el usuario actual para logging
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log del intento de cambio de rol
        logger.info(String.format("Intento de cambio de rol para usuario ID: %s a rol: %s por usuario: %s",
                id,
                nuevoRol.getNombre(),
                actual != null ? actual.getNombre() : "No autenticado"));

        // Verifica permisos de administrador
        if (actual == null || !(actual.getRol() instanceof Administrador)) {
            logger.warning(String.format("Intento no autorizado de cambiar rol del usuario %s por %s",
                    id,
                    actual != null ? actual.getNombre() : "usuario no autenticado"));
            throw new SecurityException("Solo administradores pueden cambiar roles");
        }

        // Aquí iría la lógica de cambio de rol real
        logger.info(String.format("Rol cambiado exitosamente para usuario ID %s a %s por administrador %s",
                id, nuevoRol.getNombre(), actual.getNombre()));
    }

    // === IMPLEMENTACIÓN DE IOperacionesUsuario ===

    @Override
    public void cambiarPassword(String nuevaPassword) {
        // Obtiene el usuario actual
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log del intento de cambio de contraseña (sin incluir la contraseña por seguridad)
        logger.info(String.format("Intento de cambio de contraseña por usuario: %s",
                actual != null ? actual.getNombre() : "No autenticado"));

        // Verifica que hay un usuario autenticado
        if (actual == null) {
            logger.warning("Intento de cambio de contraseña sin usuario autenticado");
            throw new SecurityException("Usuario no autenticado");
        }

        // Valida la nueva contraseña
        if (!validador.validar(nuevaPassword)) {
            logger.warning(String.format("Usuario %s intentó cambiar a una contraseña inválida",
                    actual.getNombre()));
            throw new IllegalArgumentException("Password no cumple los requisitos");
        }

        // Simula actualización de contraseña
        logger.info(String.format("Contraseña actualizada exitosamente para usuario: %s",
                actual.getNombre()));
    }

    @Override
    public void actualizarPerfil(String nombre, String email) {
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log del intento de actualización de perfil
        logger.info(String.format("Intento de actualización de perfil por usuario: %s",
                actual != null ? actual.getNombre() : "No autenticado"));

        if (actual == null) {
            logger.warning("Intento de actualización de perfil sin usuario autenticado");
            throw new SecurityException("Usuario no autenticado");
        }

        // Aquí iría la lógica de actualización real
        logger.info(String.format("Perfil actualizado exitosamente para usuario: %s (nuevo nombre: %s, nuevo email: %s)",
                actual.getNombre(), nombre, email));
    }

    // === IMPLEMENTACIÓN DE IOperacionesLectura ===

    @Override
    public Usuario verPerfil() {
        Usuario actual = autenticacion.obtenerUsuarioActual();

        // Log de consulta de perfil (nivel DEBUG para no saturar logs)
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Consulta de perfil por usuario: %s",
                    actual != null ? actual.getNombre() : "No autenticado"));
        }

        // Retorna el usuario actual directamente
        return actual;
    }

    @Override
    public boolean puedeAcceder(String recurso) {
        // Obtiene el usuario actual
        Usuario actual = autenticacion.obtenerUsuarioActual();
        boolean puedeAcceder = actual != null && actual.getRol().puedeAcceder(recurso);

        // Log de verificación de permisos (nivel DEBUG)
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Verificación de acceso al recurso '%s' por usuario %s: %s",
                    recurso,
                    actual != null ? actual.getNombre() : "No autenticado",
                    puedeAcceder ? "PERMITIDO" : "DENEGADO"));
        }

        // Verifica que existe Y que su rol permite el acceso
        return puedeAcceder;
    }
}