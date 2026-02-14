package compilador;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.text.*;

public class IDE extends JFrame {

    // --area de declaracion de botones---
    private JButton btnNuevo, btnGuardar, btnAbrir;
    private JButton btnReserved, btnIdentifiers, btnTokens, btnCompilar;

    // ----areas generales del programa---
    private JTextArea areaCodigo;
    private JTextArea areaLineas;
    private JTextArea areaConsola;

    public IDE() {
        initComponents();
        setTitle("Prueba IDE - Compilador");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // -- area de edicion de la consola---
        areaCodigo = new JTextArea();
        areaCodigo.setFont(new Font("Consolas", Font.PLAIN, 14));

        areaLineas = new JTextArea("1");
        areaLineas.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaLineas.setBackground(new Color(240, 240, 240));
        areaLineas.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaCodigo);
        scroll.setRowHeaderView(areaLineas);
        add(scroll, BorderLayout.CENTER);

        areaCodigo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
        });

        // --area de consola donde se mostrara el resultado---
        areaConsola = new JTextArea(6, 20);
        areaConsola.setEditable(false);
        areaConsola.setBackground(Color.BLACK);
        areaConsola.setForeground(Color.GREEN);
        areaConsola.setFont(new Font("Consolas", Font.PLAIN, 12));
        add(new JScrollPane(areaConsola), BorderLayout.SOUTH);
        
        areaConsola.setBackground(new Color(245, 245, 245));
