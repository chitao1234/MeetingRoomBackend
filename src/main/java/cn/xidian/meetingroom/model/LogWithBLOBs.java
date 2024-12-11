package cn.xidian.meetingroom.model;

public class LogWithBLOBs extends Log {
    private String operationDetails;

    private byte[] ipAddress;

    public String getOperationDetails() {
        return operationDetails;
    }

    public void setOperationDetails(String operationDetails) {
        this.operationDetails = operationDetails == null ? null : operationDetails.trim();
    }

    public byte[] getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(byte[] ipAddress) {
        this.ipAddress = ipAddress;
    }
}