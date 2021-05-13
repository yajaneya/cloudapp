package yajaneya.server.service.impl.command;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ViewFilesInDirCommand implements CommandService {

    Document doc;

    private final String PATH_CLOUD = "D:" + File.separator + "cloudFiles";

    @Override
    public String processCommand(String command, ClientService clientService) {
        final int requirementCountCommandParts = 2;

        String[] actualCommandParts = command.split("\\n");
        if(actualCommandParts.length != requirementCountCommandParts) {
            throw new IllegalArgumentException("Команда \"" + getCommand() + "\" не корректна.");
        }

        return process(actualCommandParts[1]);
    }

    private String process(String dirPath) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builderdoc;
        try {
            builderdoc = factory.newDocumentBuilder();
            doc = builderdoc.newDocument();
            Element rootElement = doc.createElementNS("DIR_CLOUD", "DIR");
            doc.appendChild(rootElement);

            if (dirPath.equals(".")) dirPath = PATH_CLOUD;

            File directory = new File(dirPath);

            if(!directory.exists()) {
                return "Такого каталога нет";
            }

            addFilesToDir(directory, rootElement);



            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // для красивого вывода в консоль
            DOMSource source = new DOMSource(doc);

            //печатаем в консоль или файл
            StreamResult fileResult = new StreamResult(new File("dir_cloud.xml"));

            //записываем данные
            transformer.transform(source, fileResult);
            System.out.println("Создание XML файла закончено");

        } catch (Exception e) {
            e.printStackTrace();
        }




        if (dirPath.equals(".")) dirPath = PATH_CLOUD;

        File directory = new File(dirPath);

        if(!directory.exists()) {
            return "Такого каталога нет";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("CLOUDDIR:");
        for (File childFile : directory.listFiles()) {
            String typeFile = getTypeFile(childFile);
            builder.append(childFile.getName()).append(" | ").append(typeFile).append(System.lineSeparator());
        }

        return builder.toString();
    }

    private String getTypeFile(File childFile) {
        return childFile.isDirectory() ? "DIR" : "FILE";
    }

    private void addFilesToDir (File directory, Element element) {

        for (File childFile : directory.listFiles()) {
            if (getTypeFile(childFile).equals("DIR")) {
                Element dir = addElement(element, "DIR", childFile.getName());
                addFilesToDir(childFile, dir);
            } else {
                addElement(element, "File", childFile.getName());
            }
        }

    }

    private Element addElement (Element element, String typeElement, String nameElement) {
        Element file = doc.createElement(typeElement);
        file.setAttribute("name", nameElement);
        element.appendChild(file);

//        Element nameFile = doc.createElement("Name");
//        nameFile.setAttribute("name", nameElement);
//        file.appendChild(nameFile);

        return file;
    }

    @Override
    public String getCommand() {
        return "ls";
    }
}
