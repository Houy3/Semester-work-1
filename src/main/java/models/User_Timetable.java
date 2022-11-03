package models;

import jdbc.SQLAnnotations.*;

import java.util.Objects;

@Table(name = "users_timetables", isInsertIncludeUniqueField = true)
public class User_Timetable {

    @Unique(group = 1)
    @Column(name = "user_id")
    private Long userId;

    @Unique(group = 1)
    @Column(name = "timetable_id")
    private Long timetableId;

    @Column(name = "access_rights")
    private Timetable.AccessRights accessRights;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }

    public Timetable.AccessRights getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Timetable.AccessRights accessRights) {
        this.accessRights = accessRights;
    }

    @EnumSetter(field_name = "accessRights")
    public void setAccessRights(String accessRights) {
        this.accessRights = Timetable.AccessRights.valueOf(accessRights);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User_Timetable that = (User_Timetable) o;

        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(timetableId, that.timetableId)) return false;
        return accessRights == that.accessRights;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (timetableId != null ? timetableId.hashCode() : 0);
        result = 31 * result + (accessRights != null ? accessRights.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User_Timetable{" +
                "userId=" + userId +
                ", timetableId=" + timetableId +
                ", accessRights=" + accessRights +
                '}';
    }
}
