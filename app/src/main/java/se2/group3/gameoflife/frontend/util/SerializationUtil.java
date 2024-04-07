package se2.group3.gameoflife.frontend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SerializationUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TAG = "Networking";

    private SerializationUtil(){
        throw new UnsupportedOperationException();
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);

    }

    public static <T> Object toObject(String message, Class<T> messageType) throws JsonProcessingException {
        return objectMapper.readValue(message, messageType);
    }
}
