package io.sample.demo.qa.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyboardEvents {

    ENTER("Enter"),
    TAB("Tab"),
    BACKSPACE("Backspace"),
    DELETE("Delete"),
    PAGE_DOWN("PageDown"),
    PAGE_UP("PageUp");

    private final String description;

//    KeyboardEvents(String description) {
//        this.description = description;
//    }
//
//    // Getter method to access the description
//    public String getDescription() {
//        return description;
//    }
}