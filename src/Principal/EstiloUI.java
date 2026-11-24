package Principal;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

public final class EstiloUI {
    private static boolean aplicado;

    private EstiloUI() {
    }

    public static void aplicar() {
        if (aplicado) return;
        aplicado = true;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Se conserva el estilo por defecto si el sistema no lo admite.
        }
        Color fondo = new Color(232, 243, 248);
        Color azulOscuro = new Color(16, 61, 89);
        Color azulBoton = new Color(207, 229, 240);
        Color azulClaro = new Color(218, 237, 245);
        UIManager.put("Panel.background", fondo);
        UIManager.put("OptionPane.background", fondo);
        UIManager.put("Button.background", azulBoton);
        UIManager.put("Button.foreground", azulOscuro);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("List.selectionBackground", azulOscuro);
        UIManager.put("List.selectionForeground", Color.WHITE);
        UIManager.put("Table.selectionBackground", azulClaro);
        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
    }

    public static Border bordeSeccion() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 211, 224)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }
}
