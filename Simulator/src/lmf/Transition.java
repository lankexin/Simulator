package lmf;

public class Transition {
    private String source;
    private String dest;
    private String event;

    public Transition(String source, String dest, String event) {
        this.source = source;
        this.dest = dest;
        this.event = event;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
