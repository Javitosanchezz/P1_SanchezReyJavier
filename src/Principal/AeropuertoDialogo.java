package Principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public final class AeropuertoDialogo {

    private AeropuertoDialogo() {
    }

    public static void ejecutar(Window padre, String operacion, String... etiquetas) {
        java.util.ArrayList<String> valores = new java.util.ArrayList<String>();
        for (String etiqueta : etiquetas) {
            String valor = JOptionPane.showInputDialog(padre, etiqueta);
            if (valor == null) {
                return;
            }
            valores.add(valor);
        }

        if (operacion.startsWith("configuracion.")) {
            String opcion = operacion.endsWith("mostrar") ? "1"
                    : operacion.endsWith("consultar") ? "2"
                    : operacion.endsWith("modificar") ? "3" : "4";
            valores.add(0, opcion);
            valores.add("0");
        }

        String resultado = Aeropuerto.ejecutarInterfaz(operacion,
                valores.toArray(new String[valores.size()]));

        if (esOperacionDeEscritura(operacion)) {
            JOptionPane.showMessageDialog(padre, ultimoMensaje(resultado, operacion), "Resultado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] lineas = resultado.split("\\R", -1);
        if (operacion.endsWith(".buscar")) {
            java.util.ArrayList<String> filtradas = new java.util.ArrayList<String>();
            for (String linea : lineas) {
                String texto = linea.trim().toLowerCase(java.util.Locale.ROOT);
                boolean esPrompt = texto.startsWith("introduce el c")
                        || texto.startsWith("introduce el codigo")
                        || texto.contains("que quieras buscar");
                if (!esPrompt && !texto.isEmpty()) {
                    filtradas.add(linea);
                }
            }
            lineas = filtradas.toArray(new String[filtradas.size()]);
            if (lineas.length == 0) {
                lineas = new String[] { "No se encontraron resultados." };
            }
        }
        JList<String> lista = new JList<String>(lineas);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFocusable(true);
        lista.setSelectionBackground(new Color(51, 102, 153));
        lista.setSelectionForeground(Color.WHITE);
        lista.setVisibleRowCount(Math.min(18, Math.max(1, lineas.length)));
        lista.setFixedCellWidth(700);
        JDialog dialogo = new JDialog(padre, "Resultado", JDialog.ModalityType.APPLICATION_MODAL);
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lista.getSelectedValue() != null) {
                dialogo.setTitle("Seleccionado: " + lista.getSelectedValue());
            }
        });
        dialogo.add(new JScrollPane(lista), BorderLayout.CENTER);
        dialogo.pack();
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
    }

    private static boolean esOperacionDeEscritura(String operacion) {
        return operacion.endsWith(".insertar")
                || operacion.endsWith(".actualizar")
                || operacion.endsWith(".borrar")
                || operacion.endsWith(".crear")
                || operacion.endsWith(".modificar")
                || operacion.endsWith(".cancelar");
    }

    private static String ultimoMensaje(String resultado, String operacion) {
        String[] lineas = resultado.split("\\R");
        for (int i = lineas.length - 1; i >= 0; i--) {
            if (!lineas[i].trim().isEmpty()) {
                String mensaje = lineas[i].trim();
                String minusculas = mensaje.toLowerCase(java.util.Locale.ROOT);
                if (minusculas.startsWith("introduce el tipo del pasajero")) {
                    return operacion.endsWith(".insertar")
                            ? "Pasajero insertado correctamente."
                            : "Pasajero actualizado correctamente.";
                }
                if (minusculas.startsWith("introduce ")
                        || minusculas.startsWith("que ")) {
                    return "Operación realizada correctamente.";
                }
                return mensaje;
            }
        }
        return "Operación completada.";
    }
}
