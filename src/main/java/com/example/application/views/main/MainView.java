package com.example.application.views.main;

import com.example.application.CurrencyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.stream.Collectors;


@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {
    private String convertedAmount;
    private ComboBox<String> comboBoxFromCurrency = new ComboBox<>();
    private ComboBox<String> comboBoxToCurrency = new ComboBox<>();

    public MainView(@Autowired CurrencyService service) {
        addClassName("centered-content");

        initializeComboBoxFromCurrency(service);

        NumberField amountField = new NumberField();
        amountField.setWidth("12em");

        Div fromCurrencyPrefixDiv = new Div();
        fromCurrencyPrefixDiv.setText("From");
        amountField.setPrefixComponent(fromCurrencyPrefixDiv);

        comboBoxFromCurrency.addValueChangeListener(event -> {
            String selectedCurrency = event.getValue();
            if (selectedCurrency != null) {
                String fromCurrencyCodePrefix = service.getCurrencyCode(selectedCurrency);
                fromCurrencyPrefixDiv.setText(fromCurrencyCodePrefix);
            } else {
                fromCurrencyPrefixDiv.setText("From");
            }
        });

        initializeComboBoxToCurrency(service);

        Div toCurrencyPrefixDiv = new Div();
        toCurrencyPrefixDiv.setText("To");

        comboBoxToCurrency.addValueChangeListener(event -> {
            String selectedCurrency = event.getValue();
            if (selectedCurrency != null) {
                String toCurrencyCodePrefix = service.getCurrencyCode(selectedCurrency);
                toCurrencyPrefixDiv.setText(toCurrencyCodePrefix);
            } else {
                toCurrencyPrefixDiv.setText("To");
            }
        });

        Button invertButton = new Button(new Icon(VaadinIcon.ARROWS_LONG_H), e -> invertCurrencies());
        invertButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        invertButton.addClassName("button");

        Div wrappersContainer = new Div();
        wrappersContainer.add(comboBoxFromCurrency, invertButton, comboBoxToCurrency);
        wrappersContainer.addClassName("wrappers-container");

        Button convertButton = new Button("Convert", e -> {
            if (comboBoxFromCurrency.getValue() == null) {
                Notification.show("Select the currency you would like to convert from");
            }
            if (comboBoxToCurrency.getValue() == null) {
                Notification.show("Select the currency you would like to convert to");
            }
            if (comboBoxFromCurrency.getValue() != null && comboBoxToCurrency.getValue() != null) {
                Double amount = amountField.getValue();
                String fromCurrencyName = comboBoxFromCurrency.getValue();
                String toCurrencyName = comboBoxToCurrency.getValue();

                if (amount != null) {
                    try {
                        Double exchangeRate = service.getExchangeRate(fromCurrencyName, toCurrencyName);
                        Double result = amount / exchangeRate;
                        setConvertedAmount(result);
                        removeAllH2Elements();
                        H2 h2 = new H2("That amount is equivalent to " + convertedAmount + " " + comboBoxToCurrency.getValue() +"s");
                        h2.addClassName("h2");
                        add(h2);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    Notification.show("Please set an amount of currency");
                }
            }
        });
        convertButton.addClassName("button");

        H1 title = new H1("Currency converter");
        title.addClassName("h1");

        add(title, wrappersContainer, amountField, convertButton);
    }
    private void initializeComboBoxFromCurrency(CurrencyService service) {
        comboBoxFromCurrency.setItems(service.getAllCurrencyNames());
        comboBoxFromCurrency.setLabel("Select a currency");
        comboBoxFromCurrency.setPlaceholder("Choose an option");
        comboBoxFromCurrency.setClearButtonVisible(true);
        comboBoxFromCurrency.setRequired(true);
    }
    private void initializeComboBoxToCurrency(CurrencyService service) {
        comboBoxToCurrency.setItems(service.getAllCurrencyNames());
        comboBoxToCurrency.setLabel("Convert to");
        comboBoxToCurrency.setPlaceholder("Choose an option");
        comboBoxToCurrency.setClearButtonVisible(true);
        comboBoxToCurrency.setRequired(true);
    }
    private void invertCurrencies() {
        String fromCurrencyValue = comboBoxFromCurrency.getValue();
        String toCurrencyValue = comboBoxToCurrency.getValue();

        comboBoxFromCurrency.setValue(toCurrencyValue);
        comboBoxToCurrency.setValue(fromCurrencyValue);
    }
    private String parseAmount(Double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.####", symbols);
        return decimalFormat.format(amount);
    }
    private void setConvertedAmount(Double amount) {
        convertedAmount = parseAmount(amount);
    }
    private void removeAllH2Elements() {
        getChildren().filter(component -> component instanceof H2)
                .collect(Collectors.toList())
                .forEach(this::remove);
    }
}
