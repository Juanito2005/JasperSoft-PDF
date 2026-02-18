package dev.juanito;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ReportService {

    private final String path = "src/main/resources/informe_pedidos.jrxml";
    
    public JasperReport readAndCompileFile() {
        JasperReport compiledReport = null;

        try {
            compiledReport = JasperCompileManager.compileReport(path);
            System.out.println("Archivo compilado con exito mi rey");

        } catch (JRException e) {
            System.out.println("Error al compilar el archivo JRXML: " + e.getMessage());
            e.printStackTrace();
        }
        return compiledReport;
    }

    public JasperPrint fillFile(JasperReport jasperReport, Map<String, Object> parameters) {
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ConnectionJDBC.connection());
            System.out.println("Archivo llenado con éxito");
        } catch (JRException e) {
            System.out.println("Error al llenar el archivo JRXML; " + e.getMessage());
            e.printStackTrace();
        }
        return jasperPrint;
    }

    public void exportToPdf(JasperPrint jasperPrint, String nombreArchivo) {
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, nombreArchivo);
            System.out.println("¡PDF generado correctamente en: " + nombreArchivo + "!");
        } catch (JRException e) {
            System.err.println("Error al exportar a PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
