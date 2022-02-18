package com.ouyangliuy.wordtopdf;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AsposeUtil {
    //校验license
    private static boolean judgeLicense() {
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

    public static void main(String[] args) {
        String wordPath = "C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img.docx";
        String pdfPath = "C:\\Users\\chopsticks\\Desktop\\xxx\\dismiss-img.pdf";
        wordofpdf(wordPath,pdfPath);
    }
}
