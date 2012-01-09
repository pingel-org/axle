package org.pingel.gestalt.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class GLogger extends Logger
{
    public static GLogger global = new GLogger("global gestalt logger", null);
    
    GLogger(String name, String resourceBundle)
    {
        super(name, resourceBundle);
        
        Handler h = null;
        try {
            h = new FileHandler("gestalt.log");
        }
        catch(IOException e) {
            System.err.println("Can't create log handler: " + e);
            System.exit(1);
        }
        h.setFormatter(new GFormatter());
        h.setLevel(Level.FINER);
        addHandler(h);
        setLevel(Level.FINER);
    }
    
}

class GFormatter extends Formatter
{
    public String format(LogRecord record)
    {
        String msg = record.getMessage();
        
        if( msg == null ) {
            return "\n";
        }
        else if( msg.equals("ENTRY") ) {
            return
            "Entering " +
            record.getSourceClassName() + " " +
            record.getSourceMethodName() + "\n";
        }
        else if ( msg.equals("RETURN") ) {
            return
            "Leaving " +
            record.getSourceClassName() + " " +
            record.getSourceMethodName() + "\n";
        }
        else {
            return record.getMessage() + "\n";
        }
        
    }
}
