package models;

import java.util.List;

public class Event{

    private Long noteId;

    private List<Period> periods;
    private String place;
    private String link;

    public enum Repeatability {
        DAY,
        WEEK,
        MONTH
    }
}
