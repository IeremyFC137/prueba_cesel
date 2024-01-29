package pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TextOverlay {
    @JsonProperty("Lines")
    private List<Line> lines;

    @JsonProperty("HasOverlay")
    private boolean hasOverlay;

    @JsonProperty("Message")
    private String message;

}
