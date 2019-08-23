package lmf;

import java.util.HashMap;
import java.util.Map;

public class Transition {
    private String source;
    private String dest;
    private String event;

    private Map<String, String> attrs;

    public Transition() {
        attrs = new HashMap<>();
    }

    public Transition(String source, String dest, String event) {
        this.source = source;
        this.dest = dest;
        this.event = event;
    }

    public String getSource() {
        source = attrs.get("source");
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        dest = attrs.get("dest");
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getEvent() {
        if (attrs.get("event") != null) {
            event = attrs.get("event");
        } else {
            event = "";
        }
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setAttr(String key, String value) {
        attrs.put(key, value);
    }

    public String getAttr(String key) {
        return attrs.get(key);
    }
}
