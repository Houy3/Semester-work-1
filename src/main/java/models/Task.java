package models;


import jdbc.SQLAnnotations.*;

import java.util.Date;
import java.util.Objects;

@Table(name = "tasks")
public class Task{

    @Id(isInsertIncludeId = true)
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



    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }


    public Date getNotificationStartTime() {
        return notificationStartTime;
    }

    public void setNotificationStartTime(Date notificationStartTime) {
        this.notificationStartTime = notificationStartTime;
    }


    public Date getDeadlineTime() {
        return deadlineTime;
    }

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
