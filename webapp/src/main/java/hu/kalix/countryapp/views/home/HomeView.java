package hu.kalix.countryapp.views.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import hu.kalix.countryapp.service.RestClientService;

import java.util.List;

@PWA(name = "CountryApp", shortName = "CountryApp", enableInstallPrompt = false)
@Theme(themeFolder = "countryapp", variant = Lumo.DARK)
@PageTitle("Country App")
@Route(value = "/")
@RouteAlias(value = "")
public class HomeView extends HorizontalLayout {
    private final TextField countryName;
    private final TextField capitalName;
    private final Button findButton;
    private final Button reset;
    private final TextArea textArea;
    private final H2 headTitle;

    public HomeView() {
        setSizeFull();

        this.getElement().getStyle().set("background-image", "url('images/globe.jpg')");
        this.getElement().getStyle().set("background-repeat", "no-repeat");
        this.getElement().getStyle().set("background-attachment", "fixed");
        this.getElement().getStyle().set("background-position", "top left");
        this.getElement().getStyle().set("overflow", "hidden");

        headTitle = new H2("Country and capital finder");

        countryName = new TextField("Country name");
        countryName.setWidth(300, Unit.PIXELS);
        countryName.setClearButtonVisible(true);
        countryName.setPattern("^[a-zA-Z]+");
        countryName.setErrorMessage("Invalid format");
        countryName.setValueChangeMode(ValueChangeMode.EAGER);

        capitalName = new TextField("Capital name");
        capitalName.setWidth(300, Unit.PIXELS);
        capitalName.setClearButtonVisible(true);
        capitalName.setPattern("^[a-zA-Z]+");
        capitalName.setErrorMessage("Invalid format");
        capitalName.setValueChangeMode(ValueChangeMode.EAGER);

        findButton = new Button("Find", new Icon(VaadinIcon.SEARCH));
        findButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        findButton.setWidth(300, Unit.PIXELS);

        findButton.addClickListener(e -> {
            Action();
        });

        findButton.addClickShortcut(Key.ENTER);

        textArea = new TextArea();
        textArea.setWidth(300, Unit.PIXELS);
        textArea.setHeight(200, Unit.PIXELS);
        textArea.setLabel("Result");
        textArea.setReadOnly(true);
        textArea.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);

        reset = new Button("Reset", new Icon(VaadinIcon.TRASH));
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        reset.setWidth(300, Unit.PIXELS);

        reset.addClickListener(e -> {
            countryName.setValue("");
            capitalName.setValue("");
            textArea.setValue("");
            countryName.setInvalid(false);
            capitalName.setInvalid(false);
        });

        reset.addClickShortcut(Key.ESCAPE);

        VerticalLayout form = new VerticalLayout();
        VerticalLayout space = new VerticalLayout();
        form.add(headTitle, countryName, capitalName, findButton, reset, textArea);

        form.setSpacing(false);
        form.setPadding(true);
        form.setAlignItems(Alignment.CENTER);
        form.setMinWidth(375, Unit.PIXELS);
        form.setMaxWidth(375, Unit.PIXELS);
        form.getElement().getStyle().set("background-color", "black");

        add(form, space);
    }

    private void Action() {
        if (!countryName.isEmpty() && capitalName.isEmpty()) {

            if (countryName.isInvalid()) {
                return;
            }

            try {
                List<JsonNode> nodes = RestClientService.getCountryByName(countryName.getValue());
                setTextArea(nodes);
            } catch (Exception exception) {
                Notification.show("Not found", 3000, Notification.Position.TOP_STRETCH);
            }
        } else if (!capitalName.isEmpty() && countryName.isEmpty()) {
            if (capitalName.isInvalid()) {
                return;
            }

            try {
                List<JsonNode> nodes = RestClientService.getCountryByCapital(capitalName.getValue());
                setTextArea(nodes);
            } catch (Exception exception) {
                Notification.show("Not found", 3000, Notification.Position.TOP_STRETCH);
            }
        }
    }

    private void setTextArea(List<JsonNode> nodes) {
        StringBuilder sb = new StringBuilder();
        for (JsonNode node : nodes) {
            sb.append(node.findValue("name").asText());
            sb.append(": ");
            sb.append(node.findValue("capital").asText());
            sb.append("\n");
        }
        textArea.setValue(sb.toString());
    }
}
