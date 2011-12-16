package de.dengot.steamcommunityclient.model;

import java.text.MessageFormat;

public class SteamGame {

    private long appId;

    private String technicalName;

    private String title;

    public SteamGame(long appId, String technicalName, String title) {
        super();
        this.appId = appId;
        this.technicalName = technicalName;
        this.title = title;
    }

    public long getAppId() {
        return appId;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} [{1}, {2}]", getTitle(), getTechnicalName(), getAppId());
    }
}
