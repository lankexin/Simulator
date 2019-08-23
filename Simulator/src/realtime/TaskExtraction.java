package realtime;

import lmf.*;
import util.XmlParse;

import java.util.*;

public class TaskExtraction {

    public Map<String, Task> taskExtraction(Map<String, Component> componentMap, List<Channel> channelList) {
        Map<String, Task> taskMap = new LinkedHashMap<>();
        int newTaskId = 0;

        for (String key : componentMap.keySet()) {
            Component currentComponent = componentMap.get(key);
            if (currentComponent.getStateMap().isEmpty()) continue;
            for (String stateKey : currentComponent.getStateMap().keySet()) {
                if (currentComponent.getStateMap().get(stateKey).getAttr("andstate") != null) {
                    Task newTask = new Task(currentComponent,
                            currentComponent.getStateMap().get(stateKey),
                            String.valueOf(newTaskId));
                    taskMap.put(newTask.getId(), newTask);
                    newTaskId++;
                } else {
                    Task newTask = new Task(currentComponent, String.valueOf(newTaskId));
                    taskMap.put(newTask.getId(), newTask);
                    newTaskId++;
                    break;
                }
            }
        }

        return taskMap;
    }

//    private static void getSystemPathNode(List<Channel> channelList,
//                                          Map<String, Component> componentMap,
//                                          Map<String, PathNode> pathNodeList) {
//        //System.out.println(channelList.size());
//        for (Channel channel : channelList) {
//            String sourceId = channel.getAttr("source");
//            for (String componentKey : componentMap.keySet()) {
//                if (componentMap.get(componentKey).getLinkpointMap().get(sourceId) != null) {
//                    sourceId = componentMap.get(componentKey).getAttr("id");
//                }
//            }
//            String destId = channel.getAttr("dest");
//            for (String componentKey : componentMap.keySet()) {
//                if (componentMap.get(componentKey).getLinkpointMap().get(destId) != null) {
//                    destId = componentMap.get(componentKey).getAttr("id");
//                }
//            }
//            //System.out.println("source id is " + sourceId + " dest id is " + destId);
//            if (componentMap.get(destId) == null || componentMap.get(sourceId) == null) continue;
//            if (pathNodeList.get(destId) != null) pathNodeList.get(destId).setIsFirst(false);
//            else {
//                PathNode newDestNode = new PathNode(destId, componentMap.get(destId).getAttr("wcet"),
//                        componentMap.get(destId).getAttr("name"), false);
//                pathNodeList.put(destId, newDestNode);
//            }
//            if (pathNodeList.get(sourceId) != null) {
//                pathNodeList.get(sourceId).getNextComponents().add(pathNodeList.get(destId));
//            } else {
//                PathNode newSourceNode = new PathNode(sourceId, componentMap.get(sourceId).getAttr("wcet"),
//                        componentMap.get(sourceId).getAttr("name"), true);
//                newSourceNode.getNextComponents().add(pathNodeList.get(destId));
//                pathNodeList.put(sourceId, newSourceNode);
//            }
//
//        }
//    }


    /**
     * 根据组件的连接顺序给任务排序
     * @return 返回一个排好序的task map
     */
//    public Map<String, Task> staticSchedule(Map<String, Task> taskMap, Map<String, Component> compponentMap) {
//        Map<String, Task> taskList = new HashMap<>();
//
//
//
//        return taskList;
//    }

//    public static void main(String[] args) {
//        TaskExtraction mTaskExtraction = new TaskExtraction();
//
//        Map<String, Component> componentMap = new HashMap<>();
//        List<Channel> channelList = new ArrayList<>();
//        Map<String, Data> sharedDataMap = new HashMap<>();
//        XmlParse.parseXML("simulink0822.xml", componentMap, sharedDataMap, channelList);
//
//        Map<String, Task> taskMap;
//        taskMap = mTaskExtraction.taskExtraction(componentMap, channelList);
//
//        for (String key : taskMap.keySet()) {
//            System.out.println(key + " " + taskMap.get(key).getPeriod() + " "
//                    + taskMap.get(key).getComponentId());
//        }
//    }
}
