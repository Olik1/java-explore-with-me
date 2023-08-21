package ru.practicum.main_service.event.model;

public enum StateAction {
    /**
     * Изменение соcтояния события
     */
    SEND_TO_REVIEW, CANCEL_REVIEW, PUBLISH_EVENT, REJECT_EVENT
}
