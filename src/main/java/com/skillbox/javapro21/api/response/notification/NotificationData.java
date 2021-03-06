package com.skillbox.javapro21.api.response.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.javapro21.api.response.Content;
import com.skillbox.javapro21.domain.enumeration.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class NotificationData implements Content {
    private Long id;
    private NotificationType type;
    @JsonProperty(value = "sent_time")
    private Long sentTime;
}
