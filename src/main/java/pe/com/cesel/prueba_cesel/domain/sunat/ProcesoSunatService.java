package pe.com.cesel.prueba_cesel.domain.sunat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.com.cesel.prueba_cesel.domain.sunat.jsonFromApi.ComprobanteSunatResponse;
import pe.com.cesel.prueba_cesel.domain.sunat.jsonFromApi.ErrorValidarResponse;
import pe.com.cesel.prueba_cesel.domain.sunat.jsonFromApi.TokenSunatResponse;
import pe.com.cesel.prueba_cesel.infra.errores.InvalidRequestError;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProcesoSunatService {
    @Value("${api.security.clientId}")
    private String clientId;
    @Value("${api.security.clientSecret}")
    private String clientSecret;
    public final CloseableHttpClient httpClient;
    @Autowired
    public ProcesoSunatService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private volatile String tokenActual;
    private volatile long tokenExpiryTimeMillis;


    private synchronized TokenSunatResponse obtenerTokenSunat() throws IOException {
        // Verificar si el token actual ha expirado o si nunca se ha obtenido
        if (tokenActual == null || System.currentTimeMillis() >= tokenExpiryTimeMillis) {
            HttpPost post = new HttpPost(
                    "https://api-seguridad.sunat.gob.pe/v1/clientesextranet/" + clientId + "/oauth2/token/");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            params.add(new BasicNameValuePair("scope", "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes"));
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    ObjectMapper objectMapper = new ObjectMapper();
                    TokenSunatResponse tokenResponse = objectMapper.readValue(jsonResponse, TokenSunatResponse.class);
                    tokenActual = tokenResponse.getAccess_token();
                    // Establece el tiempo de expiración del token a la hora actual más su duración válida
                    tokenExpiryTimeMillis = System.currentTimeMillis() + (tokenResponse.getExpires_in() * 1000L);
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.valueOf(response.getStatusLine().getStatusCode()),
                            "Error al obtener el token de SUNAT"
                    );
                }
            }
        }
        return new TokenSunatResponse(tokenActual, "JWT", (int) ((tokenExpiryTimeMillis - System.currentTimeMillis()) / 1000));
    }
    public DatosResultadoSunat validar(DatosValidarComprobante datos) throws IOException {

        String token = obtenerTokenSunat().getAccess_token();

        System.out.println(token);

        HttpPost post = new HttpPost("https://api.sunat.gob.pe/v1/contribuyente/contribuyentes/20101064191/validarcomprobante");
        post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(datos);


        HttpEntity entity = new StringEntity(jsonBody);
        post.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(post)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {

                ComprobanteSunatResponse sunatResponse = objectMapper.readValue(jsonResponse, ComprobanteSunatResponse.class);

                return new DatosResultadoSunat(
                        sunatResponse.isSuccess(),
                        sunatResponse.getMessage(),
                        sunatResponse.getData().getEstadoCp(),
                        sunatResponse.getData().getEstadoRuc(),
                        sunatResponse.getData().getCondDomiRuc(),
                        sunatResponse.getData().getObservaciones()
                );
            } else {

                ErrorValidarResponse errorResponse = objectMapper.readValue(jsonResponse, ErrorValidarResponse.class);

                throw new ValidacionDeIntegridad(errorResponse.getMessage());
            }
        } catch (Exception e) {

            throw new InvalidRequestError(e.getMessage());
        }
    }
}


