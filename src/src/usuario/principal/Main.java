package usuario.principal;

import usuario.interfaces.*;
import usuario.implementaciones.*;
import usuario.modelo.Usuario;
import usuario.modelo.roles.*;
import usuario.servicio.ServicioControlUsuarios;

/**
 * Clase principal para demostrar el sistema
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE CONTROL DE USUARIOS - PRINCIPIOS SOLID ===\n");

        // === CONFIGURACIÓN DEL SISTEMA (PRINCIPIO DIP) ===
        // Se crean las implementaciones concretas
        IRepositorioUsuario repositorio = new RepositorioUsuarioMemoria();  // Persistencia en memoria
        IValidadorPassword validador = new ValidadorPasswordSeguro();       // Validación segura
        IAutenticacion autenticacion = new AutenticacionBasica(repositorio);// Autenticación básica

        System.out.println("✓ Sistema configurado con inyección de dependencias (DIP)");

        // === CREACIÓN DE USUARIOS CON DIFERENTES ROLES (LSP) ===
        // Todos los roles son intercambiables gracias al principio LSP
        Usuario admin = new Usuario("1", "Ana Admin", "admin@empresa.com", "Admin123", new Administrador());
        Usuario user = new Usuario("2", "Juan Usuario", "juan@empresa.com", "User123", new UsuarioRegular());
        Usuario guest = new Usuario("3", "Pedro Invitado", "guest@empresa.com", "Guest123", new Invitado());

        // Guardar usuarios en el repositorio
        repositorio.guardar(admin);     // Guarda administrador
        repositorio.guardar(user);      // Guarda usuario regular
        repositorio.guardar(guest);     // Guarda invitado

        System.out.println("✓ Usuarios creados con diferentes roles (LSP)");

        // === CREACIÓN DEL SERVICIO PRINCIPAL ===
        ServicioControlUsuarios servicio = new ServicioControlUsuarios(autenticacion, repositorio, validador);
        System.out.println("✓ Servicio principal creado\n");

        // === ESCENARIO 1: ADMINISTRADOR ===
        System.out.println("--- ESCENARIO 1: ADMINISTRADOR ---");

        // Autenticar como administrador
        if (autenticacion.autenticar("admin@empresa.com", "Admin123")) {
            System.out.println("✓ Administrador autenticado: " + servicio.verPerfil().getNombre());

            // El administrador puede crear usuarios (ISP - interfaz específica)
            try {
                // Crear nuevo moderador (OCP - nuevo rol sin modificar código)
                Usuario nuevoModerador = new Usuario("4", "Luis Moderador", "mod@empresa.com", "Moderador123", new Moderador());
                servicio.crearUsuario(nuevoModerador);
                System.out.println("✓ Moderador creado exitosamente");
            } catch (Exception e) {
                System.out.println("✗ Error creando usuario: " + e.getMessage());
            }

            // Verificar permisos del administrador
            System.out.println("Admin puede acceder a admin_panel: " + servicio.puedeAcceder("admin_panel"));
            System.out.println("Admin puede acceder a user_management: " + servicio.puedeAcceder("user_management"));
        }

        System.out.println();

        // === ESCENARIO 2: USUARIO REGULAR ===
        System.out.println("--- ESCENARIO 2: USUARIO REGULAR ---");

        // Cambiar a usuario regular
        if (autenticacion.autenticar("juan@empresa.com", "User123")) {
            System.out.println("✓ Usuario regular autenticado: " + servicio.verPerfil().getNombre());

            // Verificar permisos limitados
            System.out.println("Usuario puede acceder a admin_panel: " + servicio.puedeAcceder("admin_panel"));
            System.out.println("Usuario puede acceder a public_content: " + servicio.puedeAcceder("public_content"));

            // El usuario puede cambiar su contraseña
            try {
                servicio.cambiarPassword("NuevoPass123");
                System.out.println("✓ Contraseña cambiada exitosamente");
            } catch (Exception e) {
                System.out.println("✗ Error cambiando contraseña: " + e.getMessage());
            }

            // Intentar crear usuario (debe fallar)
            try {
                Usuario otroUsuario = new Usuario("5", "Test", "test@test.com", "Test123", new UsuarioRegular());
                servicio.crearUsuario(otroUsuario);
            } catch (SecurityException e) {
                System.out.println("✓ Seguridad funcionando: " + e.getMessage());
            }
        }

        System.out.println();

        // === ESCENARIO 3: INVITADO ===
        System.out.println("--- ESCENARIO 3: INVITADO ---");

        // Cambiar a invitado
        if (autenticacion.autenticar("guest@empresa.com", "Guest123")) {
            System.out.println("✓ Invitado autenticado: " + servicio.verPerfil().getNombre());

            // Verificar permisos muy limitados
            System.out.println("Invitado puede acceder a public_content: " + servicio.puedeAcceder("public_content"));
            System.out.println("Invitado puede acceder a admin_panel: " + servicio.puedeAcceder("admin_panel"));
            System.out.println("Invitado puede acceder a user_management: " + servicio.puedeAcceder("user_management"));
        }

        System.out.println();

        // === DEMOSTRACIÓN DE EXTENSIBILIDAD (OCP) ===
        System.out.println("--- DEMOSTRACIÓN DE EXTENSIBILIDAD (OCP) ---");

        // Cambiar a autenticación OAuth sin modificar código existente
        IAutenticacion authOAuth = new AutenticacionOAuth(repositorio);
        ServicioControlUsuarios servicioOAuth = new ServicioControlUsuarios(authOAuth, repositorio, validador);

        // Simular autenticación OAuth
        if (authOAuth.autenticar("admin@empresa.com", "oauth_token_123")) {
            System.out.println("✓ Autenticación OAuth exitosa para: " + servicioOAuth.verPerfil().getNombre());
            System.out.println("✓ Sistema funciona con diferentes tipos de autenticación (OCP)");
        }

        System.out.println();

        // === RESUMEN DE PRINCIPIOS SOLID APLICADOS ===
        System.out.println("=== PRINCIPIOS SOLID DEMOSTRADOS ===");
        System.out.println("✓ SRP: Cada clase tiene una responsabilidad única");
        System.out.println("✓ OCP: Sistema abierto para extensión, cerrado para modificación");
        System.out.println("✓ LSP: Subclases son intercambiables sin romper funcionalidad");
        System.out.println("✓ ISP: Interfaces específicas y cohesivas");
        System.out.println("✓ DIP: Dependencias de abstracciones, no de concreciones");
    }
}