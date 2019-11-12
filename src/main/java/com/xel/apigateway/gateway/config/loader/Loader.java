package com.xel.apigateway.gateway.config.loader;

/**
 * 
 * @author xeldawe
 *
 */
public class Loader {

	public Bean start() {
		Nio reader = new Nio();
		long time = System.currentTimeMillis();
		reader.read();
		time = System.currentTimeMillis()-time;
		System.out.println("Beolvasva: "+time+" ms");
		return reader.getLines().get(0);
	}
	
}
