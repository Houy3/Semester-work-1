package models;

import jdbc.SQLAnnotations.*;

import java.util.Objects;

@Table(name = "timetables")
public class Timetable {

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;


    @Getter(field_name = "id")
    public Long getId() {
        return id;
    }

    @Setter(field_name = "id")
    public void setId(Long id) {
        this.id = id;
    }

    @Getter(field_name = "name")
    public String getName() { return name;}

    @Setter(field_name = "name")
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
