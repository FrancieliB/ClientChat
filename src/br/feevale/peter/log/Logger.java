package br.feevale.peter.log;

public class Logger {

	public static void info(String message, Object... args) {
		log( LogLevel.INFO, message, args );
	}
	
	public static void warning(String message, Object... args) {
		log( LogLevel.WARN, message, args );
	}
	
	public static void error(String message, Object... args) {
		log( LogLevel.ERROR, message, args );
	}
	
	public static void error(Exception e) {
		error("%s", e.getLocalizedMessage());
		e.printStackTrace();
	}
	
	public static void log(LogLevel level, String message, Object... args) {
		message = String.format( message, args );
		level.println( message );
	}
}