package pisd.dk.dtu.compute.se.roborally.model;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This adapter is used to convert the FieldAction classes when serializing
 * and deserializing to the JSON file of the game board.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Adapter<E> implements JsonSerializer<E>, JsonDeserializer<E> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";
    public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = src.getClass().getName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);
        return retValue;
    }

    public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        Class<?> klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(INSTANCE), klass);
    }

}
