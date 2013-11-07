package br.feevale.peter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

import br.feevale.peter.log.Logger;

import server.ServerRMI;

import comm.Message;

import client.ClientRMI;
import exeception.PeterException;

public class Client implements ClientRMI {

	private Integer port;
	private String hostname;
	private BufferedReader br;
	private String name;
	private ServerRMI server;
	private boolean keepWainting;

	public void start() {
		prepareToReadConsole();

		try {
			readServerHostname();
			readPort();
			readName();
			conectToServer( hostname, port );

			enterCommands();
			Logger.info( "ss" );
			
			do{
				waitSendMessage();
			}while(keepWainting);
			
			Logger.info( "ww" );
			server.disconnectClient( this );
			Logger.info( "sd" );

		} catch( Exception e ) {
			Logger.error( "Erro na inicialização do cliente, tente novamente " );
			e.printStackTrace();
		};
	}

	private void enterCommands() {
		System.out.println( "Para sair so chat use : exit peter" );
		System.out.println( "Para enviar uma mensagem use : friend_name \"message\"" );
		System.out.println( "Para enviar mensagem para todos use : all \"message\"" );
		System.out.println( "Para listar as pessoas online use : list" );
	}

	private void waitSendMessage() throws IOException {
		Logger.info( "ff" );
		String command = br.readLine();
		Logger.info( "ff" );
		try {
			Message msg = null;

			switch( command.split( " " )[ 0 ] ) {
				case "exit":
					keepWainting = false;
					break;
				case "list":
					System.out.println( "Amigos online:" );
					
					for( String frind : server.list() ) {
						System.out.println( frind );
					}
					break;
				case "all":
					msg = new Message();
					msg.setSender( name );
					msg.setSendDate( new Date( System.currentTimeMillis() ) );
					msg.setMessage( command.split( " " )[ 1 ] );
					
					server.sendMessage( msg );

					break;
				default:
					msg = new Message();
					msg.setSender( name );
					msg.setAddressee( command.split( " " )[ 0 ] );
					msg.setSendDate( new Date( System.currentTimeMillis() ) );
					msg.setMessage( command.split( " " )[ 1 ] );
					
					server.sendMessage( msg );
					
					break;
			}
		} catch( PeterException pe ) {
			Logger.error( "Sistema não reconhe comando :", command );
		}
	}

	private void readName() throws IOException {
		System.out.println( "Informe seu nome:" );
		name = br.readLine();
	}

	private void readPort() throws NumberFormatException, IOException {
		System.out.println( "Informe a porta do servidor:" );
		port = Integer.parseInt( br.readLine().trim() );
	}

	private void readServerHostname() throws IOException {
		System.out.println( "Informe o endereço do servidor:" );
		hostname = br.readLine();
	}

	private void prepareToReadConsole() {
		InputStreamReader is = new InputStreamReader( System.in );
		br = new BufferedReader( is );
	}

	@Override
	public void conectToServer( String host, Integer port ) throws PeterException {
		try {
			server = (ServerRMI) Naming.lookup( String.format( ServerRMI.FORMAT_URL_SERVER, host, port ) );
			server.registryClientForCallBack( this );
		} catch( RemoteException | NotBoundException e ) {
			e.printStackTrace();
			System.out.println( "why???" );
			Logger.error( "Erro ao conectar com o servidor!" );
		} catch( MalformedURLException e ) {
			Logger.error( "Erro ao formatar URL!" );
		}
	}

	@Override
	public void receiveMessage( Message msg ) throws PeterException {
		System.out.println( String.format( ClientRMI.FORMAT_MSG, msg.getSender(), msg.getMessage() ) );
	}

	@Override
	public String getHostname() {
		return hostname;
	}

	@Override
	public Integer getPort() {
		return port;
	}

	@Override
	public String getName() {
		return name;
	}
}