package realtime;

import lmf.Channel;
import lmf.Component;
import lmf.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskExtraction {

    public TaskExtraction() {

    }

    public Map<String, Task> taskExtraction(Map<String, Component> componentMap, List<Channel> channelList) {
        Map<String, Task> taskMap = new HashMap<>();

        return taskMap;
    }

    /**
     * 根据组件的连接顺序给任务排序
     * @return 返回一个排好序的task map
     */
    public Map<String, Task> staticSchedule(Map<String, Task> taskMap, Map<String, Component> compponentMap) {
        Map<String, Task> taskList = new HashMap<>();



        return taskList;
    }
}
