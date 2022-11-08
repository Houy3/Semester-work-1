package models;

import jdbc.SQLAnnotations.*;

import java.util.Objects;

@Table(name = "timetables")
public class Timetable {

    @PK
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable timetable = (Timetable) o;

        if (!Objects.equals(id, timetable.id)) return false;
        return Objects.equals(name, timetable.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public enum AccessRights {
        READER,
        WRITER,
        OWNER
    }
}
