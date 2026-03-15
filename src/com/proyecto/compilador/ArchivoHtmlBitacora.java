package com.proyecto.compilador;

import java.io.File;
import java.io.FileWriter;
import java.awt.Desktop;
import java.util.List;

public class ArchivoHtmlBitacora {

    public static void generar(List<String> erroresLexicos, List<String> erroresSintacticos) {

        try {

            File archivo = new File("bitacora_errores.html");
            FileWriter writer = new FileWriter(archivo);

            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Bitacora de Errores</title>");
            writer.write("<style>");
            writer.write("body{font-family:Arial;background:#f4f4f4;padding:20px;}");
            writer.write("table{border-collapse:collapse;width:100%;}");
            writer.write("th,td{border:1px solid black;padding:8px;text-align:left;}");
            writer.write("th{background:#333;color:white;}");
            writer.write(".lexico{color:red;}");
            writer.write(".sintactico{color:blue;}");
            writer.write("</style>");
            writer.write("</head>");

            writer.write("<body>");
            writer.write("<h1>Bitácora de Errores del Compilador</h1>");

            writer.write("<table>");
            writer.write("<tr><th>Tipo</th><th>Error</th></tr>");

            for (String error : erroresLexicos) {
                writer.write("<tr><td class='lexico'>Léxico</td><td>" + error + "</td></tr>");
            }

            for (String error : erroresSintacticos) {
                writer.write("<tr><td class='sintactico'>Sintáctico</td><td>" + error + "</td></tr>");
            }

            writer.write("</table>");
            writer.write("</body>");
            writer.write("</html>");

            writer.close();

            Desktop.getDesktop().browse(archivo.toURI());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}