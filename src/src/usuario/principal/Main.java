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
            } catch (Exception