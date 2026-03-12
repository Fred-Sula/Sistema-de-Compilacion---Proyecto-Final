package com.proyecto.compilador;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ArchivoHTML {

	public void generar(String nombreArchivo, Map<String, Object> datos) {
		try {
			File carpetaBase = new File("html");
			carpetaBase.mkdirs();
			
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
	        cfg.setClassForTemplateLoading(ArchivoHTML.class, "/templates");
	        
	        Template plantilla = cfg.getTemplate("archivo.ftl");

	        StringBuilder sbNombre = new StringBuilder();
	        sbNombre.append(nombreArchivo);
	        sbNombre.append("_");
	        sbNombre.append(System.currentTimeMillis());
	        sbNombre.append(".html");
	        
	        File archivo = new File(carpetaBase, sbNombre.toString());
	        FileWriter fw = new FileWriter(archivo);

	        plantilla.process(datos, fw);

	        fw.close();
	        
	        System.out.println("Archivo HTML generado exitosamente.");
	        
	        if (Desktop.isDesktopSupported()) {
	            Desktop.getDesktop().browse(archivo.toURI());
	        }
		} catch (Exception e) {
			System.out.println("Error al generar el archivo HTML. \n" + e);
		}
	}
}
