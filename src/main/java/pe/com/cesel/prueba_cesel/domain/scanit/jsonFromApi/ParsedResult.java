package pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ParsedResult {
    @JsonProperty("TextOverlay")
    private TextOverlay textOverlay;
    @JsonProperty("TextOrientation")
    private String textOrientation;

    @JsonProperty("FileParseExitCode")
    private String fileParseExitCode;

    @JsonProperty("ParsedText")
    private String parsedText;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ErrorDetails")
    private String errorDetails;
}
