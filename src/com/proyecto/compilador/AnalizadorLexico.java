package com.proyecto.compilador;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {

    public ArrayList<String> errores = new ArrayList<>();

    public void analizar(String codigo){

        // detectar cadenas primero
        Pattern patronCadena = Pattern.compile("\"[^\"]*\"");
        Matcher matcher = patronCadena.matcher(codigo);

        while(matcher.find()){
            String cadena = matcher.group();
            System.out.println(cadena + " -> CADENA");
        }

        // eliminar cadenas para no romperlas al separar
        codigo = codigo.replaceAll("\"[^\"]*\"", " ");

        codigo=codigo.replace("("," ( ");
        codigo=codigo.replace(")"," ) ");
        codigo=codigo.replace("{"," { ");
        codigo=codigo.replace("}"," } ");
        codigo=codigo.replace(";"," ; ");
        codigo=codigo.replace("+"," + ");
        codigo=codigo.replace("-"," - ");
        codigo=codigo.replace("*"," * ");
        codigo=codigo.replace("/"," / ");
        codigo=codigo.replace("="," = ");
        codigo=codigo.replace("<"," < ");
        codigo=codigo.replace(">"," > ");

        String[] tokens = codigo.split("\\s+");

        for(String token : tokens){

            if(token.trim().isEmpty()) continue;

            if(token.matches("[a-zA-Z][a-zA-Z0-9]*")){
                System.out.println(token+" -> IDENTIFICADOR");
            }

            else if(token.matches("\\d+")){
                System.out.println(token+" -> NUMERO");
            }

            else if (token.matches("[+\\-*/=<>]")){
                System.out.println(token+" -> OPERADOR");
            }

            else if(token.matches("[;(){}]")){
                System.out.println(token+" -> SIMBOLO");
            }

            else{
                errores.add("Token inválido: "+token);
            }

        }

    }

}