package xyz.fz.configuration.shiro.realm;

import java.util.Date;

public class JwtData {

    private String id;

    private int version;

    private Date expTime;

    private String ext;

    public JwtData(String id, int version, Date expTime) {
        this(id, version, expTime, "");
    }

    public JwtData(String id, int version, Date expTime, String ext) {
        this.id = id;
        this.version = version;
        this.expTime = expTime;
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "JwtData{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", expTime=" + expTime +
                ", ext='" + ext + '\'' +
                '}';
    }
}

