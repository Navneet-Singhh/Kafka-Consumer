package CustomDeserializer.pojo;

public class FeDevice {
    private  String deviceName;
    private String deviceId;
    private Integer operatorId;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public FeDevice(){}
    public FeDevice(String deviceName, String deviceId, Integer operatorId) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.operatorId = operatorId;
    }

}
