package com.bacontech.exploreitext7.helloworld;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.text.MessageFormat;

@RestController
@RequestMapping("/hello-world")
@RequiredArgsConstructor
public class HelloWorldController {

    // http://localhost:8091/hello-world

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public byte[] helloWorldPdf(HttpServletResponse response) throws FileNotFoundException {

        // Web Application would use ServletOutputStream, to the PdfWriter
        String dest = "hello-world.pdf";

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Hello World!"));
        document.close();

//        String filename = MessageFormat.format("hello-world.pdf");
        String filename = "hello-world.pdf";
        String contentDisposition = MessageFormat.format("attachment; filename={0}", filename);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return null;
    }
}
