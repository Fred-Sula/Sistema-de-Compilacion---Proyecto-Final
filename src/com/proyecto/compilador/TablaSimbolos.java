package com.proyecto.compilador;

import java.io.FileWriter;
import java.io.File;
import java.awt.Desktop;
import java.util.*;

public class TablaSimbolos {

    public void generarHTML(List<Map<String, Object>> simbolos) {

        try {

            FileWriter writer = new FileWriter("tabla_simbolos.html");

            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Tabla de Simbolos</title>");
            writer.write("<style>");
            writer.write("table{border-collapse:collapse;font-family:Arial;}");
            writer.write("th,td{border:1px solid black;padding:8px;text-align:center;}");
            writer.write("th{background-color:#dddddd;}");
            writer.write("</style>");
            writer.write("</head>");
            writer.write("<body>");

            writer.write("<h2>TABLA DE SIMBOLOS</h2>");

            writer.write("<table>");
            writer.write("<tr>");
            writer.write("<th>IDENTIFICADOR</th>");
            writer.write("<th>TIPO</th>");
            writer.write("<th>LINEA</th>");
            writer.write("</tr>");

            for (Map<String, Object> fila : simbolos) {

                writer.write("<tr>");

                writer.write("<td>" + fila.get("IDENTIFICADOR") + "</td>");
                writer.write("<td>" + fila.get("TIPO") + "</td>");
                writer.write("<td>" + fila.get("LINEA") + "</td>");

                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</body>");
            writer.write("</html>");

            writer.close();

            File archivo = new File("tabla_simbolos.html");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}