areaConsola.setForeground(Color.DARK_GRAY);
areaConsola.setCaretColor(Color.BLACK);

        // -----area de botonos---
        btnNuevo = new JButton(icon("/iconos/nuevo.png"));
        btnGuardar = new JButton(icon("/iconos/guardar.png"));
        btnAbrir = new JButton(icon("/iconos/abrir.png"));
        btnReserved = new JButton(icon("/iconos/reservadas.png"));
        btnIdentifiers = new JButton(icon("/iconos/ident.png"));
        btnTokens = new JButton(icon("/iconos/tokens.png"));
        btnCompilar = new JButton(icon("/iconos/compilar.png"));

        // ----area de acciones que realiza los botones---
        btnNuevo.addActionListener(e -> nuevoArchivo());
        //btnAbrir.addActionListener(e -> abrirArchivo());
       // btnGuardar.addActionListener(e -> guardarArchivo());
        btnCompilar.addActionListener(e -> compilar());
        btnGuardar.addActionListener(e -> guardarEnEscritorioPrueba());
        btnAbrir.addActionListener(e -> abrirPruebaTxt());
        btnTokens.addActionListener(e -> mostrarTokens());
        btnReserved.addActionListener(e -> mostrarReservadas());
        btnIdentifiers.addActionListener(e -> mostrarIdentificadores());

        JButton[] botones = {
            btnNuevo, btnGuardar, btnAbrir,
            btnReserved, btnIdentifiers, btnTokens, btnCompilar
        };

        for (JButton b : botones) {
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
        }

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(btnNuevo);
        toolBar.add(btnGuardar);
        toolBar.add(btnAbrir);
        toolBar.addSeparator();
        toolBar.add(btnReserved);
        toolBar.add(btnIdentifiers);
        toolBar.add(btnTokens);
        toolBar.addSeparator();
        toolBar.add(btnCompilar);

        add(toolBar, BorderLayout.NORTH);
    }

    // ----area de los metodos que utilizara el programa--

    private void actualizarLineas() {
        int total = areaCodigo.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= total; i++) sb.append(i).append("\n");
        areaLineas.setText(sb.toString());
    }

    private void nuevoArchivo() {
        areaCodigo.setText("");
        areaConsola.setText("");
        setTitle("Nuevo archivo");
    }

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                areaCodigo.read(br, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al abrir archivo");
            }
        }
    }

    private void guardarArchivo() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                areaCodigo.write(bw);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar archivo");
            }
        }
    }
    private void limpiarResaltado() {
    areaCodigo.getHighlighter().removeAllHighlights();
}
   private void resaltarLinea(int linea) {
    try {
        int inicio = areaCodigo.getLineStartOffset(linea - 1);
        int fin = areaCodigo.getLineEndOffset(linea - 1);

        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

        areaCodigo.getHighlighter().addHighlight(inicio, fin, painter);
        areaCodigo.setCaretPosition(inicio);

    } catch (BadLocationException e) {
        e.printStackTrace();
    }

}
    private void subrayarTexto(int linea, String texto) {
    try {
        int inicioLinea = areaCodigo.getLineStartOffset(linea - 1);
        int finLinea = areaCodigo.getLineEndOffset(linea - 1);
        String lineaTexto = areaCodigo.getText(inicioLinea, finLinea - inicioLinea);

        int inicioError = lineaTexto.indexOf(texto);
        if (inicioError >= 0) {
            Highlighter.HighlightPainter painter =
                    new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

            areaCodigo.getHighlighter().addHighlight(
                    inicioLinea + inicioError,
                    inicioLinea + inicioError + texto.length(),
                    painter
            );
        }

    } catch (BadLocationException e) {
        e.printStackTrace();
    }
}

  private void compilar() {

    limpiarResaltado();
    areaConsola.setText("Iniciando compilación...\n\n");

    String[] lineas = areaCodigo.getText().split("\n");

    int balanceLlaves = 0;
    int balanceParentesis = 0;

    for (int i = 0; i < lineas.length; i++) {

        String linea = lineas[i].trim();
        int numLinea = i + 1;

        if (linea.isEmpty()) continue;

        for (char c : linea.toCharArray()) {
            if (c == '{') balanceLlaves++;
            if (c == '}') balanceLlaves--;
            if (c == '(') balanceParentesis++;
            if (c == ')') balanceParentesis--;
        }

        // --area de error y punto y coma--
        if (!linea.endsWith(";")
                && !linea.endsWith("{")
                && !linea.endsWith("}")
                && !linea.startsWith("if")
                && !linea.startsWith("while")
                && !linea.startsWith("for")) {

            areaConsola.append("ERROR en línea " + numLinea + ": Falta ';'\n");

            resaltarLinea(numLinea);
            subrayarTexto(numLinea, linea);

            areaConsola.append("\nCompilación fallida");
            return;
        }
    }

    if (balanceLlaves != 0) {
        areaConsola.append("ERROR: Llaves desbalanceadas\n");
        areaConsola.append("\nCompilación fallida");
        return;
    }

    if (balanceParentesis != 0) {
        areaConsola.append("ERROR: Paréntesis desbalanceados\n");
        areaConsola.append("\nCompilación fallida");
        return;
    }

    areaConsola.append("Compilación exitosa");
}

   private void analizarTokens(String codigo, String modo) {

    String[] palabrasReservadas = {
        "int", "float", "if", "else", "while", "for", "return"
    };

    String[] tokens = codigo.split("\\s+");

    for (String token : tokens) {

        if (esReservada(token, palabrasReservadas)) {
            if (modo.equals("TODO") || modo.equals("RESERVADAS")) {
                areaConsola.append(token + " → RESERVADA\n");
            }
        }
        else if (token.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            if (modo.equals("TODO") || modo.equals("IDENTIFICADORES")) {
                areaConsola.append(token + " → IDENTIFICADOR\n");
            }
        }
        else if (token.matches("\\d+")) {
            if (modo.equals("TODO")) {
                areaConsola.append(token + " → NuMERO\n");
            }
        }
        else {
            if (modo.equals("TODO")) {
                areaConsola.append(token + " → SlMBOLO\n");
            }
        }
    }
}
    
    private void guardarEnEscritorioPrueba() {
    String texto = areaCodigo.getText();

    if (texto.trim().isEmpty()) {
        areaConsola.append("No hay texto para guardar\n");
        return;
    }

    String escritorio = System.getProperty("user.home") + File.separator + "Desktop";
    File archivo = new File(escritorio, "pruebaIDE.txt");

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
        bw.write(texto);
        areaConsola.append("Archivo guardado en el Escritorio como pruebaIDE.txt\n");
    } catch (IOException e) {
        areaConsola.append("Error al guardar el archivo\n");
    }
}
    private void abrirPruebaTxt() {
    String escritorio = System.getProperty("user.home") + File.separator + "Desktop";
    File archivo = new File(escritorio, "PruebaIDE.txt");

    if (!archivo.exists()) {
        areaConsola.append(" El archivo no existe\n");
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        areaCodigo.read(br, null);
        areaConsola.append(" Archivo cargado desde el Escritorio\n");
    } catch (IOException e) {
        areaConsola.append(" Error al abrir archivo\n");
    }
}

    private boolean esReservada(String t, String[] r) {
        for (String s : r) if (s.equals(t)) return true;
        return false;
    }
    

    private ImageIcon icon(String path) {
        java.net.URL url = getClass().getResource(path);
        return url == null ? null :
                new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IDE().setVisible(true));
    }private void mostrarTokens() {
    areaConsola.setText("");
    analizarTokens(areaCodigo.getText(), "TODO");
}
    private void mostrarReservadas() {
    areaConsola.setText("");
    analizarTokens(areaCodigo.getText(), "RESERVADAS");
}
    private void mostrarIdentificadores() {
    areaConsola.setText("");
    analizarTokens(areaCodigo.getText(), "IDENTIFICADORES");
}
    private boolean parentesisBalanceados(String codigo) {
    int contador = 0;
    for (char c : codigo.toCharArray()) {
        if (c == '(') contador++;
        if (c == ')') contador--;
        if (contador < 0) return false;
    }
    return contador == 0;
}

private boolean llavesBalanceadas(String codigo) {
    int contador = 0;
    for (char c : codigo.toCharArray()) {
        if (c == '{') contador++;
        if (c == '}') contador--;
        if (contador < 0) return false;
    }
    return contador == 0;
}

private boolean faltanPuntoComa(String codigo) {
    String[] lineas = codigo.split("\n");

    for (String linea : lineas) {
        linea = linea.trim();

        if (linea.isEmpty()) continue;
        if (linea.endsWith("{") || linea.endsWith("}")) continue;
        if (linea.startsWith("if") || linea.startsWith("while") || linea.startsWith("for")) continue;

        if (!linea.endsWith(";")) {
            return true;
        }
    }
    return false;
}
    
}