package usuario.implementaciones;

import usuario.interfaces.IRepositorioUsuario;
import usuario.modelo.Usuario;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación de repositorio en memoria - PRINCIPIOS DIP y SRP
 * SRP: Solo se encarga de persistir usuarios
 * DIP: Implementa la interfaz IRepositorioUsuario
 */
public class RepositorioUsuarioMemoria implements IRepositorioUsuario {

    // Map para almacenar usuarios en memoria (clave: ID, valor: Usuario)
    private Map<String, Usuario> usuarios = new HashMap<>();

    /**
     * Busca un usuario por email recorriendo todos los usuarios
     */
    @Override
    public Usuario buscarPorEmail(String email) {
        // stream() convierte la colección en un flujo de datos
        return usuarios.values().stream()
                // filter() filtra usuarios cuyo email coincida
                .filter(u -> u.getEmail().equals(email))
                // findFirst() retorna el primer elemento encontrado
                .findFirst()
                // orElse(null) retorna null si no encuentra nada
                .orElse(null);
    }

    /**
     * Guarda un usuario usando su ID como clave
     */
    @Override
    public void guardar(Usuario usuario) {
        // put() almacena el usuario en el Map usando su ID como clave
        usuarios.put(usuario.getId(), usuario);
    }

    /**
     * Verifica si un email ya existe usando el método buscarPorEmail
     */
    @Override
    public boolean existeEmail(String email) {
        // Reutiliza buscarPorEmail y verifica si el resultado no es null
        return buscarPorEmail(email) != null;
    }
}