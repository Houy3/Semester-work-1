package models;

import jdbc.SQLAnnotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "events")
public class Event {

    @Unique
    @Column(name = "note_id")
    private Long noteId;

    @Column(name = "place")
    private String place;

    @Column(name = "link")
    private String link;

    private List<Period> periods;


    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public void addPeriod(Period period) {
        if (periods == null) {
            periods = new ArrayList<>();
        }
        periods.add(period);
    }

    public void deletePeriod(Period period) {
        if (periods != null) {
            periods.remove(period);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return Objects.equals(noteId, event.noteId);
    }

    @Override
    public int hashCode() {
        int result = noteId != null ? noteId.hashCode() : 0;
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "noteId=" + noteId +
                ", place='" + place + '\'' +
                ", link='" + link + '\'' +
                '}';
    }


}
