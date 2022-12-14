package models;


import SQL.SQLAnnotations.*;

import java.util.Date;
import java.util.Objects;

@Table(name = "tasks")
public class Task{

    @Unique
    @Column(name = "note_id")
    private Long noteId;

    @NotNull
    @Column(name = "notification_start_date")
    private Date notificationStartDate;

    @NotNull
    @Column(name = "deadline_time")
    private Date deadlineTime;

    @NotNull
    @Column(name = "is_done")
    private Boolean isDone;



    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }


    public Date getNotificationStartDate() {
        return notificationStartDate;
    }

    public void setNotificationStartDate(Date notificationStartDate) {
        this.notificationStartDate = notificationStartDate;
    }


    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
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
        result = 31 * result + (notificationStartDate != null ? notificationStartDate.hashCode() : 0);
        result = 31 * result + (deadlineTime != null ? deadlineTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "noteId=" + noteId +
                ", notificationsStartTime=" + notificationStartDate +
                ", deadlineTime=" + deadlineTime +
                '}';
    }
}
