package realtime;

import lmf.Channel;
import lmf.Component;
import lmf.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        System.out.println("task map *********************");
        for(String key : taskMap.keySet()) {
            System.out.println("task id is " + taskMap.get(key).getId() + " "  +
                    taskMap.get(key).getComponentId());
        }
        System.out.println("******************************");

        return taskMap;
    }


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
