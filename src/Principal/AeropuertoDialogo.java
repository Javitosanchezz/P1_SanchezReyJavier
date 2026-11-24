package Principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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

        if (operacion.startsWith("configuracion.")) {
            String configuracion = limpiarSalidaConfiguracion(resultado);
            if (operacion.endsWith(".mostrar")) {
                mostrarLista(padre, configuracion);
            } else {
                JOptionPane.showMessageDialog(padre, ultimoMensaje(configuracion, operacion), "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

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
        if (!mostrarTabla(padre, operacion, lineas)) {
            mostrarLista(padre, lineas);
        }
    }

    private static void mostrarLista(Window padre, String texto) {
        mostrarLista(padre, texto.split("\\R", -1));
    }

    private static void mostrarLista(Window padre, String[] lineas) {
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

    private static boolean mostrarTabla(Window padre, String operacion, String[] lineas) {
        if (operacion.startsWith("vuelos.")) {
            return mostrarTablaVuelos(padre, lineas);
        }
        String[] cabeceras;
        int columnas;
        if (operacion.startsWith("pasajeros.")) {
            cabeceras = new String[] { "Código", "Nombre", "Apellidos", "DNI", "País", "Correo", "Tipo" };
            columnas = 7;
        } else if (operacion.startsWith("reservas.")) {
            cabeceras = new String[] { "Código", "Pasajero", "Vuelo", "Asiento", "Clase", "Precio", "Moneda", "Estado", "Fecha", "Canal" };
            columnas = 10;
        } else if (operacion.startsWith("equipajes.")) {
            cabeceras = new String[] { "Código", "Reserva", "Peso", "Tipo", "Estado", "Prioridad" };
            columnas = 6;
        } else if (operacion.startsWith("embarques.")) {
            cabeceras = new String[] { "Código", "Reserva", "Fecha y hora", "Puerta", "Estado", "Observaciones" };
            columnas = 6;
        } else if (operacion.startsWith("incidencias.")) {
            cabeceras = new String[] { "Fecha", "Código", "Vuelo", "Tipo", "Duración", "Descripción", "Prioridad", "Estado" };
            columnas = 8;
        } else {
            return false;
        }

        java.util.ArrayList<String[]> filas = new java.util.ArrayList<String[]>();
        for (String linea : lineas) {
            String[] valores = operacion.startsWith("incidencias.")
                    ? linea.trim().split("\\|", -1) : linea.trim().split("\\s+");
            if (valores.length < columnas) {
                continue;
            }
            String[] fila = new String[columnas];
            System.arraycopy(valores, 0, fila, 0, columnas);
            if (columnas == 6 && operacion.startsWith("embarques.") && valores.length > 6) {
                fila[5] = linea.substring(linea.indexOf(valores[5])).trim();
            }
            filas.add(fila);
        }
        if (filas.isEmpty()) {
            return false;
        }

        DefaultTableModel modelo = new DefaultTableModel(cabeceras, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String[] fila : filas) {
            modelo.addRow(fila);
        }
        JTable tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        JDialog dialogo = new JDialog(padre, "Resultado", JDialog.ModalityType.APPLICATION_MODAL);
        dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        dialogo.setSize(900, 360);
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
        return true;
    }

    private static boolean mostrarTablaVuelos(Window padre, String[] lineas) {
        java.util.ArrayList<String[]> filas = new java.util.ArrayList<String[]>();
        for (String linea : lineas) {
            java.util.Map<String, String> valores = new java.util.HashMap<String, String>();
            for (String parte : linea.split("\\|\\|")) {
                String texto = parte.trim();
                int separador = texto.indexOf('=');
                if (separador >= 0) {
                    valores.put(texto.substring(0, separador).trim().toLowerCase(), texto.substring(separador + 1).trim());
                } else {
                    String[] trozos = texto.split("\\s+", 2);
                    if (trozos.length == 2) valores.put(trozos[0].trim().toLowerCase(), trozos[1].trim());
                }
            }
            if (valores.containsKey("codigo")) {
                filas.add(new String[] { valores.getOrDefault("codigo", ""), valores.getOrDefault("aerolinea", ""),
                        valores.getOrDefault("avion", ""), valores.getOrDefault("estado", "") });
            }
        }
        if (filas.isEmpty()) return false;

        DefaultTableModel modelo = new DefaultTableModel(new String[] { "Código", "Aerolínea", "Avión", "Estado" }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String[] fila : filas) modelo.addRow(fila);
        JTable tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        JDialog dialogo = new JDialog(padre, "Resultado", JDialog.ModalityType.APPLICATION_MODAL);
        dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        dialogo.setSize(700, 320);
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
        return true;
    }

    private static String limpiarSalidaConfiguracion(String resultado) {
        StringBuilder limpia = new StringBuilder();
        for (String linea : resultado.split("\\R")) {
            String texto = linea.trim();
            if (texto.equals("CONFIGURACION") || texto.matches("^[0-4]\\. .*")
                    || texto.startsWith("Introduce la ruta:") || texto.startsWith("Nuevo valor:")) {
                continue;
            }
            if (!texto.isEmpty()) {
                if (limpia.length() > 0) {
                    limpia.append(System.lineSeparator());
                }
                limpia.append(texto);
            }
        }
        return limpia.toString();
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
