package cn.bipt.chinact.commons;

import org.apache.poi.hwpf.converter.HtmlDocumentFacade;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.misc.BASE64Encoder;

public class CustomConverter extends WordToHtmlConverter {
    public CustomConverter(Document document) {
        super(document);
    }

    public CustomConverter(HtmlDocumentFacade htmlDocumentFacade) {
        super(htmlDocumentFacade);
    }

    @Override
    protected void processImage(Element currentBlock, boolean inlined, Picture picture) {
        Element img = super.getDocument().createElement("img");
        img.setAttribute("src", "data:image/png;base64,"+ new BASE64Encoder().encode(picture.getContent()));
        currentBlock.appendChild(img);
    }
}
