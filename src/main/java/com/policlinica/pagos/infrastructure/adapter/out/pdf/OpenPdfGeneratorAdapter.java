package com.policlinica.pagos.infrastructure.adapter.out.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.out.PdfGeneratorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Component
public class OpenPdfGeneratorAdapter implements PdfGeneratorPort {
    
    @Value("${pdf.storage.path:./boletas}")
    private String storagePath;
    
    @Override
    public byte[] generarBoleta(Pago pago, String detalleCita, String nombrePaciente, String dniPaciente) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("POLICLÍNICA - COMPROBANTE DE PAGO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("═══════════════════════════════════════════════════════════"));
            document.add(new Paragraph(" "));
            
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("DATOS DE LA TRANSACCIÓN", boldFont));
            document.add(new Paragraph("N° de Transacción: " + pago.getId()));
            document.add(new Paragraph("N° Transacción Externa: " + pago.getTransaccionExternaId()));
            document.add(new Paragraph("Código Autorización: " + pago.getCodigoAutorizacion()));
            document.add(new Paragraph("Fecha: " + pago.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            document.add(new Paragraph("Estado: " + pago.getEstado()));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("DATOS DEL PACIENTE", boldFont));
            document.add(new Paragraph("Nombre: " + nombrePaciente));
            document.add(new Paragraph("DNI: " + dniPaciente));
            document.add(new Paragraph("Email: " + pago.getEmailContacto()));
            document.add(new Paragraph("Teléfono: " + pago.getTelefonoContacto()));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("DETALLE DE LA CITA", boldFont));
            document.add(new Paragraph(detalleCita));
            document.add(new Paragraph(" "));
            
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            
            addCell(table, "Concepto", true);
            addCell(table, "Detalle", true);
            addCell(table, "Cita médica", false);
            addCell(table, detalleCita, false);
            addCell(table, "Monto base", false);
            addCell(table, "S/ " + String.format("%.2f", pago.getMonto()), false);
            
            if (pago.getDescuento() != null && pago.getDescuento() > 0) {
                addCell(table, "Descuento", false);
                addCell(table, "- S/ " + String.format("%.2f", pago.getDescuento()), false);
            }
            
            addCell(table, "Método de pago", false);
            String metodoStr = pago.getMetodo() + (pago.getUltimosDigitosTarjeta() != null ? 
                " (****" + pago.getUltimosDigitosTarjeta() + ")" : "");
            addCell(table, metodoStr, false);
            
            document.add(table);
            document.add(new Paragraph(" "));
            
            Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Paragraph total = new Paragraph("TOTAL PAGADO: S/ " + String.format("%.2f", pago.getTotalConDescuento()), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por su preferencia. Esta boleta es un comprobante de pago válido.", 
                new Font(Font.HELVETICA, 10, Font.ITALIC)));
            document.add(new Paragraph("Para consultas o reclamos, contáctenos al email: atencion@policlinica.com"));
            
            document.close();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
    
    private void addCell(PdfPTable table, String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        if (isHeader) {
            cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.setPadding(8);
        table.addCell(cell);
    }
    
    @Override
    public String guardarPDF(byte[] pdf, String idTransaccion) {
        try {
            File directory = new File(storagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = "boleta_" + idTransaccion + ".pdf";
            String filePath = storagePath + File.separator + fileName;
            
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdf);
            }
            
            return "http://localhost:8080/api/pagos/boleta/" + idTransaccion;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el PDF", e);
        }
    }
}