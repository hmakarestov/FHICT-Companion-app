package org.fhict.fhictcompanion;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private Date start;
    private Date end;
    private String title;
    private String location;
    private String description;
    private boolean allDay;

    public  Event(){
        start = null;
        end = null;
        title = null;
        location = null;
        description = null;
        allDay = false;
    }
    public Event(Date start, Date end, String title, String location, String description, boolean allDay)
    {
        this.start=start;
        this.end=end;
        this.title=title;
        this.location=location;
        this.description=description;
        this.allDay=allDay;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAllDay() {
        return allDay;
    }
    public boolean isEqual(Event e)
    {
        if(e.getStart() == start &&
            e.getEnd() == end &&
            e.getLocation() == getLocation() &&
            e.getTitle() == title &&
            e.getDescription() == description &&
            e.isAllDay() == allDay) return true;
        else return false;
    }
}


