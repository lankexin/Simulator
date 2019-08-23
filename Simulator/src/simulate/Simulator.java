package simulate;

import common.DataStore;
import lmf.*;
import manager.TaskManagement;
import realtime.TaskExtraction;
import util.PropertiyParse;
import util.XmlParse;

import java.io.IOException;
import java.util.*;

import static util.FileUtil.writeFile;
import static util.PropertiyParse.readProperty;

public class Simulator implements DataStore {

    //static int currentSystemTime;
    public static int currentTimePiece;

    /**一个时间片的长度*/
    public static float timePiece=Float.valueOf(readProperty("realtime.schedule.timepiece"));

    private static TaskManagement mTaskManageMent = new TaskManagement();
    private static TaskExcute mTaskExecute = new TaskExcute();
    private static TaskExtraction mTaskExtraction = new TaskExtraction();

    /**记录失败任务的列表*/
    static List<String> failureTaskList;

    static String filePath=readProperty("logFilePath");

    static String faultPath=readProperty("faultFilePath");

    /**解析出来的transitionMap
     * key：component id*/
    static Map<String, Component> componentMap;

    /**记录状态的迁移过程
     * key：任务实例id
     * value：状态-event-data-timestamp*/
    static Map<String, List<String>> statePathBuffer=new LinkedHashMap<>();

    static Map<String, List<String>> faultBuffer;


    /**
     * 共享部分的dataMap
     * key：data name（要求名字不能重复）
     */
    static Map<String, Data> sharedDataMap;

    /**
     * 传输部分的dataMap
     * key：data name（要求名字不能重复）
     */
    static Map<String, List<String>> channelDataMap = new HashMap<>();

    /**解析得到的dataMap
     * key：task id（要求名字不能重复）*/
    static Map<String, Task> taskMap;

    static List<Channel> channelList;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    static Map<String, TaskInstance> waitingTaskInstanceList;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    static List<TaskInstance> blockQueue;

    /**
     * 调度返回的时间片和任务的映射map
     */
    static Map<Integer, String> timePieceMap;

    public static void main(String[] args) {
        componentMap = new HashMap<>();
        sharedDataMap = new HashMap<>();
        channelList = new ArrayList<>();
        XmlParse.parseXML("xml-name", componentMap, sharedDataMap, channelDataMap, channelList);

        taskMap = mTaskExtraction.taskExtraction(componentMap, channelList);

        int targetTime = Integer.valueOf(PropertiyParse.readProperty("target execute time"));

        waitingTaskInstanceList = new HashMap<>();
        timePieceMap = new HashMap<>();
        blockQueue = new ArrayList<>();

        Timer taskQueueManagementTimer = new Timer();
        taskQueueManagementTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //currentSystemTime++;

                while (currentTimePiece != targetTime) {
                    timePieceMap = mTaskManageMent.timePieceMapManagement(currentTimePiece, taskMap,
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
                mTaskExecute.taskExcute();
            }
        }, 1000, 100);

        //日志：从当前的缓存区拿信息到文件中
        Timer logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, List<String>> statePathtemp = statePathBuffer;
                Map<String,List<String>> faultPathtemp=faultBuffer;
                statePathBuffer = null;
                statePathBuffer = new LinkedHashMap<>();

                faultBuffer=null;
                faultBuffer=new LinkedHashMap<>();

                //todo：将state path写到文件里去。
                try {
                    writeFile(filePath,statePathtemp);
                    writeFile(filePath,faultPathtemp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 1000, 500);

    }
    @Override
    public synchronized void update(Component component, String dataName, String newValue){
        Data data=sharedDataMap.get(dataName);
        data.setValue(newValue);
        sharedDataMap.put(dataName,data);
    }

    @Override
    public String get(Component component, String dataName){
        String value=sharedDataMap.get(dataName).getValue();
        return value;
    }

}