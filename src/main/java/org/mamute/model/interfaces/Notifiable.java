package org.mamute.model.interfaces;

import org.joda.time.DateTime;
import org.mamute.model.User;

import java.time.LocalDateTime;

public interface Notifiable {
    public String getTrimmedContent();
    public LocalDateTime getCreatedAt();
    public String getTypeNameKey();
    public String getEmailTemplate();
    public User getAuthor();
}
