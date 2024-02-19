package pe.com.cesel.prueba_cesel.domain.scanit;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

@Component
public class TextExtractor {
    public String extractProvider(String text) {
        Pattern pattern = Pattern.compile("\\b(\\w+)\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).toUpperCase() : "";
    }

    public String extractRuc(String text) {
        Pattern pattern = Pattern.compile("\\b(\\d{11})\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public String extractTipoDocumento(String text) {
        Pattern pattern = Pattern.compile("\\b(factura|boleta)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(0).toUpperCase() : "";
    }

    public String extractNumeroDocumento(String text) {

        Pattern pattern = Pattern.compile("\\b([FfBbEeRr]\\d{3}\\s*-\\s*\\d{3,10}|\\d{1,5}\\s*-\\s*\\d{5,10})\\b");

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", "");
        }

        return "";
    }

    public String extractFechaEmision(String text) {
        Pattern pattern = Pattern.compile("\\b(\\d{2}[/-]\\d{2}[/-]\\d{4})\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }
    public List<BigDecimal> extractMoney(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{1,3}(?:[.,]?\\d{3})*\\.\\d{2,}\\b");
        Matcher matcher = pattern.matcher(text);
        List<BigDecimal> numericData = new ArrayList<>();

        while (matcher.find()) {
            String amount = processAmountString(matcher.group());
            System.out.println(amount);
            numericData.add(new BigDecimal(amount));
        }

        return processAmounts(numericData);
    }

    public String processAmountString(String number) {

        number = number.replaceAll("\\s+", "");

        long dotCount = number.chars().filter(ch -> ch == '.').count();
        long commaCount = number.chars().filter(ch -> ch == ',').count();

        if (dotCount > 1 && commaCount == 0) {

            number = number.replaceAll("\\.(?=.*\\.)", "");
        } else if (commaCount > 1 && dotCount == 0) {

            number = number.replaceAll(",(?=.*,)", "");
        } else if (dotCount == 1 && commaCount > 0) {

            number = number.replace(",", "");
        } else if (commaCount == 1 && dotCount > 0) {

            number = number.replaceAll("\\.(?=.*,)", "").replace(",", ".");
        } else if (dotCount == 1) {

            number = number.replace(",", "");
        } else if (commaCount == 1) {

            number = number.replace(",", ".");
        } else {

            number = number.replace(",", "").replace(".", "");
        }

        return number;
    }

    private List<BigDecimal> processAmounts(List<BigDecimal> numericData) {
        while (!numericData.isEmpty()) {
            numericData.sort(Collections.reverseOrder());
            List<BigDecimal> highestValues = numericData.subList(0, Math.min(numericData.size(), 2));

            if (highestValues.size() < 2) {
                break;
            }

            BigDecimal difference = highestValues.get(0).subtract(highestValues.get(1));

            BigDecimal twelvePercent = highestValues.get(0).multiply(new BigDecimal("0.12"));
            if (difference.compareTo(twelvePercent) < 0) {
                numericData.remove(highestValues.get(1));
            } else if (highestValues.get(1).compareTo(difference) < 0) {
                numericData.remove(highestValues.get(0));
            } else {

                difference = difference.setScale(2, RoundingMode.DOWN);
                return List.of(highestValues.get(0), highestValues.get(1), difference);
            }
        }

        return List.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public String extractTipoMoneda(String text) {
        Pattern pattern = Pattern.compile("\\b(soles|dolares|sol|dolar)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) { // Primero, busca una coincidencia
            String match = matcher.group(0).toLowerCase(); // Usa toLowerCase() para simplificar las comparaciones
            switch (match) { // Un switch es más limpio para este caso
                case "dolar":
                case "dolares":
                    return "DOLARES";
                case "sol":
                case "soles":
                    return "SOLES";
                default: // En teoría, este caso nunca debería alcanzarse debido a tu regex
                    return "";
            }
        } else {
            return ""; // Devuelve cadena vacía si no hay coincidencias
        }
    }


}