package pe.com.cesel.prueba_cesel.domain.sunat.jsonFromApi;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DataResponse {
    @JsonProperty("estadoCp")
    private String estadoCp;
    @JsonProperty("estadoRuc")
    private String estadoRuc;
    @JsonProperty("condDomiRuc")
    private String condDomiRuc;
    @JsonProperty("observaciones")
    private List observaciones = new ArrayList<>();
}