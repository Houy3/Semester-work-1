package models;

import jdbc.SQLAnnotations.*;

import java.util.Objects;

@Table(name = "notes")
public class Note {
    @PK
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "timetable_id")
    private Long timetableId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "body")
    private String body;

    @NotNull
    @Column(name = "is_in_archive")
    private Boolean isInArchive;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getInArchive() {
        return isInArchive;
    }

    public void setInArchive(Boolean inArchive) {
        isInArchive = inArchive;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", timetableId=" + timetableId +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                ", isInArchive=" + isInArchive +
                '}';
    }
}
