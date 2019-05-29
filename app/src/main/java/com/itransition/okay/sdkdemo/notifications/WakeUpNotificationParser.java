package com.itransition.okay.sdkdemo.notifications;

import com.google.gson.Gson;
import com.protectoria.pss.dto.notification.WakeUpNotification;

public class WakeUpNotificationParser extends BaseNotificationParser<WakeUpNotification> {
    public WakeUpNotificationParser(Gson gson) {
        super(gson);
    }

    @Override
    protected Class<WakeUpNotification> getNotificationType() {
        return WakeUpNotification.class;
    }
}
