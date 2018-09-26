package org.mamute.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tags.splitter")
public class TagsSplitterProperties {
    private String regex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
