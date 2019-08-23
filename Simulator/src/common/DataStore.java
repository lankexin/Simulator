package common;

import lmf.Component;
import lmf.TaskInstance;

public interface DataStore {
    void update(Component component, String dataName, String newValue);

    String get(TaskInstance taskInstance, Component component, String dataName);

//    boolean isShared(Component component, String dataName);
}
