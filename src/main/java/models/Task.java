package models;

import java.time.LocalDateTime;

public class Task {

    private Long id;
    private Timetable timetable;
    private String name;
    private String description;
    private LocalDateTime notificationsStartTime;
    private LocalDateTime deadlineTime;
}
