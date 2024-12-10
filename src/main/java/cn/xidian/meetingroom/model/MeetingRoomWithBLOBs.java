package cn.xidian.meetingroom.model;

public class MeetingRoomWithBLOBs extends MeetingRoom {
    private String photoUrl;

    private String description;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}