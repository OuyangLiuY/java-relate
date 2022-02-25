package com.ouyangliuy.wordtopdf;


import com.aspose.pdf.*;


public class PdfWater {
     static  String pdfPath = "C:/Users/chopsticks/Desktop/xxx/dismiss-img-clear.pdf";
    public static void add(){
        // Initialize document object
        Document document = new Document(pdfPath);

//Add page
        Page page = document.getPages().add();

// Add text to new page
        page.getParagraphs().add(new TextFragment("Hello World!"));

// Save updated PDF
        document.save("C:/Users/chopsticks/Desktop/xxx/HelloWorld_out.pdf");
    }

    public static void main(String[] args) throws Exception{
//        add();
        addWater();
    }

    public static void  addWater(){
        boolean res = AsposeUtil.judgeLicense();
        System.out.println(res);
        String pdfPath = "C:/Users/chopsticks/Desktop/xxx/dismiss-img-clear.pdf";
        Document pdfDocument = new Document(pdfPath);
        //TODO 汉字不支持问题
        TextStamp textStamp = new TextStamp("仅供上汽总部离职业务");
        // set whether stamp is background
        textStamp.setBackground(true);
        // set origin
        textStamp.setXIndent(100);
        textStamp.setYIndent(100);
        // rotate stamp
        textStamp.setRotate(Rotation.None);
        // set text properties
        textStamp.getTextState().setFont(FontRepository.findFont("Arial"));
        textStamp.getTextState().setFontSize(14.0F);
        textStamp.getTextState().setFontStyle(FontStyles.Bold);
        textStamp.getTextState().setFontStyle(FontStyles.Italic);
        textStamp.getTextState().setForegroundColor(Color.getGray());
        // add stamp to particular page
        pdfDocument.getPages().get_Item(1).addStamp(textStamp);
        // save output document
        pdfDocument.save("C:/Users/chopsticks/Desktop/xxx/dismiss-img-result.pdf");
    }
}
