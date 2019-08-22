package simulate;

import lmf.*;
import manager.TaskManagement;
import util.PropertiyParse;
import util.XmlParse;

import java.util.*;

public class Simulator {

    private static int currentSystemTime;
    private static int currentTimePiece;

    private static TaskManagement mTaskManageMent = new TaskManagement();
    private static TaskExcute mTaskExecute = new TaskExcute();

    /**记录失败任务的列表*/
    private static List<String> failureTaskList;

    /**解析出来的transitionMap
     * key：component id*/
    private static Map<String, Component> componentMap;

    /**记录状态的迁移过程
     * key：任务实例id
     * value：状态-event-data-timestamp*/
    private static Map<String, List<String>> statePathBuffer;

    /**
     * 共享部分的dataMap
     * key：data name（要求名字不能重复）
     */
    private static Map<String, Data> sharedDataMap;

    /**解析得到的dataMap
     * key：task id（要求名字不能重复）*/
    private static Map<String, Task> taskMap;

    private static List<Channel> channelList;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    private static Map<String, TaskInstance> waitingTaskInstanceList;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    private static List<TaskInstance> blockQueue;

    /**
     * 调度返回的时间片和任务的映射map
     */
    private static Map<Integer, String> timePieceMap;

    public static void main(String[] args) {
        componentMap = new HashMap<>();
        sharedDataMap = new HashMap<>();
        channelList = new ArrayList<>();
        XmlParse.parseXML("xml-name", componentMap, sharedDataMap, channelList);

        int targetTime = Integer.valueOf(PropertiyParse.readProperty("target execute time"));

        waitingTaskInstanceList = new HashMap<>();
        timePieceMap = new HashMap<>();

        Timer taskQueueManagementTimer = new Timer();
        taskQueueManagementTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSystemTime++;

                while (currentSystemTime != targetTime) {
                    timePieceMap = mTaskManageMent.timePieceMapManagement(currentSystemTime, taskMap,
                            waitingTaskInstanceList, blockQueue, componentMap);
                }
            }
        }, 1000, 10);

        //定时器
        Timer exucuteTimer = new Timer();
        exucuteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTimePiece++;
                mTaskExecute.taskExcute(currentTimePiece, componentMap, waitingTaskInstanceList,
                        blockQueue, timePieceMap, taskMap,
                        statePathBuffer);
            }
        }, 1000, 100);

        //日志：从当前的缓存区拿信息到文件中
        Timer logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, List<String>> statePathtemp = statePathBuffer;
                statePathBuffer = null;
                statePathBuffer = new HashMap<>();

                //todo：将state path写到文件里去。
            }
        }, 1000, 500);

    }

    public synchronized void update(String dataName , String newValue){
        Data data=sharedDataMap.get(dataName);
        data.setValue(newValue);
        sharedDataMap.put(dataName,data);
    }

    public String get(String dataName){
        String value=sharedDataMap.get(dataName).getValue();
        return value;
    }

}
