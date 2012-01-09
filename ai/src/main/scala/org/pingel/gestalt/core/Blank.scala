package org.pingel.gestalt.core;

import java.awt.Color;

public class Blank extends SimpleForm {

    public Blank()
    {
        super(new Name("?"));
        color = new Color(100, 200, 240);
    }
    
    public Form duplicate() {
        return new Blank();
    }
    
}
