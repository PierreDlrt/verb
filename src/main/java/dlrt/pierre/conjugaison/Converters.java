package dlrt.pierre.conjugaison;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {
    @TypeConverter
    public static VxT fromString(String value) {
        Type listType = new TypeToken<VxT>(){}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromVxT(VxT list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}