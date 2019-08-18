package lmf;

import common.DataStore;

import java.util.Map;

public class Component implements DataStore {

    private String id;
    private String name;

    /**
     * 该组件下的所有data
     * key: data name
     */
    private Map<String, Data> dataMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Data> dataMap) {
        this.dataMap = dataMap;
    }

    public synchronized void update(String dataName,String newValue){
        dataMap.get(dataName).setValue(newValue);

    }
    public String get(String dataName){
        String value=dataMap.get(dataName).getValue();
        return value;
    }

    public boolean isShared(String dataName){
        boolean isShared=dataMap.get(dataName).isShared();
        return isShared;
    }
}
