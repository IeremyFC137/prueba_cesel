package pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Line {
    @JsonProperty("Words")
    private List<Word> words;

    @JsonProperty("MaxHeight")
    private double maxHeight;

    @JsonProperty("MinTop")
    private double minTop;

    @JsonProperty("LineText")
    private String lineText;

}