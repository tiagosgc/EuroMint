package com.sergiocarvalho.banklogger;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Olá!");
		HttpGateway gateway = new HttpGateway();
		System.out.println(System.currentTimeMillis());
		// TODO username e password estão mal para poder usar o github
		gateway.bpiLogin("user","password");
		System.out.println(System.currentTimeMillis());
		gateway.bpiGetMovimentos();
	}

}
