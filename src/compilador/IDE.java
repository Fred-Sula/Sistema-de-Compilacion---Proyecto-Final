package compilador;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.text.*;
import java.util.Locale;

import com.proyecto.compilador.ArchivoHTML;
import com.proyecto.compilador.Tokens;
import com.proyecto.compilador.utilidad.Fila;

public class IDE extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// --area de declaracion de botones---
	private JButton btnNuevo, btnGuardar, btnAbrir;
	private JButton btnReserved, btnIdentifiers, btnAnalizarTokens, btnHtmlTokens, btnCompilar, btnHtml;

	// ----areas generales del programa---
	private JTextArea areaCodigo;
	private JTextArea areaLineas;
	private JTextArea areaConsola;

	Tokens tokens;

	public IDE() {
		Locale.setDefault(new Locale("es", "ES"));
		initComponents();
		configurarAtajos();
		setTitle("Sistema de Compilación - Proyecto Final");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		tokens = new Tokens();
	}

	private void initComponents() {

		getContentPane().setLayout(new BorderLayout());

		// -- area de edicion de la consola---
		areaCodigo = new JTextArea();
		areaCodigo.setFont(new Font("Consolas", Font.PLAIN, 14));

		areaLineas = new JTextArea("1");
		areaLineas.setFont(new Font("Consolas", Font.PLAIN, 14));
		areaLineas.setBackground(new Color(240, 240, 240));
		areaLineas.setEditable(false);

		JScrollPane scroll = new JScrollPane(areaCodigo);
		scroll.setRowHeaderView(areaLineas);
		getContentPane().add(scroll, BorderLayout.CENTER);

		areaCodigo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				actualizarLineas();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				actualizarLineas();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				actualizarLineas();
			}
		});

		// --area de consola donde se mostrara el resultado---
		areaConsola = new JTextArea(6, 20);
		areaConsola.setEditable(false);
		areaConsola.setBackground(Color.BLACK);
		areaConsola.setForeground(Color.GREEN);
		areaConsola.setFont(new Font("Consolas", Font.PLAIN, 12));
		getContentPane().add(new JScrollPane(areaConsola), BorderLayout.SOUTH);

		areaConsola.setBackground(new Color(245, 245, 245));
		areaConsola.setForeground(Color.DARK_GRAY);
		areaConsola.setCaretColor(Color.BLACK);

		// -----area de botonos---
		btnNuevo = new JButton(icono("/iconos/archivo_nuevo.png", 24, 24));
		btnNuevo.setToolTipText("Nuevo archivo (Ctrl+N)");
		btnGuardar = new JButton(icono("/iconos/archivo_guardar.png", 24, 24));
		btnGuardar.setToolTipText("Guardar archivo (Ctrl+S)");
		btnAbrir = new JButton(icono("/iconos/archivo_abrir.png", 24, 24));
		btnAbrir.setToolTipText("Abrir archivo (Ctrl+O)");
		btnReserved = new JButton(icon("/iconos/reservadas.png"));
		btnReserved.setToolTipText("Mostrar palabras reservadas");
		btnIdentifiers = new JButton(icon("/iconos/ident.png"));
		btnIdentifiers.setToolTipText("Mostrar identificadores");
		btnAnalizarTokens = new JButton(icono("/iconos/analiza_tokens.png", 24, 24));
		btnAnalizarTokens.setToolTipText("Mostrar todos los tokens");
		btnCompilar = new JButton(icono("/iconos/compilar_codigo.png", 24, 24));
		btnCompilar.setToolTipText("Compilar código");
		btnHtmlTokens = new JButton(icono("/iconos/html_token.png", 24, 24));
		btnHtmlTokens.setToolTipText("Bitácora de Tokens");
		btnHtml = new JButton(icon("/iconos/html.png"));

		// ----area de acciones que realiza los botones---
		btnNuevo.addActionListener(e -> nuevoArchivo());
		// btnAbrir.addActionListener(e -> abrirArchivo());
		// btnGuardar.addActionListener(e -> guardarArchivo());
		btnCompilar.addActionListener(e -> compilar());
		btnGuardar.addActionListener(e -> guardarArchivo());
		btnAbrir.addActionListener(e -> abrirArchivo());
		btnAnalizarTokens.addActionListener(e -> mostrarTokens());
		btnReserved.addActionListener(e -> mostrarReservadas());
		btnIdentifiers.addActionListener(e -> mostrarIdentificadores());
		btnHtmlTokens.addActionListener(e -> htmlTokens());
		btnHtml.addActionListener(e -> html());

		JButton[] botones = { btnNuevo, btnGuardar, btnAbrir, btnReserved, btnIdentifiers, btnAnalizarTokens,
				btnCompilar, btnHtmlTokens, btnHtml };

		for (JButton b : botones) {
			b.setFocusPainted(false);
			b.setBorderPainted(false);
			b.setContentAreaFilled(false);
		}

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(btnNuevo);
		toolBar.add(btnAbrir);
		toolBar.add(btnGuardar);
		toolBar.addSeparator();
		toolBar.add(btnReserved);
		toolBar.add(btnIdentifiers);
		toolBar.add(btnAnalizarTokens);
		toolBar.addSeparator();
		toolBar.add(btnCompilar);
		toolBar.addSeparator();
		toolBar.add(btnHtmlTokens);
		toolBar.add(btnHtml);

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	// ----area de los metodos que utilizara el programa--

	private void actualizarLineas() {
		int total = areaCodigo.getLineCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= total; i++)
			sb.append(i).append("\n");
		areaLineas.setText(sb.toString());
	}

	private void nuevoArchivo() {
		areaCodigo.setText("");
		areaConsola.setText("");
		setTitle("Nuevo archivo");
	}

	private void abrirArchivo() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccionar archivo para abrir");

		int seleccion = fileChooser.showDialog(this, "Abrir");

		if (seleccion == JFileChooser.APPROVE_OPTION) {

			File archivo = fileChooser.getSelectedFile();

			try {
				BufferedReader lector = new BufferedReader(new FileReader(archivo));
				String linea;
				String contenido = "";

				while ((linea = lector.readLine()) != null) {
					contenido += linea + "\n";
				}

				lector.close();

				areaCodigo.setText(contenido);
				areaConsola.append("Archivo abierto correctamente\n");

			} catch (IOException e) {
				areaConsola.append("Error al abrir archivo\n");
			}
		}
	}

	private void guardarArchivo() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Guardar archivo");

		int seleccion = fileChooser.showDialog(this, "Guardar");

		if (seleccion == JFileChooser.APPROVE_OPTION) {

			File archivo = fileChooser.getSelectedFile();

			try {
				FileWriter writer = new FileWriter(archivo);
				writer.write(areaCodigo.getText());
				writer.close();

				areaConsola.append("Archivo guardado correctamente\n");

			} catch (IOException e) {
				areaConsola.append("Error al guardar archivo\n");
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

			Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

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
				Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

				areaCodigo.getHighlighter().addHighlight(inicioLinea + inicioError,
						inicioLinea + inicioError + texto.length(), painter);
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

			if (linea.isEmpty())
				continue;

			for (char c : linea.toCharArray()) {
				if (c == '{')
					balanceLlaves++;
				if (c == '}')
					balanceLlaves--;
				if (c == '(')
					balanceParentesis++;
				if (c == ')')
					balanceParentesis--;
			}

			// --area de error y punto y coma--
			if (!linea.endsWith(";") && !linea.endsWith("{") && !linea.endsWith("}") && !linea.startsWith("if")
					&& !linea.startsWith("while") && !linea.startsWith("for")) {

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

		String[] palabrasReservadas = { "int", "float", "if", "else", "while", "for", "return" };

		// separar símbolos
		codigo = codigo.replace("(", " ( ");
		codigo = codigo.replace(")", " ) ");
		codigo = codigo.replace("{", " { ");
		codigo = codigo.replace("}", " } ");
		codigo = codigo.replace(";", " ; ");
		codigo = codigo.replace("+", " + ");
		codigo = codigo.replace("-", " - ");
		codigo = codigo.replace("*", " * ");
		codigo = codigo.replace("/", " / ");
		codigo = codigo.replace("=", " = ");
		codigo = codigo.replace("<", " < ");
		codigo = codigo.replace(">", " > ");

		String[] tokens = codigo.split("\\s+");

		for (String token : tokens) {

			if (esReservada(token, palabrasReservadas)) {
				if (modo.equals("TODO") || modo.equals("RESERVADAS")) {
					areaConsola.append(token + " → RESERVADA\n");
				}
			} else if (token.matches("[a-zA-Z][a-zA-Z0-9]*")) {
				if (modo.equals("TODO") || modo.equals("IDENTIFICADORES")) {
					areaConsola.append(token + " → IDENTIFICADOR\n");
				}
			} else if (token.matches("\\d+")) {
				if (modo.equals("TODO")) {
					areaConsola.append(token + " → NUMERO\n");
				}
			} else {
				if (modo.equals("TODO")) {
					areaConsola.append(token + " → SIMBOLO\n");
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
		for (String s : r)
			if (s.equals(t))
				return true;
		return false;
	}

	private ImageIcon icon(String path) {
		java.net.URL url = getClass().getResource(path);
		return url == null ? null
				: new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new IDE().setVisible(true));
	}

	private void analizarTokens() {
		java.util.List<Fila> filas = tokens.analizar(areaCodigo);

		areaConsola.setText("");
		for (Fila f : filas) {
			areaConsola.append(f.getDato() + " -> " + f.getTipo() + "\n");
		}

		// analizarTokens(areaCodigo.getText(), "TODO");
	}

	private void mostrarTokens() {
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
			if (c == '(')
				contador++;
			if (c == ')')
				contador--;
			if (contador < 0)
				return false;
		}
		return contador == 0;
	}

	private boolean llavesBalanceadas(String codigo) {
		int contador = 0;
		for (char c : codigo.toCharArray()) {
			if (c == '{')
				contador++;
			if (c == '}')
				contador--;
			if (contador < 0)
				return false;
		}
		return contador == 0;
	}

	private boolean faltanPuntoComa(String codigo) {
		String[] lineas = codigo.split("\n");

		for (String linea : lineas) {
			linea = linea.trim();

			if (linea.isEmpty())
				continue;
			if (linea.endsWith("{") || linea.endsWith("}"))
				continue;
			if (linea.startsWith("if") || linea.startsWith("while") || linea.startsWith("for"))
				continue;

			if (!linea.endsWith(";")) {
				return true;
			}
		}
		return false;
	}

	private void html() {
		ArchivoHTML archivoHTML = new ArchivoHTML();

		java.util.List<String> columnas = new ArrayList<>();
		columnas.add("NOMBRE1");
		columnas.add("NOMBRE2");
		columnas.add("NOMBRE3");

		java.util.List<Map<String, Object>> filas = new ArrayList<>();

		Map<String, Object> fila1 = new HashMap<>();
		fila1.put("NOMBRE1", "int");
		fila1.put("NOMBRE2", "PALABRA_RESERVADA");
		fila1.put("NOMBRE3", 1);

		Map<String, Object> fila2 = new HashMap<>();
		fila2.put("NOMBRE1", "x");
		fila2.put("NOMBRE2", "IDENTIFICADOR");
		fila2.put("NOMBRE3", 1);

		filas.add(fila1);
		filas.add(fila2);

		Map<String, Object> datos = new HashMap<>();

		datos.put("titulo", "Errores");
		datos.put("columnas", columnas);
		datos.put("filas", filas);

		archivoHTML.generar("errores", datos);
	}

	private void htmlTokens() {
		tokens.generarHTML(areaCodigo);
	}

	// UTILIDADES
	public ImageIcon icono(String path, int w, int h) {
		ImageIcon icon = new ImageIcon(getClass().getResource(path));
		Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	private void configurarAtajos() {

		// CTRL + S (Guardar)
		KeyStroke ctrlS = KeyStroke.getKeyStroke("control S");
		areaCodigo.getInputMap().put(ctrlS, "guardar");
		areaCodigo.getActionMap().put("guardar", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}
		});

		// CTRL + O (Abrir)
		KeyStroke ctrlO = KeyStroke.getKeyStroke("control O");
		areaCodigo.getInputMap().put(ctrlO, "abrir");
		areaCodigo.getActionMap().put("abrir", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirArchivo();
			}
		});

		// CTRL + N (Nuevo)
		KeyStroke ctrlN = KeyStroke.getKeyStroke("control N");
		areaCodigo.getInputMap().put(ctrlN, "nuevo");
		areaCodigo.getActionMap().put("nuevo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				areaCodigo.setText("");
				areaConsola.setText("");
			}
		});
	}
}