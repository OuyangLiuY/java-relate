package com.ouyangliuy.wordtopdf;

import com.aspose.words.*;
import com.aspose.words.Shape;


import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class AsposeUtil {
    //校验license
    public static boolean judgeLicense() {
        boolean result = false;
        try {
            // 凭证
            String license =
                    "<License>\n" +
                            "  <Data>\n" +
                            "    <Products>\n" +
                            "      <Product>Aspose.Total for Java</Product>\n" +
                            "      <Product>Aspose.Words for Java</Product>\n" +
                            "    </Products>\n" +
                            "    <EditionType>Enterprise</EditionType>\n" +
                            "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                            "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                            "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                            "  </Data>\n" +
                            "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                            "</License>";
            InputStream is = new ByteArrayInputStream(license.getBytes(StandardCharsets.UTF_8));
            License asposeLic = new License();
            asposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void wordofpdf(String wordPath, String filePath) {
        FileInputStream os = null;
        Document doc;
        try {
            boolean res = judgeLicense();
            if(!res)
                return;
            os = new FileInputStream(wordPath);
            doc = new Document(os);
            doc.save(filePath, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为word文档添加水印
     * @param doc word文档模型
     * @param watermarkText 需要添加的水印字段
     * @throws Exception
     */
    public static void insertWatermarkText(Document doc, String watermarkText) throws Exception {
        Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        //水印内容
        watermark.getTextPath().setText(watermarkText);
        //水印字体
        watermark.getTextPath().setFontFamily("宋体");
        //水印宽度
        watermark.setWidth(200);
        //水印高度
        watermark.setHeight(50);
        //旋转水印
        watermark.setRotation(-40);
        //水印颜色 浅灰色
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);
        //设置相对水平位置
        watermark.setLeft(500);
        //设置相对垂直位置
        watermark.setTop(500);
        //设置包装类型
        watermark.setWrapType(WrapType.TOP_BOTTOM);
        //设置垂直对齐
//        watermark.setVerticalAlignment(VerticalAlignment.CENTER);
//        //设置文本水平对齐方式
//        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);
        for (Section sect : doc.getSections())
        {
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
        System.out.println("Watermark Set");
    }

    /**
     * 在页眉中插入水印
     * @param watermarkPara
     * @param sect
     * @param headerType
     * @throws Exception
     */
    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType) throws Exception{
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
        if (header == null)
        {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }
        header.appendChild(watermarkPara.deepClone(true));
    }

    /**
     * 设置水印属性
     * @param doc
     * @param wmText
     * @param left
     * @param top
     * @return
     * @throws Exception
     */
    public static Shape ShapeMore(Document doc, String wmText, double left, double top)throws Exception{
        Shape waterShape = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        //水印内容
        waterShape.getTextPath().setText(wmText);
        //水印字体
        waterShape.getTextPath().setFontFamily("宋体");
        waterShape.setWidth(200);
        //水印高度
        waterShape.setHeight(50);
        //旋转水印
        waterShape.setRotation(-40);
        //水印颜色 浅灰色
        waterShape.getFill().setColor(Color.lightGray);
        waterShape.setStrokeColor(Color.lightGray);
        //将水印放置在页面中心
        waterShape.setLeft(left);
        waterShape.setTop(top);
        //设置包装类型
        waterShape.setWrapType(WrapType.NONE);
        return waterShape;
    }


    /**
     * 插入多个水印
     * @param mdoc
     * @param wmText
     * @throws Exception
     */
    public static void waterToMore(Document mdoc, String wmText)throws Exception{
        Paragraph watermarkPara = new Paragraph(mdoc);
        for (int j = 0; j < 500; j = j + 200)
        {
            for (int i = 0; i < 700; i = i + 150)
            {
                System.out.println(" i= " + i+ ",j= " + j);
                if(i == 300){
                    Shape waterShape = ShapeMore(mdoc, wmText, j, i);
                    watermarkPara.appendChild(waterShape);
                }
            }
        }
        for (Section sect : mdoc.getSections())
        {
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
    }



    public static void main(String[] args) throws Exception {
//        String wordPath = "C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img.docx";
//        String pdfPath = "C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img.pdf";
//        wordofpdf(wordPath,pdfPath);
//        FileInputStream os = null;
//        Document doc;
//        os = new FileInputStream(wordPath);
//        doc = new Document(os);
//        insertWatermarkText(doc,"这是一个测试");
//        doc.save(wordPath,);

        try {
            boolean res = judgeLicense();
            if(!res)
                return;
            Document document = new Document("C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img.docx");
            waterToMore(document,"仅供上汽总部离职业务");
            //文件输出路径
            document.save("C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img-word.pdf",SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
