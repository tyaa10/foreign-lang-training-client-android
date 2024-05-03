package org.tyaa.training.client.android.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Универсальный преобразователь json-строк в java-объекты
 * */
public class JsonSerde {

    private static final ObjectMapper mObjectMapper = new ObjectMapper();

    /**
     * Получение объекта objectType из JSON-строки jsonString, содержащей поле полезной нагрузки типа contentType
     * @param jsonString строка в формате JSON, подлежащая десериализации
     * @param objectType тип объекта, который будет получен в результате десериализации
     * @param contentType тип объекта с данными полезной нагрузки, ссылка на который будет содержаться в одном из полей основного результирующего объекта
     * */
    public static  <O, C> O parseWithSingleContent (String jsonString, Class<O> objectType, Class<C> contentType) throws JsonProcessingException {
        return mObjectMapper.readValue(
                jsonString,
                mObjectMapper.getTypeFactory().constructParametricType(objectType, contentType)
        );
    }

    /**
     * Получение объекта objectType из JSON-строки jsonString, содержащей поле полезной нагрузки типа список объектов contentType
     * @param jsonString строка в формате JSON, подлежащая десериализации
     * @param objectType тип объекта, который будет получен в результате десериализации
     * @param contentType тип объектов списка с данными полезной нагрузки, ссылка на который будет содержаться в одном из полей основного результирующего объекта
     * */
    public static  <O, C> O parseWithListContent (String jsonString, Class<O> objectType, Class<C> contentType) throws JsonProcessingException {
        return mObjectMapper.readValue(
                jsonString,
                mObjectMapper.getTypeFactory().constructParametricType(
                        objectType,
                        mObjectMapper.getTypeFactory().constructParametricType(List.class, contentType)
                )
        );
    }

    /**
     * Получение JSON-строки из объекта типа T
     * @param object объект для сериализации
     * */
    public static <T> String serialize(T object) throws JsonProcessingException {
        return mObjectMapper.writeValueAsString(object);
    }

    // private static final Gson gson = new Gson();

    /* public static <T> T parseObject (String jsonString, Class<T> objectType) {
        return gson.fromJson(jsonString, objectType);
    } */

    /**
     * Преобразовать json-строку, содержащую массив значений, в java-объект список,
     * каждым элементом которого будет java-объект типа, заданного параметром {@code T}
     * @param <T> тип элементов списка
     * @param jsonString входная json-строка, которую необходимо преобразовать
     * @param itemType тип элементов выходного объекта-списка
     * @return список элементов типа {@code T}
     * */
    /* public static <T> List<T> parseList (String jsonString, Class<T> itemType) {
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
    } */
}
