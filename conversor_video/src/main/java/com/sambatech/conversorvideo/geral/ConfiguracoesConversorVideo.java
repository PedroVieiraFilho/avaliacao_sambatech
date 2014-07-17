/**
 * 
 */
package com.sambatech.conversorvideo.geral;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Pedro
 *
 */
public class ConfiguracoesConversorVideo {
	
	public static Properties carregaConfiguracoes() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"./properties/dados.properties");
		props.load(file);
		return props;
	}

}
