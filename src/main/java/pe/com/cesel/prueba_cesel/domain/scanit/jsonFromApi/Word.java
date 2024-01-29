package pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Word {
    @JsonProperty("WordText")
    private String wordText;

    @JsonProperty("Left")
    private double left;

    @JsonProperty("Top")
    private double top;

    @JsonProperty("Height")
    private double height;

    @JsonProperty("Width")
    private double width;

    // Getters y setters
}