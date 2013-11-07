package br.feevale.peter;

import br.feevale.peter.log.Logger;


public class ClientStart {
	
	public static void main( String[] args ) {
		try {
			Logger.info( "Conectando..." );
			
			Client client = new Client();
			client.start();
		} catch ( Exception e ) {
			Logger.error( e );
		}
	}
}
