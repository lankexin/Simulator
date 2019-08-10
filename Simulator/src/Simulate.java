import com.sun.xml.internal.ws.wsdl.writer.document.Fault;
import lmf.*;
import realtime.Schedule;
import safety.FaultInjection;
import util.EventProcess;
import util.PropertiyParse;
import util.XmlParse;

import java.util.*;

public class Simulate {

    private static int currentSystemTime;

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
     * 任务队列，有新的任务加入时调用schedule，生成优先级队列。
     */
    private static List<Task> taskQueue;

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

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentSystemTime != targetTime) {
                    for (String taskKey : taskMap.keySet()) {
                        // todo：检查task是否在运行中，如果在，检查他是否超过了deadline， 超过就记录

                        String currentStateId = taskMap.get(taskKey).getCurrentStateId();
                        List<Transition> transitions = componentMap.get(taskMap.get(taskKey).getComponentId())
                                .getTrantionById(currentStateId);

                        if (faultMap.get(currentStateId) != null) {
                            FaultInjection.inject();
                        }

                        if (currentSystemTime % taskMap.get(taskKey).getPeriod() == 0) {
                            Schedule.schedule(scheduleAlgorithm, taskMap.get(task), taskQueue);
                            taskMap.get(taskKey).setCurrentStateId();
                        }

                        for (Transition transition : transitions) {
                            if (EventProcess.eventProcess(transition.getEvent, dataMap)) {
                                Schedule.schedule(scheduleAlgorithm, taskMap.get(task), taskQueue);
                                break;
                            }
                        }
                    }

                    TaskExcute.taskExcute(currentSystemTime, taskQueue);
                }
            }
        });

        updateThread.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                currentSystemTime++;

            }
        }, 1000, 100);

    }

}
