package org.springframework.samples.petclinic.game;

import org.springframework.format.Formatter;

import java.awt.*;
import java.text.ParseException;
import java.util.Locale;

public class ColorFormatter implements Formatter<Color> {
    @Override
    public Color parse(String text, Locale locale) throws ParseException {
        System.out.println("Parsing!");
        System.out.println("text " + text);

        if(text.equals("yellow"))
            return Color.YELLOW;
        else if(text.equals("red"))
            return Color.RED;
        else if(text.equals("blue"))
            return Color.BLUE;
        else if(text.equals("green"))
            return Color.GREEN;
        else
            return null;
    }

    @Override
    public String print(Color object, Locale locale) {
        return null;
    }
}
