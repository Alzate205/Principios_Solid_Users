package usuario.principal;

import usuario.interfaces.*;
import usuario.implementaciones.*;
import usuario.modelo.Usuario;
import usuario.modelo.roles.*;
import usuario.servicio.ServicioControlUsuarios;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Clase principal para demostrar el sistema
 * Configurada con logger para cumplir con las mejores prácticas de SonarLint
 */
public class Main {

    // Logger para la clase Main (recomendación SonarLint)
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // Configuración inicial del logger
    static {
        configurarLogger();
    }

    /**
     * Configura el logger para mostrar mensajes en consola con formato legible
     */
    private static void configurarLogger() {
        // Remover handlers por defecto para evitar duplicados
        Logger rootLogger = Logger.getLogger("");
        rootLogger.getHandlers()[0].setLevel(Level.OFF);

        // Crear handler personalizado para consola
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                // Formato personalizado para mostrar solo el mensaje (sin timestamp para demo)
                return record.getMessage() + System.lineSeparator();
            }
        });

        // Agregar handler al logger
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    public static void main(String[] args) {
        logger.info("=== SISTEMA DE CONTROL DE USUARIOS - PRINCIPIOS SOLID ===");
        logger.info(""); // Línea en blanco

        // === CONFIGURACIÓN DEL SISTEMA (PRINCIPIO DIP) ===
        logger.info("Iniciando configuración del sistema...");

        // Se crean las implementaciones concretas
        IRepositorioUsuario repositorio = new RepositorioUsuarioMemoria();  // Persistencia en memoria
        IValidadorPassword validador = new ValidadorPasswordSeguro();       // Validación segura
        IAutenticacion autenticacion = new AutenticacionBasica(repositorio);// Autenticación básica

        logger.info("✓ Sistema configurado con inyección de dependencias (DIP)");

        // === CREACIÓN DE USUARIOS CON DIFERENTES ROLES (LSP) ===
        logger.info("Creando usuarios de prueba...");

        // Todos los roles son intercambiables gracias al principio LSP
        Usuario admin = new Usuario("1", "Ana Admin", "admin@empresa.com", "Admin123", new Administrador());
        Usuario user = new Usuario("2", "Juan Usuario", "juan@empresa.com", "User123", new UsuarioRegular());
        Usuario guest = new Usuario("3", "Pedro Invitado", "guest@empresa.com", "Guest123", new Invitado());

        // Guardar usuarios en el repositorio
        repositorio.guardar(admin);     // Guarda administrador
        repositorio.guardar(user);      // Guarda usuario regular
        repositorio.guardar(guest);     // Guarda invitado

        logger.info("✓ Usuarios creados con diferentes roles (LSP)");

        // === CREACIÓN DEL SERVICIO PRINCIPAL ===
        ServicioControlUsuarios servicio = new ServicioControlUsuarios(autenticacion, repositorio, validador);
        logger.info("✓ Servicio principal creado");
        logger.info(""); // Línea en blanco

        // === ESCENARIO 1: ADMINISTRADOR ===
        logger.info("--- ESCENARIO 1: ADMINISTRADOR ---");

        // Autenticar como administrador
        if (autenticacion.autenticar("admin@empresa.com", "Admin123")) {
            String adminName = servicio.verPerfil().getNombre();
            logger.info(String.format("✓ Administrador autenticado: %s", adminName));

            // El administrador puede crear usuarios (ISP - interfaz específica)
            try {
                // Crear nuevo moderador (OCP - nuevo rol sin modificar código)
                Usuario nuevoModerador = new Usuario("4", "Luis Moderador", "mod@empresa.com", "Moderador123", new Moderador());
                servicio.crearUsuario(nuevoModerador);
                logger.info("✓ Moderador creado exitosamente");
            } catch (Exception e) {
                logger.warning(String.format("✗ Error creando usuario: %s", e.getMessage()));
            }

            // Verificar permisos del administrador
            boolean puedeAdminPanel = servicio.puedeAcceder("admin_panel");
            boolean puedeUserMgmt = servicio.puedeAcceder("user_management");

            logger.info(String.format("Admin puede acceder a admin_panel: %s", puedeAdminPanel));
            logger.info(String.format("Admin puede acceder a user_management: %s", puedeUserMgmt));
        } else {
            logger.severe("✗ Falló la autenticación del administrador");
        }

        logger.info(""); // Línea en blanco

        // === ESCENARIO 2: USUARIO REGULAR ===
        logger.info("--- ESCENARIO 2: USUARIO REGULAR ---");

        // Cambiar a usuario regular
        if (autenticacion.autenticar("juan@empresa.com", "User123")) {
            String userName = servicio.verPerfil().getNombre();
            logger.info(String.format("✓ Usuario regular autenticado: %s", userName));

            // Verificar permisos limitados
            boolean puedeAdminPanel = servicio.puedeAcceder("admin_panel");
            boolean puedePublicContent = servicio.puedeAcceder("public_content");

            logger.info(String.format("Usuario puede acceder a admin_panel: %s", puedeAdminPanel));
            logger.info(String.format("Usuario puede acceder a public_content: %s", puedePublicContent));

            // El usuario puede cambiar su contraseña
            try {
                servicio.cambiarPassword("NuevoPass123");
                logger.info("✓ Contraseña cambiada exitosamente");
            } catch (Exception e) {
                logger.warning(String.format("✗ Error cambiando contraseña: %s", e.getMessage()));
            }

            // Intentar crear usuario (debe fallar)
            try {
                Usuario otroUsuario = new Usuario("5", "Test", "test@test.com", "Test123", new UsuarioRegular());
                servicio.crearUsuario(otroUsuario);
                logger.warning("✗ ERROR: El usuario regular pudo crear un usuario (esto no debería pasar)");
            } catch (SecurityException e) {
                logger.info(String.format("✓ Seguridad funcionando: %s", e.getMessage()));
            }
        } else {
            logger.severe("✗ Falló la autenticación del usuario regular");
        }

        logger.info(""); // Línea en blanco

        // === ESCENARIO 3: INVITADO ===
        logger.info("--- ESCENARIO 3: INVITADO ---");

        // Cambiar a invitado
        if (autenticacion.autenticar("guest@empresa.com", "Guest123")) {
            String guestName = servicio.verPerfil().getNombre();
            logger.info(String.format("✓ Invitado autenticado: %s", guestName));

            // Verificar permisos muy limitados
            boolean puedePublicContent = servicio.puedeAcceder("public_content");
            boolean puedeAdminPanel = servicio.puedeAcceder("admin_panel");
            boolean puedeUserMgmt = servicio.puedeAcceder("user_management");

            logger.info(String.format("Invitado puede acceder a public_content: %s", puedePublicContent));
            logger.info(String.format("Invitado puede acceder a admin_panel: %s", puedeAdminPanel));
            logger.info(String.format("Invitado puede acceder a user_management: %s", puedeUserMgmt));
        } else {
            logger.severe("✗ Falló la autenticación del invitado");
        }

        logger.info(""); // Línea en blanco

        // === DEMOSTRACIÓN DE EXTENSIBILIDAD (OCP) ===
        logger.info("--- DEMOSTRACIÓN DE EXTENSIBILIDAD (OCP) ---");

        try {
            // Cambiar a autenticación OAuth sin modificar código existente
            IAutenticacion authOAuth = new AutenticacionOAuth(repositorio);
            ServicioControlUsuarios servicioOAuth = new ServicioControlUsuarios(authOAuth, repositorio, validador);

            // Simular autenticación OAuth
            if (authOAuth.autenticar("admin@empresa.com", "oauth_token_123")) {
                String oauthUserName = servicioOAuth.verPerfil().getNombre();
                logger.info(String.format("✓ Autenticación OAuth exitosa para: %s", oauthUserName));
                logger.info("✓ Sistema funciona con diferentes tipos de autenticación (OCP)");
            } else {
                logger.warning("✗ Falló la autenticación OAuth");
            }
        } catch (Exception e) {
            logger.severe(String.format("✗ Error en demostración OAuth: %s", e.getMessage()));
        }

        logger.info(""); // Línea en blanco

        // === RESUMEN DE PRINCIPIOS SOLID APLICADOS ===
        logger.info("=== PRINCIPIOS SOLID DEMOSTRADOS ===");
        logger.info("✓ SRP: Cada clase tiene una responsabilidad única");
        logger.info("✓ OCP: Sistema abierto para extensión, cerrado para modificación");
        logger.info("✓ LSP: Subclases son intercambiables sin romper funcionalidad");
        logger.info("✓ ISP: Interfaces específicas y cohesivas");
        logger.info("✓ DIP: Dependencias de abstracciones, no de concreciones");

        // Log final de ejecución completada
        logger.info(""); // Línea en blanco
        logger.info("=== DEMOSTRACIÓN COMPLETADA EXITOSAMENTE ===");
    }
}