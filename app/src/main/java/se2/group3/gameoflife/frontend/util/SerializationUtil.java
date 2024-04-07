package se2.group3.gameoflife.frontend.util;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import se2.group3.gameoflife.frontend.dto.PlayerDTO;

public class SerializationUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TAG = "Networking";

    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);

    }

    public static <T> Object toObject(String message, Class<T> messageType) throws JsonProcessingException {
        return objectMapper.readValue(message, messageType);
    }
}
