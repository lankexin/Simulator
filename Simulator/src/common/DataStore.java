package common;

import lmf.Component;

public interface DataStore {
    void update(Component component, String dataName, String newValue);

    String get(Component component, String dataName);

//    boolean isShared(Component component, String dataName);
}
