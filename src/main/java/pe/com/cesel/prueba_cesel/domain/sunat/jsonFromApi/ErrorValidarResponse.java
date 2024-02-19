package pe.com.cesel.prueba_cesel.domain.sunat.jsonFromApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorValidarResponse {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("status")
    private Long status;
}
