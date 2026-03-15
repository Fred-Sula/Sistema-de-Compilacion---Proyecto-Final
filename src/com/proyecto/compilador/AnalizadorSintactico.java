package com.proyecto.compilador;

import java.util.ArrayList;

public class AnalizadorSintactico {

    public ArrayList<String> errores = new ArrayList<>();

    public void analizar(String codigo){

        String[] lineas = codigo.split("\n");

        for(int i=0;i<lineas.length;i++){

            String linea = lineas[i].trim();

            if(linea.isEmpty()) continue;

            if(!linea.endsWith(";") && !linea.endsWith("{") && !linea.endsWith("}")){

                errores.add("Error en linea "+(i+1)+" falta ;");

            }

        }

    }

}