package eg.edu.alexu.csd.oop.draw.controller;

import eg.edu.alexu.csd.oop.draw.Shape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

public class Xml {
    private static Xml xml = null;
    private Xml()
    {
    }
    static synchronized Xml makeInstance()
    {
        if (xml == null)
        {
            return  xml = new Xml();
        }
        else
            return xml;
    }
    void save(String file, Shape[] shapes) throws TransformerException, ParserConfigurationException, FileNotFoundException {

        FileOutputStream outXml = new FileOutputStream(file + "\\XmlFile.xml");
        writeOnXml(file + "\\XmlFile.xml", shapes);
    }
    private void writeOnXml(String file, Shape[] shapes) throws TransformerException, ParserConfigurationException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();

        Document document = dbuilder.newDocument();

        Element Shapes = document.createElement("Shapes");
        document.appendChild(Shapes);
        for (Shape shape1 : shapes) {
            Element shape = document.createElement(shape1.getClass().toString().replaceAll("\\s+", ""));
            Shapes.appendChild(shape);

            Element position = document.createElement("position");
            shape.appendChild(position);
            Element x = document.createElement("x");
            Element y = document.createElement("y");
            x.appendChild(document.createTextNode(String.valueOf(shape1.getPosition().getX())));
            y.appendChild(document.createTextNode(String.valueOf(shape1.getPosition().getY())));
            position.appendChild(x);
            position.appendChild(y);

            Element fillColor = document.createElement("fillcolor");
            fillColor.appendChild(document.createTextNode(shape1.getFillColor().toString()));
            shape.appendChild(fillColor);

            Element borderColor = document.createElement("borderColor");
            borderColor.appendChild(document.createTextNode(shape1.getColor().toString()));
            shape.appendChild(borderColor);

            Element properties = document.createElement("properties");
            shape.appendChild(properties);

            for (Map.Entry<String, Double> entry : shape1.getProperties().entrySet()) {
                Element element = document.createElement(entry.getKey());
                element.appendChild(document.createTextNode(entry.getValue().toString()));
                properties.appendChild(element);
            }

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(new File(file));

        transformer.transform(source, result);

    }
}
