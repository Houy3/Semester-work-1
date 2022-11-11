package models;

import SQL.SQLAnnotations.*;

import java.util.Date;
import java.util.Objects;

@Table(name="periods")
public class Period {

    @PK
    @Column(name = "id")
    private Long id;

    @Unique(group = 1)
    @Column(name = "event_id")
    private Long eventId;

    @Unique(group = 1)
    @Column(name = "start_time")
    private Date startTime;

    @Unique(group = 1)
    @Column(name = "end_time")
    private Date endTime;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;

    private Repeatability repeatability;
    private Date repeatabilityEndTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Repeatability getRepeatability() {
        return repeatability;
    }

    public void setRepeatability(Repeatability repeatability) {
        this.repeatability = repeatability;
    }

    public Date getRepeatabilityEndTime() {
        return repeatabilityEndTime;
    }

    public void setRepeatabilityEndTime(Date repeatabilityEndTime) {
        this.repeatabilityEndTime = repeatabilityEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!Objects.equals(id, period.id)) return false;
        if (!Objects.equals(eventId, period.eventId)) return false;
        if (!Objects.equals(startTime, period.startTime)) return false;
        if (!Objects.equals(endTime, period.endTime)) return false;
        if (!Objects.equals(groupId, period.groupId)) return false;
        if (repeatability != period.repeatability) return false;
        return Objects.equals(repeatabilityEndTime, period.repeatabilityEndTime);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (repeatability != null ? repeatability.hashCode() : 0);
        result = 31 * result + (repeatabilityEndTime != null ? repeatabilityEndTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", groupId=" + groupId +
                ", repeatability=" + repeatability +
                ", repeatabilityEndTime=" + repeatabilityEndTime +
                '}';
    }

    public enum Repeatability {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

}
