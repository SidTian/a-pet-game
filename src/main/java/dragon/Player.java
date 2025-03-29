package dragon;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号
    public float totalPlayTime;
    public int numSessions;
    public String name;
    public boolean isGuardian;
    public String password;
    public ArrayList<Pet> pets;
    public double score;
    public LocalTime ValidStartTime = LocalTime.of(0, 0);
    public LocalTime ValidEndTime = LocalTime.of(23, 59);
    private LocalTime sessionStartTime = LocalTime.now();

    public Player() {
        totalPlayTime = 0;
        numSessions = 0;
        isGuardian = false;
        name = "test";
        password = "test";
        pets = new ArrayList<>();
    }

    public void switchGuardian() {
        isGuardian = !isGuardian;
    }

    public void enableGuardian(String password) {
        isGuardian = true;
        this.password = password;
    }

    public void sessionStatistic() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime.toSecondOfDay();
        totalPlayTime += sessionDuration;
        numSessions++;
    }

    public void resetPlayTime() {
        totalPlayTime = 0;
        numSessions = 0;
    }

    public float calculateAveragePlayTime() {
        if (numSessions > 0) {
            return totalPlayTime / numSessions;
        } else {
            return 0;
        }
    }

    /**
     * @param password from user's input to enable Parent Control.
     *                 When parent control is enabled, time-limit will be removed,
     *                 and revive pet function is showing on the UI
     *
     * @see #checkPassword
     */
    public void enableParentControl(String password) {
        if (checkPassword(password)) {
            isGuardian = true;
        }

    }

    private boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getPassword() {
        return this.password;
    }

    public void setValidTimePeriod(LocalTime validStartTime, LocalTime validEndTime) {
        this.ValidStartTime = validStartTime;
        this.ValidEndTime = validEndTime;
    }

    public boolean isValid() {
        LocalTime currTime = LocalTime.now();
        return (currTime.isAfter(ValidStartTime) && ValidEndTime.isAfter(currTime)) || isGuardian;
    }

}
