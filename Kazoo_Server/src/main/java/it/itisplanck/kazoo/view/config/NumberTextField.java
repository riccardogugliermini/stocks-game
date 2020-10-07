package it.itisplanck.kazoo.view.config;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * Textfield implementation that accepts formatted number and stores them in a
 * BigDecimal property The user input is formatted when the focus is lost or the
 * user hits RETURN.<br><br>
 *
 * Modificato da Onnivello Emanuele
 *
 * @author Thomas Bolz
 */
public class NumberTextField extends TextField {

    private ObjectProperty<Integer> number = new SimpleObjectProperty<>();

    public final Integer getNumber() {
        return Math.abs(number.get());
    }

    public final void setNumber(Integer value) {
        number.set(value);
    }

    public ObjectProperty<Integer> numberProperty() {
        return number;
    }

    public NumberTextField() {
        this(0);
    }
    
    public NumberTextField(Integer value) {
        super();
        initHandlers();
        setNumber(value);
    }

    private void initHandlers() {

        // try to parse when focus is lost or RETURN is hit
        setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                parseAndFormatInput();
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue)
                    parseAndFormatInput();
            }
        });

        // Set text in field if BigDecimal property is changed from outside.
        numberProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> obserable, Integer oldValue, Integer newValue) {
                setText(newValue.toString());
            }
            
        });
    }

    /**
     * Tries to parse the user input to a number according to the provided
     * NumberFormat
     */
    private void parseAndFormatInput() {
        try {
            String input = getText();
            if (input == null || input.length() == 0) {
                return;
            }
            Integer n = Math.abs(Integer.valueOf(input));
            setNumber(n);
            selectAll();
        } catch(NumberFormatException ex) {
            // If parsing fails keep old number
            setText(getNumber().toString());
        }
    }
}