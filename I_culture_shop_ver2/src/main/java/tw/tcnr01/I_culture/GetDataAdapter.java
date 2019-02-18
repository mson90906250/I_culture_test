package tw.tcnr01.I_culture;

public class GetDataAdapter {
//DB欄位相關設定
    public String ImageServerUrl;
    public String EventDate;
    public String EventTitle;

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

}