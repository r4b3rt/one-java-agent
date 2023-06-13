package io.oneagent.plugin.config;

import io.oneagent.plugin.config.Config;

@Config(prefix = "system.test")
public class SystemObject {
    String systemKey;

    int systemIngeger;

    String nonSystemKey;

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public int getSystemIngeger() {
        return systemIngeger;
    }

    public void setSystemIngeger(int systemIngeger) {
        this.systemIngeger = systemIngeger;
    }

    public String getNonSystemKey() {
        return nonSystemKey;
    }

    public void setNonSystemKey(String nonSystemKey) {
        this.nonSystemKey = nonSystemKey;
    }
}
