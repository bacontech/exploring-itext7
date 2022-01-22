package com.bacontech.exploreitext7.helloworld;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
        String dest = "base-applicatoin.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        tutorialDocumentCreator.createFillableAppForm(document);

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
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            tutorialDocumentCreator.createFillableAppForm(doc);

            response.flushBuffer();
        } catch (IOException ioException){
            System.out.println("IO Exception");
            ioException.printStackTrace();
        }
    }

    // http://localhost:8091/hello-world/fill-out-application
    @GetMapping(value = "/fill-out-application", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void fillOutApplication(HttpServletResponse response) {
        String filename = "filled-out-app.pdf";
        // Adding this header tells the browser to download as an attachment
        String contentDisposition = MessageFormat.format("attachment; filename={0}", filename);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);


        try (ServletOutputStream out = response.getOutputStream()) {

            // Expects the base application to live in the directory
            PdfDocument pdf = new PdfDocument(new PdfReader("base-application.pdf"), new PdfWriter(out));

//            Document doc = new Document(pdf);
            tutorialDocumentCreator.fillOutApplication(pdf);

            response.flushBuffer();
        } catch (IOException ioException){
            System.out.println("IO Exception");
            ioException.printStackTrace();
        }
    }


}
