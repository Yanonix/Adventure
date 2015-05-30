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




    public class Text {
        public String text;
        public int wait = 0;

        public Text(String text, int wait) {
            this.text = text;
            this.wait = wait;
        }
    }

    class Choice {
        public String text;
        public String to;
        public String summary;

        public Choice(String text, String to, String summary) {
            this.text = text;
            this.to = to;
            this.summary = summary;
        }

        @Override
        public String toString() {
            return "Choice{" +
                    "text='" + text + '\'' +
                    ", to='" + to + '\'' +
                    ", summary='" + summary + '\'' +
                    '}';
        }
    }

    class Situation {
        public String id;
        public ArrayList<Text> texts;
        public ArrayList<Choice> choices;

        public Situation(String id, ArrayList<Text> texts, ArrayList<Choice> choices) {
            this.id = id;
            this.texts = texts;
            this.choices = choices;
        }

        @Override
        public String toString() {
            return "Situation{" +
                    "id='" + id + '\'' +
                    ", n(text)='" + texts.size() + '\'' +
                    ", n(choices)=" + choices.size() +
                    '}';
        }

        public String text() {
            StringBuilder out = new StringBuilder();
            for (Text t : texts)
            {
                out.append(t.text);
            }
            return out.toString();
        }
    }

    private HashMap<String, Situation> situations;
    private String state_id = "";
    private String state_old = "";


    public StoryEngine(InputStream is) {
        this.situations = new HashMap<>();
        try {
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
                if(i == 0){
                    state_id = id;
                    state_old = state_id;
                }
                NodeList childs = situation.getChildNodes();
                ArrayList<Text> texts = new ArrayList<>();
                ArrayList<Choice> choices = new ArrayList<>();
                for (int j = 0; j < childs.getLength(); j++) {
                    Node child = childs.item(j);
                    if(child.getNodeName().equals("text")){
                        int wait = 0;
                        if(child.getAttributes().getNamedItem("wait") != null) {
                            wait = Integer.parseInt(child.getAttributes().getNamedItem("wait")
                                    .getNodeValue());
                        }
                        texts.add(new Text(child.getTextContent().trim(),wait));
                    }
                    if(child.getNodeName().equals("choices")){
                        NodeList choices_xml = child.getChildNodes();
                        for (int k = 0; k < choices_xml.getLength(); k++) {
                            Node choice_xml = choices_xml.item(k);
                            if(choice_xml.getNodeName().equals("choice")){
                                String choice_text = choice_xml.getTextContent().trim();
                                String choice_to = choice_xml.getAttributes().getNamedItem("to").getNodeValue();

                                if(choice_xml.getAttributes().getNamedItem("summary") != null) {
                                    String choice_summary = choice_xml.getAttributes().getNamedItem("summary").getNodeValue();
                                    choices.add(new Choice(choice_text, choice_to, choice_summary));
                                }
                                else
                                {
                                    choices.add(new Choice(choice_text, choice_to, choice_text));
                                }
                            }
                        }
                    }
                }
                this.situations.put(id, new Situation(id, texts, choices));
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    public boolean makeChoice(int i) {
        if(getSituation().choices.size() > i) {
            String next_state = getSituation().choices.get(i).to;
            if (situations.containsKey(next_state)) {
                state_old = state_id;
                this.state_id = next_state;
                return true;
            }
        }

        return false;
    }

    public void undo() {
        state_id = state_old;
    }

}
