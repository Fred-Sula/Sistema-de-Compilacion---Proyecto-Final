package com.proyecto.compilador.utilidad;

public class Fila {

	String dato;
    String tipo;
    int linea;
	
    public Fila(String dato, String tipo, int linea) {
		super();
		this.dato = dato;
		this.tipo = tipo;
		this.linea = linea;
	}

	public String getDato() {
		return dato;
	}
	
	public String getTipo() {
		return tipo;
	}

	public int getLinea() {
		return linea;
	}   
}
