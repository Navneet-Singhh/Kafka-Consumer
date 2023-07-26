package CustomDeserializer;


import CustomDeserializer.pojo.FeDevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class DeviceDersializer implements Deserializer<FeDevice> {
    @Override
    public FeDevice deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper=new ObjectMapper();
        FeDevice feDevice;
        try {
             feDevice = objectMapper.readValue(data, FeDevice.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return feDevice;
    }
}
