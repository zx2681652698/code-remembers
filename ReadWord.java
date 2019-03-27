package cn.bipt.chinact.commons;

import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileWriter;

public class ReadWord {
    public static void readWord() throws Exception {
        FileInputStream fis = new FileInputStream("F:\\zx备份\\职业素养\\课件\\第四讲 素养初体验 课件.doc");
        HWPFDocumentCore document = WordToHtmlUtils.loadDoc(fis);
        CustomConverter converter = new CustomConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        converter.processDocument(document);
        org.w3c.dom.Document doc = converter.getDocument();
        DOMSource domSource = new DOMSource( doc );
        FileWriter out = new FileWriter("F:\\zx备份\\职业素养\\课件\\test4.html");
        StreamResult streamResult = new StreamResult( out );
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
        serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
        serializer.setOutputProperty( OutputKeys.METHOD, "html" );
        serializer.transform( domSource, streamResult );
        out.close();
    }


}
