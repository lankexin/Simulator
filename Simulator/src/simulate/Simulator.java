package simulate;

import com.sun.xml.internal.ws.wsdl.writer.document.Fault;
import lmf.*;
import realtime.Schedule;
import safety.FaultInjection;
import util.EventProcess;
import util.PropertiyParse;
import util.XmlParse;

import java.util.*;

import static realtime.Schedule.staticSchedule;

public class Simulator implements FaultInjection{

    private static int currentSystemTime;
    private static int currentTimePiece;

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
    private static Map<String, TaskInstance> taskMap;

    /**
     * 任务队列，有新的任务将其id加入该队列，调用schedule，生成优先级队列。
     */
    private static List<String> taskQueue;

    private static void updateByTime(State targetState, int currentTime) {
        // todo:
    }

    public static void main(String[] args) {
        XmlParse.parse("xml-name", componentMap, dataMap, taskMap);

        //从配置文件里
        Map<String, Fault> faultMap = FaultInjection.getFaultInjectionMap();
        String scheduleAlgorithm = PropertiyParse.read("schedule");

        // todo: 从配置

        taskQueue = new ArrayList<>();
        List<Task> staticScheduleList=staticSchedule();

        Timer taskQueueManagementTimer = new Timer();
        taskQueueManagementTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSystemTime++;


                while (currentSystemTime != targetTime) {

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

}
