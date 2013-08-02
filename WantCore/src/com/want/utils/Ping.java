package com.want.utils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ping {
	
	static final String ip = "localhost:15674/stomp";
	
	public static boolean isAlive(){
		InetAddress in = null;
		boolean alive = false;
	                    try {
	                        //ping a una ip
	                        in = InetAddress.getByName(ip);
	                    } catch (UnknownHostException ex) {
	                    	System.out.println("Host de destino inaccesible");
	                    }
	                    catch (NullPointerException npe) {
	                    	System.out.println("Host de destino inaccesible");
	                    }
	                    try {
	                        //Definimos un tiempo en el cual ha de responder
	                        if (in.isReachable(5000)) {
	                        	System.out.println("Respuesta desde "+ip);
	                        	alive = true;
	                        } else {
	                        	System.out.println("No responde: Time out");
	                        }
	                    } catch (IOException ex) {
	                    	System.out.println("Error entrada / Salida "+ex);
	                    }
	                    catch (NullPointerException npe) {
	                    	System.out.println("Host de destino inaccesible");
	                    }
	     return alive;
	}
}
