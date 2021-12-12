package pcd.reactive.entry;

import java.util.Date;
import java.util.UUID;

public class EntryImpl implements Entry{
    protected final String id = UUID.randomUUID().toString();
    protected final String timestamp = new Date().toString();

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ID: "+id+"|TIMESTAMP: "+timestamp;
    }
}
