package com.want.utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileImporter {
	/**
	 * @uml.property  name="content"
	 */
	String content;
	String name;
	public FileImporter(String url){
		content="";
		FileReader fr = null;
	      BufferedReader br = null;
	 
	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	    	  
	         fr = new FileReader (url);
	         //fr = new FileReader ("WEB_INF/scripts/"+ archivo);
	         br = new BufferedReader(fr);
	 
	         // Lectura del fichero
	         String linea;
	         while((linea=br.readLine())!=null){
	        	 content += linea;
	        	 System.out.println(linea);
	         }
	         String[] aux = url.split("\\");
	         name = aux[aux.length-1];
	           //this.name=name;
	      }
	      catch(Exception e){
	    	  System.out.println("Exception FileImmporter 1");
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	            System.out.println("Exception FileImmporter 2");
	         }
	      }
		
	}
	/**
	 * @return
	 * @uml.property  name="content"
	 */
	public String getContent() {
		return content;
	}
	public String getName(){
		return name;
	}
	
	
}
