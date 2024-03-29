package util;

import lmf.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.applet.resources.MsgAppletViewer;

import java.util.*;

public class XmlParse {

    public static void parseXML(String inputPath,
                                Map<String, Component> componentList,
                                Map<String, Data> sharedDataMap,
                                Map<String, List<String>>  channelDataMap,
                                List<Channel> channelList) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(inputPath);
            Element root = document.getRootElement();
            getXML(root, componentList, sharedDataMap, channelDataMap, channelList);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


//        for (String key : componentList.keySet()) {
//            System.out.println(componentList.get(key).getName());
//            for (String dataKey : componentList.get(key).getDataMap().keySet()) {
//                System.out.println(dataKey + " " + componentList.get(key).getDataMap().get(dataKey).getValue());
//            }
//        }
    }


    private static void getXML(Element root,
                               Map<String, Component> componentList,
                               Map<String, Data> sharedDataMap,
                               Map<String, List<String>> channelDataMap,
                               List<Channel> channelList) {
        Iterator it = root.elementIterator();
        Element component = root;
        List<Attribute> componentAttrs = component.attributes();

        if (component.getName().equals("component")) {
            Component newComponent = new Component();
            for (Attribute attr : componentAttrs) {
//				System.out.print("属性名: " + attr.getName() + "   属性值: "
//						+ attr.getValue() + "\n");
                newComponent.setAttr(attr.getName(), attr.getValue());
            }
            componentList.put(newComponent.getAttr("id"), newComponent);
        } else if (component.getName().equals("communicationchannel")) {
            Channel newChannel = new Channel();
            for (Attribute attr : componentAttrs) {
                newChannel.setAttr(attr.getName(), attr.getValue());
            }
            channelList.add(newChannel);

        } else if (component.getName().equals("linkpoint")) {
            String componentId = root.getParent().attribute("id").getValue();
            Linkpoint newLinkpoint = new Linkpoint();
            Data newData = new Data();
            for (Attribute attr : componentAttrs) {
                newLinkpoint.setAttr(attr.getName(), attr.getValue());
                newData.setAttr(attr.getName(), attr.getValue());
            }
            newData.setAttr("ischannel", "true");

            componentList.get(componentId).getLinkpointMap().put(newLinkpoint.getAttr("id"), newLinkpoint);

            /**
             * linkpoint发送或接收的数据，放入channelDataMap中，对于一个数据，存储所有它连接到的component id
             */
            newData.setValue("null");
            if (channelDataMap.get(newData.getAttr("name")) == null) {
                List<String> componentIdList = new ArrayList<>();
                componentIdList.add(componentId);
                channelDataMap.put(newData.getAttr("name"), componentIdList);
            } else {
                List<String> componentIdList = channelDataMap.get(newData.getAttr("name"));
                componentIdList.add(componentId);
                channelDataMap.put(newData.getAttr("name"), componentIdList);
            }
            componentList.get(componentId).getDataMap().put(newData.getAttr("name"), newData);
//            System.out.println(newData.getAttr("name") + " " + componentList.get(componentId).getName() + " " + newData.getAttr("shared") +
//                    " " + componentList.get(componentId).getDataMap().get(newData.getAttr("name")));
        } else if (component.getName().equals("transition")) {
            String componentId = null;
            Element rootParent = root;
            Transition newTransition = new Transition();
            for (Attribute attr : componentAttrs) {
                newTransition.setAttr(attr.getName(), attr.getValue());
            }
            if (rootParent.getParent().getName().equals("state")) {
                rootParent = rootParent.getParent();
            }
            if (rootParent.getParent().getName().equals("component")) {
                rootParent = rootParent.getParent();
                componentId = rootParent.attribute("id").getValue();
            }

            List<Transition> transitionList = componentList.get(componentId)
                    .getTransitionMap().get(newTransition.getAttr("source"));
            if (transitionList == null) {
                transitionList = new ArrayList<>();
            }
            if (newTransition.getAttr("order") != null
                    && transitionList.size() > Integer.valueOf(newTransition.getAttr("order"))-1) {
//                System.out.println(newTransition.getAttr("order") + " " + newTransition.getAttr("source") + " " +
//                        newTransition.getAttr("dest"));
                transitionList.add(Integer.valueOf(newTransition.getAttr("order"))-1, newTransition);
            } else {
                transitionList.add(newTransition);
            }
            componentList.get(componentId).getTransitionMap()
                    .put(newTransition.getAttr("source"), transitionList);
        } else if (component.getName().equals("state")) {
            String componentId;
            State newState = new State();
            for (Attribute attr : componentAttrs) {
                newState.setAttr(attr.getName(), attr.getValue());
            }
            if (root.getParent().getName().equals("component"))  {
                componentId = root.getParent().attribute("id").getValue();
                componentList.get(componentId).getStateMap().put(newState.getAttr("id"), newState);
            }
            else {
                componentId = root.getParent().getParent().attribute("id").getValue();
                String stateId = root.getParent().attribute("id").getValue();
                State parentState = componentList.get(componentId).getStateMap().get(stateId);
                parentState.getSubStateList().put(newState.getAttr("id"), newState);
            }
        } else if (component.getName().equals("data")) {
            String componentId;
            Data newData = new Data();
            for (Attribute attr : componentAttrs) {
                newData.setAttr(attr.getName(), attr.getValue());
            }
            componentId = root.getParent().attribute("id").getValue();
            componentList.get(componentId).getDataMap().put(newData.getAttr("name"), newData);
            if (newData.getAttr("initvalue")!= null) {
                newData.setValue(newData.getAttr("initvalue"));
            }
            if (newData.getAttr("shared") != null) {
                sharedDataMap.put(newData.getAttr("name"), newData);
            }

        }

        Iterator itt = component.elementIterator();

        while (itt.hasNext()) {
            Element componentChild = (Element) itt.next();
            getXML(componentChild, componentList, sharedDataMap, channelDataMap, channelList);
        }
    }

//    public static void main(String[] args) {
//        Map<String, Component> componentList = new HashMap<>();
//        Map<String, Data> sharedDataMap = new HashMap<>();
//        List<Channel> channelList = new ArrayList<>();
//        Map<String, List<String>> channelDataMap = new HashMap<>();
//        XmlParse.parseXML("simulink0822.xml", componentList, sharedDataMap, channelDataMap, channelList);
//
//        for (Channel channel : channelList) {
//            System.out.println(channel.getAttr("id") + " " + channel.getAttr("source"));
//        }
//
//        for (String key : sharedDataMap.keySet()) {
//            System.out.println(sharedDataMap.get(key).getAttr("name") + " " +
//                    sharedDataMap.get(key).getAttr("datatype"));
//        }
//
//        for (String key : componentList.keySet()) {
//            for (String trasitionKey : componentList.get(key).getTransitionMap().keySet()) {
//                if (componentList.get(key).getTransitionMap().get(trasitionKey) != null) {
//                    System.out.println(trasitionKey);
//                    for (Transition transition : componentList.get(key).getTransitionMap().get(trasitionKey)) {
//                        System.out.println(transition.getAttr("source") + " " + transition.getAttr("dest"));
//                    }
//                }
//            }
//        }
//    }
}
