package net.frontlinesms.android.model.model;

public class Contact {

    private Integer id;
    private Integer rawId;
    private String displayName;
    private String mobile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRawId() {
        return rawId;
    }

    public void setRawId(Integer rawId) {
        this.rawId = rawId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
