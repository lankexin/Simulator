package common;

import lmf.Component;

public interface DataStore{
    void update( String dataName , String newValue);
    String get(String dataName);
    boolean isShared(String dataName);
}
