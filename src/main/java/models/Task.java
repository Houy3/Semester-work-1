package models;


import jdbc.SQLAnnotations.*;

import java.util.Date;
import java.util.Objects;

@Table(name = "tasks")
public class Task{

    @Id(isInsertIncludeId = true, isInsertReturnId = false)
    @Unique
    @NotNull
    @Column(name = "note_id")
    private Long noteId;

    @NotNull
    @Column(name = "notification_start_time")
    private Date notificationStartTime;

    @NotNull
    @Column(name = "deadline_time")
    private Date deadlineTime;


    @Getter(field_name = "id")
    public Long getNoteId() {
        return noteId;
    }

    @Setter(field_name = "id")
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    @Getter(field_name = "notificationStartTime")
    public Date getNotificationStartTime() {
        return notificationStartTime;
    }

    @Setter(field_name = "notificationStartTime")
    public void setNotificationStartTime(Date notificationStartTime) {
        this.notificationStartTime = notificationStartTime;
    }

    @Getter(field_name = "deadlineTime")
    public Date getDeadlineTime() {
        return deadlineTime;
    }

    @Setter(field_name = "deadlineTime")
    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!Objects.equals(noteId, task.noteId)) return false;
        return Objects.equals(deadlineTime, task.deadlineTime);
    }

    @Override
    public int hashCode() {
        int result = noteId != null ? noteId.hashCode() : 0;
        result = 31 * result + (notificationStartTime != null ? notificationStartTime.hashCode() : 0);
        result = 31 * result + (deadlineTime != null ? deadlineTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "noteId=" + noteId +
                ", notificationsStartTime=" + notificationStartTime +
                ", deadlineTime=" + deadlineTime +
                '}';
    }
}
