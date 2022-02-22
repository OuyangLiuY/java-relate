package com.ouyangliuy.wordtopdf;


import com.aspose.pdf.*;

import java.io.InputStream;

public class PdfWater {

    public static void main(String[] args) throws Exception{
        boolean res = AsposeUtil.judgeLicense();
        System.out.println(res);
        String pdfPath = "C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img-w.pdf";
        Document pdfDocument = new Document(pdfPath);

        TextStamp textStamp = new TextStamp("合同水印");
        textStamp.setBackground(true);
        textStamp.setXIndent(200);
        textStamp.setYIndent(100);
        textStamp.setRotate(Rotation.None);
        textStamp.getTextState().setFont( FontRepository.findFont("Arial"));
        textStamp.getTextState().setFontSize(20.0F);
        textStamp.getTextState().setFontStyle(FontStyles.Regular);
        textStamp.getTextState().setFontStyle(FontStyles.Italic);
        textStamp.getTextState().setForegroundColor(Color.getGray());


        PageCollection pages = pdfDocument.getPages();
        System.out.println(pages.size());
        int size = pages.size();
        for (int i = 0; i < size; i++) {
            pages.get_Item(i + 1).addStamp(textStamp);
//            pages.get_Item(i + 1).addStamp(textStamp1);
//            pages.get_Item(i + 1).addStamp(textStamp2);
        }

  //      pages.get_Item(size-1).addStamp(textStamp);

        pdfDocument.save("C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img-word.pdf");

    }
}
