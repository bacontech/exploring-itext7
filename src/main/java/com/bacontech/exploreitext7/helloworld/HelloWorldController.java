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

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void helloWorldPdfDownload(HttpServletResponse response) {
        try (ServletOutputStream out = response.getOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            tutorialDocumentCreator.createHelloWorldDocument(writer);

            response.flushBuffer();
        } catch (IOException ioException){
            System.out.println("IO Exception");
            ioException.printStackTrace();
        }
    }
//
//
//    @RequestMapping(value="/displayProcessFile/{processInstanceId}", method=RequestMethod.GET)
//    public ResponseEntity<byte[]> displayProcessFile(@PathVariable String processInstanceId) throws UnauthorizedUserAccessException{
//        Document document = new Document(pdf);
//        document.add(new Paragraph("Hello World!"));
//        document.close();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("application/pdf"));
//        headers.add("content-disposition", "attachment;filename=" + processFile.getDocName());
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(processFile.getContent(), headers, HttpStatus.OK);
//        return response;
//    }
}
