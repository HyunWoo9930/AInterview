package com.example.ainterview.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;

@Service
public class PdfService {

    public Path convertTextToPdf(Path textFilePath) throws IOException {
        Path pdfFilePath = Path.of("interview_scripts", textFilePath.getFileName().toString().replace(".txt", ".pdf"));

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFilePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        try (PdfWriter writer = new PdfWriter(pdfFilePath.toFile());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph(content.toString()));
        }

        return pdfFilePath;
    }
}
