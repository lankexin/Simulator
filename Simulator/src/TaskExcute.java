import lmf.Task;

import java.util.List;

public class TaskExcute {


    public static void taskExcute(int currentTimePiece, List<Task> taskQueue) {
        boolean hasExecutingTask = false;

        /**
         * 在队列里找到当前需要执行的task并使其开始执行
         * 即 更改该任务的 execute time
         */
        while (!hasExecutingTask) {

            if (currentTime - taskQueue.get(0).getExecuteTimestamp() >= taskQueue.get(0).getWcet()) {
                // todo: 检查是否会迁移，如果不迁移了，删除第一个任务，并执行第二个任务
            }

            // todo: 当找到一个满足迁移条件和wcet的任务，设置hasExcutingTask为true；并在statePath中记录当前的状态

        }
    }
}
