package com.example.jobs.maven_exemple;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CVExtractor {
    public static String extractText(byte[] cvBytes) {
        if (cvBytes == null) return "";
        try (PDDocument doc = PDDocument.load(cvBytes)) {
            return new PDFTextStripper().getText(doc);
        } catch (Exception e) {
            return "";
        }
    }
}
