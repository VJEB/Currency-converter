package org.vaadin.example;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

@Service
public class CurrencyService {
    private static final HashMap<String, String> currencyMap = new HashMap<>();

    static {
        currencyMap.put("US Dollar", "USD");
        currencyMap.put("Euro", "EUR");
        currencyMap.put("Japanese Yen", "JPY");
        currencyMap.put("British Pound", "GBP");
        currencyMap.put("Canadian Dollar", "CAD");
        currencyMap.put("Australian Dollar", "AUD");
        currencyMap.put("Swiss Franc", "CHF");
        currencyMap.put("Chinese Yuan Renminbi", "CNY");
        currencyMap.put("Swedish Krona", "SEK");
        currencyMap.put("New Zealand Dollar", "NZD");
        currencyMap.put("Mexican Peso", "MXN");
        currencyMap.put("Singapore Dollar", "SGD");
        currencyMap.put("Hong Kong Dollar", "HKD");
        currencyMap.put("Norwegian Krone", "NOK");
        currencyMap.put("South Korean Won", "KRW");
        currencyMap.put("Turkish Lira", "TRY");
        currencyMap.put("Russian Ruble", "RUB");
        currencyMap.put("Indian Rupee", "INR");
        currencyMap.put("Brazilian Real", "BRL");
        currencyMap.put("South African Rand", "ZAR");
        currencyMap.put("Danish Krone", "DKK");
        currencyMap.put("Polish Zloty", "PLN");
        currencyMap.put("Thai Baht", "THB");
        currencyMap.put("Indonesian Rupiah", "IDR");
        currencyMap.put("Saudi Riyal", "SAR");
        currencyMap.put("Czech Koruna", "CZK");
        currencyMap.put("Malaysian Ringgit", "MYR");
        currencyMap.put("Hungarian Forint", "HUF");
        currencyMap.put("Chilean Peso", "CLP");
        currencyMap.put("Philippine Peso", "PHP");
        currencyMap.put("Emirati Dirham", "AED");
        currencyMap.put("Colombian Peso", "COP");
        currencyMap.put("Romanian Leu", "RON");
        currencyMap.put("Nigerian Naira", "NGN");
        currencyMap.put("Israeli Shekel", "ILS");
        currencyMap.put("Peruvian Sol", "PEN");
        currencyMap.put("Bangladeshi Taka", "BDT");
        currencyMap.put("Egyptian Pound", "EGP");
        currencyMap.put("Ukrainian Hryvnia", "UAH");
        currencyMap.put("Qatari Riyal", "QAR");
        currencyMap.put("Kuwaiti Dinar", "KWD");
        currencyMap.put("Moroccan Dirham", "MAD");
        currencyMap.put("Omani Rial", "OMR");
        currencyMap.put("Jordanian Dinar", "JOD");
        currencyMap.put("Bahraini Dinar", "BHD");
        currencyMap.put("Bolivian Boliviano", "BOB");
        currencyMap.put("Kazakhstani Tenge", "KZT");
        currencyMap.put("Kenyan Shilling", "KES");
        currencyMap.put("Vietnamese Dong", "VND");
        currencyMap.put("Croatian Kuna", "HRK");
        currencyMap.put("Dominican Peso", "DOP");
        currencyMap.put("Panamanian Balboa", "PAB");
        currencyMap.put("Costa Rican Colon", "CRC");
        currencyMap.put("Guatemalan Quetzal", "GTQ");
        currencyMap.put("Icelandic Krona", "ISK");
        currencyMap.put("Honduran Lempira", "HNL");
        currencyMap.put("Serbian Dinar", "RSD");
        currencyMap.put("Belarusian Ruble", "BYN");
        currencyMap.put("Georgian Lari", "GEL");
        currencyMap.put("Algerian Dinar", "DZD");
        currencyMap.put("Tunisian Dinar", "TND");
        currencyMap.put("New Taiwan Dollar", "TWD");
        currencyMap.put("Azerbaijani Manat", "AZN");
        currencyMap.put("Uruguayan Peso", "UYU");
        currencyMap.put("Armenian Dram", "AMD");
        currencyMap.put("Bulgarian Lev", "BGN");
        currencyMap.put("Namibian Dollar", "NAD");
        currencyMap.put("Pakistani Rupee", "PKR");
        currencyMap.put("Paraguayan Guarani", "PYG");
        currencyMap.put("Macedonian Denar", "MKD");
        currencyMap.put("Jamaican Dollar", "JMD");
        currencyMap.put("Botswana Pula", "BWP");
        currencyMap.put("Cambodian Riel", "KHR");
        currencyMap.put("Ethiopian Birr", "ETB");
        currencyMap.put("Tanzanian Shilling", "TZS");
        currencyMap.put("Mauritian Rupee", "MUR");
        currencyMap.put("Moldovan Leu", "MDL");
        currencyMap.put("Ugandan Shilling", "UGX");
        currencyMap.put("Zambian Kwacha", "ZMW");
        currencyMap.put("Mozambican Metical", "MZN");
        currencyMap.put("Nepalese Rupee", "NPR");
        currencyMap.put("Afghan Afghani", "AFN");
        currencyMap.put("Brunei Dollar", "BND");
        currencyMap.put("Laotian Kip", "LAK");
        currencyMap.put("Netherlands Antillean Guilder", "ANG");
        currencyMap.put("Sierra Leonean Leone", "SLL");
        currencyMap.put("Maldivian Rufiyaa", "MVR");
        currencyMap.put("Aruban Florin", "AWG");
        currencyMap.put("Guyanese Dollar", "GYD");
        currencyMap.put("Malawian Kwacha", "MWK");
        currencyMap.put("Fijian Dollar", "FJD");
        currencyMap.put("São Tomé and Príncipe Dobra", "STN");
        currencyMap.put("Bhutanese Ngultrum", "BTN");
        currencyMap.put("Gambian Dalasi", "GMD");
        currencyMap.put("Surinamese Dollar", "SRD");
        currencyMap.put("Saint Helena Pound", "SHP");
        currencyMap.put("Seychellois Rupee", "SCR");
        currencyMap.put("Lesotho Loti", "LSL");
        currencyMap.put("Liberian Dollar", "LRD");
        currencyMap.put("Rwandan Franc", "RWF");
        currencyMap.put("Sudanese Pound", "SDG");
    }
    private static URL getRequestUrl() throws MalformedURLException {
        String base_url = "http://api.exchangeratesapi.io/v1/latest";
        String api_key = "682241ff6a1d2b4856188b2152c69872";
        String GET_URL = base_url + "?access_key=" + api_key;
        return new URL(GET_URL);
    }
    private static JSONObject getExchangeRates() throws IOException {
        URL url = getRequestUrl();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }in.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONObject("rates");
    }
    private static final JSONObject exchangeRates;

    static {
        try {
            exchangeRates = getExchangeRates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getAllCurrencyNames() {
        return currencyMap.keySet();
    }

    public String getCurrencyCode(String currencyMapKey){
        return currencyMap.get(currencyMapKey);
    }

    public double getExchangeRate(String fromCurrencyKey, String toCurrencyKey) throws IOException {
        String fromCode = currencyMap.get(fromCurrencyKey);
        String toCode = currencyMap.get(toCurrencyKey);

        Double fromCodeRate = exchangeRates.getDouble(fromCode);
        Double toCodeRate = exchangeRates.getDouble(toCode);

        return fromCodeRate / toCodeRate;
    }
}
