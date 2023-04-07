package ua.foxminded.javaspring.javastreamsapi;

import java.io.IOException;

public class DataParsingException extends IOException{
	
	public DataParsingException(String message) {
        super(message);
    }
    
    public DataParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
