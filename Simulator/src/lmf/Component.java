package lmf;

import java.util.Map;

public class Component  {

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


}
