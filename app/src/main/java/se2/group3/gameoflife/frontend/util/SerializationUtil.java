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
    private static final String TAG = "Serialization";

    public static String toJsonString(Object object){
        try{
             return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            Log.e(TAG, "Error mapping object to json string!", e.getCause());
            return null;
        }
    }

    public static <T> Object toObject(String message, Class<T> messageType) {
        try {
            return objectMapper.readValue(message, messageType);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Error deserializing string to java object!", e.getCause());
            return null;
        }
    }
}
