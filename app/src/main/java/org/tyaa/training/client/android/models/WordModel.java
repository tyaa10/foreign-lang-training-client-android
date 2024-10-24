package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель изучаемого слова
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"image", "pronunciationAudio"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordModel {

    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Слово на родном языке
     * */
    public String word;
    /**
     * Слово на изучаемом языке
     * */
    public String translation;
    /**
     * Порядковый номер слова в первоначальной последовательности изучения
     * */
    public Integer sequenceNumber;
    /**
     * Изображение обозначаемого словом в виде строки Base64-PNG
     * */
    public String image;
    /**
     * Аудио произношения слова на изучаемом языке в виде строки Base64-MP3
     * */
    public String pronunciationAudio;
}
