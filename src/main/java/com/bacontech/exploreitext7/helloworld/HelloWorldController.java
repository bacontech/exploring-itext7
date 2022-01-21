package com.bacontech.exploreitext7.helloworld;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

@RestController
@RequestMapping("/hello-world")
@RequiredArgsConstructor
public class HelloWorldController {

    private final TutorialDocumentCreator tutorialDocumentCreator;

    // http://localhost:8091/hello-world

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void helloWorldPdf(HttpServletResponse response) throws FileNotFoundException {

        // Web Application would use ServletOutputStream, to the PdfWriter
        String dest = "hello-world.pdf";
        PdfWriter writer = new PdfWriter(dest);
        tutorialDocumentCreator.createHelloWorldDocument(writer);

    }

    // http://localhost:8091/hello-world/download

    // produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void helloWorldPdfDownload(HttpServletResponse response) {
        String filename = "hello-world.pdf";
        // Adding this header tells the browser to download as an attachment
        String contentDisposition = MessageFormat.format("attachment; filename={0}", filename);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        // Removing "attachment" displays the pdf in the browser, but now I don't know how to change the name
//        String contentDisposition = MessageFormat.format("filename={0}", filename);

        try (ServletOutputStream out = response.getOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            tutorialDocumentCreator.createFillableForm(writer);

            response.flushBuffer();
        } catch (IOException ioException){
            System.out.println("IO Exception");
            ioException.printStackTrace();
        }
    }

}
