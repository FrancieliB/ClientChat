package br.feevale.peter.log;

import java.io.PrintStream;

public enum LogLevel {

	INFO("INFO", System.out),
	WARN("WARN", System.out),
	ERROR("ERROR", System.err);
	
	private String label;
	private PrintStream out;
	
	private LogLevel(String label, PrintStream out) {
		this.label = label;
		this.out = out;
	}
	
	public String toString() {
		return label;
	}
	
	public void println(String message) {
		message = String.format( "%s: %s", toString(), message );
		out.println( message );
	}
	
}
