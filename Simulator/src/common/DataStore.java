package common;

public interface DataStore{
    void update(String component, String dataName);
    String get(String component, String dataName);
    String isShared(String dataName);
}
