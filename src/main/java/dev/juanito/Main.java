package dev.juanito;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class Main {
    public static void main(String[] args) {
        ReportService reportService = new ReportService();

        JasperReport report = reportService.readAndCompileFile();

        if (report != null) {
            for (int i = 1; i <= 3; i++) {
                Map<String, Object> parametros = new HashMap<>();
                
                if (i == 1) {

                    parametros.put("pClienteId", 1);
                    parametros.put("pFechaInicio", Date.valueOf("2000-01-01"));
                    parametros.put("pFechaFin", Date.valueOf("2099-12-31"));
                } else if (i == 2) {

                    parametros.put("pClienteId", 3);
                    parametros.put("pFechaInicio", Date.valueOf("2000-01-01"));
                    parametros.put("pFechaFin", Date.valueOf("2026-12-31"));
                } else {

                    parametros.put("pClienteId", 4);
                    parametros.put("pFechaInicio", Date.valueOf("2000-01-01"));
                    parametros.put("pFechaFin", Date.valueOf("2026-12-31"));
                }
                
                // Rellenar y exportar con un nombre diferente cada vez
                JasperPrint impresion = reportService.fillFile(report, parametros);

                if (impresion != null) {
                    reportService.exportToPdf(impresion, "pedidos_reporte_" + i + ".pdf");
                }
            }
        }
    }
}