package pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class OcrResponse {
    @JsonProperty("ParsedResults")
    private List<ParsedResult> parsedResults;

    @JsonProperty("OCRExitCode")
    private String ocrExitCode;

    @JsonProperty("IsErroredOnProcessing")
    private boolean isErroredOnProcessing;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ErrorDetails")
    private String errorDetails;

    @JsonProperty("SearchablePDFURL")
    private String searchablePDFURL;

    @JsonProperty("ProcessingTimeInMilliseconds")
    private String processingTimeInMilliseconds;
}
