package InicioSesion;

public final class Sesion {
    private static String usuario;
    private static String rol = "USER";

    private Sesion() {
    }

    public static void iniciar(String nombreUsuario, String rolUsuario) {
        usuario = nombreUsuario;
        rol = "ADMIN".equalsIgnoreCase(rolUsuario) ? "ADMIN" : "USER";
    }

    public static String getUsuario() {
        return usuario;
    }

    public static String getRol() {
        return rol;
    }

    public static boolean esAdmin() {
        return "ADMIN".equals(rol);
    }

    public static void cerrar() {
        usuario = null;
        rol = "USER";
    }
}
