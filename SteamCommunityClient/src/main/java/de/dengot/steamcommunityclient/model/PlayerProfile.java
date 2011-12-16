package de.dengot.steamcommunityclient.model;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PlayerProfile {

    private long steamId;

    private String steamName;

    private SteamGame game;

    /**
     * in hours
     */
    private double playtime;

    /**
     * achievementApiName -> unlockTimestamp
     */
    private Map<String, Calendar> unlockedAchievements;

    public PlayerProfile(long steamId, String steamName, SteamGame game) {
        super();
        this.steamId = steamId;
        this.steamName = steamName;
        this.game = game;
        this.unlockedAchievements = new HashMap<String, Calendar>();
    }

    public long getSteamId() {
        return steamId;
    }

    public String getSteamName() {
        return steamName;
    }

    public SteamGame getGame() {
        return game;
    }

    public void addAchievement(String achievementApiName, Calendar unlockTime) {
        this.unlockedAchievements.put(achievementApiName, unlockTime);
    }

    public boolean hasAchievement(String achievementApiName) {
        return this.unlockedAchievements.containsKey(achievementApiName);
    }

    public Calendar getUnlockTime(String achievementApiName) {
        return this.unlockedAchievements.get(achievementApiName);
    }

    public void setPlaytime(double playtime) {
        this.playtime = playtime;
    }

    public double getPlaytime() {
        return playtime;
    }

    public Set<String> getUnlockedAchievements() {
        return this.unlockedAchievements.keySet();
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}[{1}]: {2}", steamName, steamId, game);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (steamId ^ (steamId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PlayerProfile)) {
            return false;
        }
        PlayerProfile other = (PlayerProfile) obj;
        if (steamId != other.steamId) {
            return false;
        }
        return true;
    }

}
