package com.proyecto.compilador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import com.proyecto.compilador.utilidad.Fila;

public class Tokens {
	
	private static final Pattern TOKEN_PATRON = Pattern.compile(
			"\\+\\+|--|&&|\\|\\||==|!=|<=|>=" +
			"|[+\\-*/=<>]" +
			"|[(){};]" +
			"|\"[^\"]*\"" +
			"|-?\\d+(\\.\\d+)?" +
			"|[a-zA-Z][a-zA-Z0-9]*"
	);
	
	private static final Pattern IDENTIFICADOR = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");

	private static final Pattern NUMERO = Pattern.compile("-?\\d+(\\.\\d+)?");

	private static final Set<String> PALABRAS_RESERVADAS = Set.of(
			"entero", "flotante", "doble", "largo",
			"caracter", "constante",
			"si", "sino", "para", "mientras", 
			"vacio", "mostrar", "retornar"
	);
	
	private static final Set<String> OPERADOR_ARITMETICO = Set.of(
			"++", "--", "+", "-", "*", "/"		
	);
	
	private static final Set<String> OPERADOR_LOGICO = Set.of(
			"&&", "||"	
	);
	
	private static final Set<String> OPERADOR_RELACIONAL = Set.of(
			"==", "!=", "<=", ">=", "<", ">"
	);
	
	private static final Set<String> OPERADOR_ASIGNACION = Set.of(
			"="		
	);
	
	private static final Set<String> SIMBOLOS = Set.of(
			"!", "@", "#", "$", "%", "&", "(", ")", "{", "}", "\"", "\'", ";"		
	);
	
	public List<Fila> analizar(JTextArea jtaCodigo) {
	    List<Fila> filas = new ArrayList<>();

	    String[] lineas = jtaCodigo.getText().split("\n");

	    for (int i = 0; i < lineas.length; i++) {
	        Matcher matcher = TOKEN_PATRON.matcher(lineas[i]);

	        while (matcher.find()) {
	            String token = matcher.group();
	            filas.add(new Fila(token, tipoToken(token), i + 1));
	        }
	    }
	    
	    return filas;
	}
	
	public void generarHTML(JTextArea jtaCodigo) {
		ArchivoHTML archivoHTML = new ArchivoHTML();

	    List<String> columnas = List.of("Token", "Tipo", "Línea");
	    List<Fila> filas = new ArrayList<>();

	    String[] lineas = jtaCodigo.getText().split("\n");

	    for (int i = 0; i < lineas.length; i++) {
	        Matcher matcher = TOKEN_PATRON.matcher(lineas[i]);

	        while (matcher.find()) {
	            String token = matcher.group();
	            filas.add(new Fila(token, tipoToken(token), i + 1));
	        }
	    }

	    Map<String, Object> datos = new HashMap<>();
	    datos.put("titulo", "Bitácora de Tokens");
	    datos.put("columnas", columnas);
	    datos.put("filas", filas);

	    archivoHTML.generar("bitacora_tokens", datos);
	}
	
	private String tipoToken(String token) {		
	    if(PALABRAS_RESERVADAS.contains(token))
	        return "PALABRA RESERVADA";
	    
	    if(OPERADOR_ARITMETICO.contains(token))
	    	return "OPERADOR ARITMETICO";
	    
	    if(OPERADOR_LOGICO.contains(token))
	    	return "OPERADOR LOGICO";
	    
	    if(OPERADOR_RELACIONAL.contains(token))
	    	return "OPERADOR RELACIONAL";
	    
	    if(OPERADOR_ASIGNACION.contains(token))
	    	return "OPERADOR ASIGNACION";
	    
	    if(NUMERO.matcher(token).matches())
	        return "NUMERO";

	    if(IDENTIFICADOR.matcher(token).matches())
	        return "IDENTIFICADOR";

	    if(SIMBOLOS.contains(token))
	        return "SIMBOLO";

	    return "SIMBOLO NO VALIDO";
	}
}
