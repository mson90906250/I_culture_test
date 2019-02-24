package tw.tcnr01.I_culture;

public class GetDataAdapter {
    //DB欄位相關設定
    public String ImageServerUrl;
    public String EventDate;
    public String EventTitle;
    public String EventContent;
    public String Date_start;
    public String Date_end;

    public String getImageServerUrl() {
        return ImageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.ImageServerUrl = imageServerUrl;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String EventDate) {
        this.EventDate = EventDate;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String EventTitle) {
        this.EventTitle = EventTitle;
    }

    public String getEventContent() {
        return EventContent;
    }

    public void setEventContent(String EventContent) {
        this.EventContent = EventContent;
    }

    public String getDate_start() {
        return Date_start;
    }

    public void setDate_start(String Date_start) {
        this.Date_start = Date_start;
    }

    public String getDate_end() {
        return Date_end;
    }

    public void setDate_end(String Date_end) {
        this.Date_end = Date_end;
    }

}