package cn.xidian.meetingroom.model;

public class ReservationWithBLOBs extends Reservation {
    private String meetingSubject;

    private String rejectionReason;

    public String getMeetingSubject() {
        return meetingSubject;
    }

    public void setMeetingSubject(String meetingSubject) {
        this.meetingSubject = meetingSubject == null ? null : meetingSubject.trim();
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason == null ? null : rejectionReason.trim();
    }
}