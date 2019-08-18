package simulate;

import lmf.*;
import safety.FaultInjection;
import util.PropertiyParse;
import util.XmlParse;

import java.util.*;

public abstract class Simulator implements FaultInjection{

    private static int currentSystemTime;
    private static int currentTimePiece;

    private static TaskManagement mTaskManageMent = new TaskManagement();

    /**
     * 运行中各组件运行的过程记录
     * key: task id；
     * value: 状态-event-data-timestamp
     */
    private static Map<String, List<String>> statePath;


    /**
     *  记录失败任务的列表
     */
    private static List<String> failureTaskList;

    /**
     * 解析出来的transitionMap
     * key：component id
     */
    private static Map<String, Component> componentMap;

    /**
     * 共享部分的dataMap
     * key：data name（要求名字不能重复）
     */
    private static Map<String, Data> sharedDataMap;

    /**
     * 解析得到的dataMap
     * key：task id（要求名字不能重复）
     */
    private static Map<String, Task> taskMap;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    private static Map<String, TaskInstance> waitingQueue;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    private static List<TaskInstance> blockQueue;

    /**
     * 调度返回的时间片和任务的映射map
     */
    private static Map<Integer, TaskInstance> timePiece;

    public static void main(String[] args) {
        XmlParse.parse("xml-name", componentMap, sharedDataMap, taskMap);

        int targetTime = Integer.valueOf(PropertiyParse.read("target execute time"));

        waitingQueue = new HashMap<>();

        Timer taskQueueManagementTimer = new Timer();
        taskQueueManagementTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSystemTime++;

                while (currentSystemTime != targetTime) {
                    mTaskManageMent.timePieceMapManagement(currentSystemTime, taskMap,
                            waitingQueue, blockQueue, componentMap);
                }
            }
        }, 1000, 10);


        //定时器
        Timer exucuteTimer = new Timer();
        exucuteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTimePiece++;
                TaskExcute.taskExcute(currentTimePiece, taskQueue);
            }
        }, 1000, 100);



        //日志：从当前的缓存区拿信息到文件中
        Timer logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               //todo:把statepath的内容加入到文件
                statePath=null;
                statePath=new HashMap<>();
            }
        }, 1000, 500);

    }

    public static synchronized void update(String dataName , String newValue){
        Data data=sharedDataMap.get(dataName);
        data.setValue(newValue);
        sharedDataMap.put(dataName,data);
    }

    public static String get(String dataName){
        String value=sharedDataMap.get(dataName).getValue();
        return value;
    }


}
