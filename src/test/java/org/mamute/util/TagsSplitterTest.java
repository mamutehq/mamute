package org.mamute.util;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class TagsSplitterTest {

    private TagsSplitterProperties getProperties(String regex) {
        TagsSplitterProperties properties = new TagsSplitterProperties();
        properties.setRegex(regex);
        return properties;
    }

    @Test
    public void should_split_tags_based_on_regex_for_comma() {
        TagsSplitter tagsSplitter = new TagsSplitter(getProperties("\\,"));
        List<String> splitTags = tagsSplitter.splitTags("java, ruby");
        assertThat(splitTags, hasItems("java", "ruby"));
    }

    @Test
    public void should_split_tags_based_on_regex_for_space_character() {
        TagsSplitter tagsSplitter = new TagsSplitter(getProperties("\\s+"));
        List<String> splitTags = tagsSplitter.splitTags("java ruby");
        assertThat(splitTags, hasItems("java", "ruby"));
    }

    @Test
    public void should_split_tags_based_on_regex_for_both_comma_and_space_character() {
        TagsSplitter tagsSplitter = new TagsSplitter(getProperties("[\\s+|\\,]"));
        List<String> splitTags = tagsSplitter.splitTags("java ruby,c");
        assertThat(splitTags, hasItems("java", "ruby", "c"));
    }

}
