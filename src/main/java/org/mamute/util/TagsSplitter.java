package org.mamute.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsSplitter {
    private final TagsSplitterProperties properties;

    @Autowired
    public TagsSplitter(TagsSplitterProperties properties) {
        this.properties = properties;
    }

    public List<String> splitTags(String tagNames) {
        if(tagNames == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(tagNames.split(properties.getRegex()))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());
    }
}
