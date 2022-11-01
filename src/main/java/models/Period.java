package models;

import jdbc.SQLAnnotations.*;

import java.util.Date;
import java.util.Objects;

@Table(name="periods")
public class Period {

    @Id(isInsertIncludeId = true)
    @NotNull
    @Column(name = "event_id")
    private Long eventId;

    @NotNull
    @Column(name = "start_time")
    private Date startTime;

    @NotNull
    @Column(name = "end_time")
    private Date endTime;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!Objects.equals(eventId, period.eventId)) return false;
        if (!Objects.equals(startTime, period.startTime)) return false;
        if (!Objects.equals(endTime, period.endTime)) return false;
        return Objects.equals(groupId, period.groupId);
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "eventId=" + eventId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
