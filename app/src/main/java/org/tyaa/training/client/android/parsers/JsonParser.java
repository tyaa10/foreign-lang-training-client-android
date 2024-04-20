package org.tyaa.training.client.android.parsers;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Универсальный преобразователь json-строк в java-объекты
 * */
public class JsonParser {

    private static final Gson gson = new Gson();

    public static <T> T parseObject (String jsonString, Class<T> objectType) {
        return gson.fromJson(jsonString, objectType);
    }

    /**
     * Преобразовать json-строку, содержащую массив значений, в java-объект список,
     * каждым элементом которого будет java-объект типа, заданного параметром {@code T}
     * @param <T> тип элементов списка
     * @param jsonString входная json-строка, которую необходимо преобразовать
     * @param itemType тип элементов выходного объекта-списка
     * @return список элементов типа {@code T}
     * */
    public static <T> List<T> parseList (String jsonString, Class<T> itemType) {
        // преобразование всей json-строки в список элементов стандартного типа
        List rawResultList = gson.fromJson(jsonString, List.class);
        // создание пустого списка выходного типа
        List<T> resultList = new ArrayList<>(rawResultList.size());
        // заполнение списка выходного типа элементами
        for(Object itemObject : rawResultList) {
            // сериализация текущего элемента стандартного типа в json-строку
            String itemJsonString = gson.toJson(itemObject);
            // преобразование json-строки текущего элемента в java-объект и добавление в список выходного типа
            resultList.add(gson.fromJson(itemJsonString, itemType));
        }
        return resultList;
    }
}
