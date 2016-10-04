package io.cadi.souklou.models;

/**
 * Created by arcadius on 23/08/16.
 */
public class Mark {

    private String key, subject, created;
    private Long mark;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String date) {
        this.created = date;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }
}
