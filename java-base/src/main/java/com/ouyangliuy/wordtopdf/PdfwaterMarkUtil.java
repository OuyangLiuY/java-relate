package com.ouyangliuy.wordtopdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PdfwaterMarkUtil {

    private static int interval = -5;
    public static void wateMark(String file, String outputFile, String waterMarkName){
        try{

            InputStream inputStream = new FileInputStream(file);
            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(outputFile));
            BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 50);
                under.setRGBColorFill(145,145,145);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
//                for (int height = interval + textH; height < pageRect.getHeight();
//                     height = height + textH*3) {
//                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
//                         width = width + textW*2) {
//                        under.showTextAligned(Element.ALIGN_LEFT
//                                , waterMarkName, width - textW,
//                                height - textH, 40);
//                    }
//                }
                under.showTextAligned(Element.ALIGN_LEFT
                        , waterMarkName, 120,
                        500, 45);
                under.showTextAligned(Element.ALIGN_LEFT
                        , waterMarkName, 120,
                        300, 45);
                under.showTextAligned(Element.ALIGN_LEFT
                        , waterMarkName, 120,
                        100, 45);
                // 添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();


        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
         String pdfPath = "C:/Users/chopsticks/Desktop/xxx/dismiss-img-clear.pdf";
         String res = "C:/Users/chopsticks/Desktop/xxx/dismiss-img-result.pdf";
        wateMark(pdfPath,res,"仅供上汽总部离职业务");
    }

}
