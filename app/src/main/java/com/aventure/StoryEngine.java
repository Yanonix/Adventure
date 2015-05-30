package com.aventure;

import android.app.Activity;

import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class StoryEngine {

    class Choice {
        String text;
        String to;

        public Choice(String text, String to) {
            this.text = text;
            this.to = to;
        }

        @Override
        public String toString() {
            return "Choice{" +
                    "text='" + text + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    class Situation {
        String id;
        String text;
        ArrayList<Choice> choices;

        public Situation(String id, String text, ArrayList<Choice> choices) {
            this.id = id;
            this.text = text;
            this.choices = choices;
        }

        @Override
        public String toString() {
            return "Situation{" +
                    "id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", n(choices)=" + choices.size() +
                    '}';
        }
    }

    HashMap<String, Situation> situations;
    String state_id = "";


    public StoryEngine(InputStream is) {
        this.situations = new HashMap<>();
        try {
            Log.e("x","loading xml");

            DocumentBuilderFactory factory;
            DocumentBuilder builder;

            Document dom;
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();

            dom = builder.parse(is);

            NodeList situations_xml = dom.getElementsByTagName("situation");

            for (int i = 0; i < situations_xml.getLength(); i++) {
                Node situation = situations_xml.item(i);
                String id = situation.getAttributes().getNamedItem("id").getNodeValue();
                Log.e("x", "id:" + id);
                if(i == 0){
                    state_id = id;
                }
                NodeList childs = situation.getChildNodes();
                String text = "";
                ArrayList<Choice> choices = new ArrayList<>();
                for (int j = 0; j < childs.getLength(); j++) {
                    Node child = childs.item(j);
                    if(child.getNodeName().equals("text")){
                        text = child.getTextContent().trim();
                    }
                    if(child.getNodeName().equals("choices")){
                        NodeList choices_xml = child.getChildNodes();
                        for (int k = 0; k < choices_xml.getLength(); k++) {
                            Node choice_xml = choices_xml.item(k);
                            if(choice_xml.getNodeName().equals("choice")){
                                String choice_text = choice_xml.getTextContent().trim();
                                String choice_to = choice_xml.getAttributes().getNamedItem("to").getNodeValue();
                                choices.add(new Choice(choice_text, choice_to));
                            }
                        }
                    }
                }
                this.situations.put(id, new Situation(id, text, choices));
                Log.e("x", "text:" + text);
                Log.e("x","choices:"+choices.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }



    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return sw.toString();
    }

    public Situation getSituation(){
        return situations.get(this.state_id);
    }

    public void makeChoice(int i){
        this.state_id = getSituation().choices.get(i).to;
    }

}
