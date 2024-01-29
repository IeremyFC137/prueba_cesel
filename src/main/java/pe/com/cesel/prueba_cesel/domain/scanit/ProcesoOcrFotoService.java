package pe.com.cesel.prueba_cesel.domain.scanit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.HttpEntity;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.com.cesel.prueba_cesel.domain.scanit.jsonFromApi.OcrResponse;


@Service
public class ProcesoOcrFotoService {
    @Value("${api.security.ocr}")
    private String apiSecret;
    @Autowired
    private TextExtractor textExtractor;
    public final CloseableHttpClient httpClient;

    @Autowired
    public ProcesoOcrFotoService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public DatosResultadoOcr procesar(MultipartFile file) throws IOException {

        HttpPost post = new HttpPost("https://api.ocr.space/parse/image");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("isOverlayRequired", "false");
        builder.addTextBody("apikey", apiSecret);
        builder.addTextBody("language", "spa");
        builder.addTextBody("isTable", "true");
        builder.addTextBody("OCREngine", "2");
        builder.addTextBody("scale", "true");
        builder.addBinaryBody("file",
                leerContenidoArchivo(file),
                ContentType.create("image/png"),
                file.getOriginalFilename());

        HttpEntity multipart = builder.build();
        post.setEntity(multipart);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {

                String jsonResponse = EntityUtils.toString(response.getEntity());

                ObjectMapper objectMapper = new ObjectMapper();

                OcrResponse ocrResponse = objectMapper.readValue(jsonResponse, OcrResponse.class);

                if (ocrResponse.getParsedResults() != null && !ocrResponse.getParsedResults().isEmpty()) {

                    String parsedText = ocrResponse.getParsedResults().get(0).getParsedText();
                    System.out.println(parsedText);
                    return new DatosResultadoOcr(
                            textExtractor.extractProvider(parsedText),
                            textExtractor.extractRuc(parsedText),
                            textExtractor.extractTipoDocumento(parsedText),
                            textExtractor.extractNumeroDocumento(parsedText),
                            textExtractor.extractFechaEmision(parsedText),
                            textExtractor.extractTipoMoneda(parsedText),
                            textExtractor.extractMoney(parsedText).get(1),
                            textExtractor.extractMoney(parsedText).get(2),
                            textExtractor.extractMoney(parsedText).get(0)
                        );
                    }

                }
            }   catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
            }

        throw new RuntimeException("Error processing the image");
    }

    public byte[] leerContenidoArchivo(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        return file.getBytes();
    }

}
