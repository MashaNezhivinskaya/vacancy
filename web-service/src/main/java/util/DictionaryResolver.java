package util;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maria on 24.11.17.
 */
public class DictionaryResolver {

    public static  <T> List<T> getObjects(String dictionaries, String code, Class<T> clazz) {
        String targetStr = dictionaries.substring(dictionaries.indexOf("\"" + code + "\":"));
        String dictionary = targetStr.substring(targetStr.indexOf("[") + 1, targetStr.indexOf("]"));
        return Arrays.stream(dictionary.split("\\},\\{")).map(str -> String.format("{%s}",str.replace("{", "").replace("}", "")))
                .map(str -> new Gson().fromJson(str, clazz)).collect(Collectors.toList());
    }
}
