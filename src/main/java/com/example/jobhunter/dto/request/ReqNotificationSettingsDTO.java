package com.example.jobhunter.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqNotificationSettingsDTO {
    private boolean notifyNewMessages;
    private boolean notifyProfileUpdates;
    private boolean notifyJobSuggestions;
}