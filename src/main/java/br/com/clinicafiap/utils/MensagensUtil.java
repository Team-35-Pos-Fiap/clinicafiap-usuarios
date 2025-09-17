package br.com.clinicafiap.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MensagensUtil {
	private static MessageSource messageSource;
	
	private static Locale locale = Locale.of("pt", "BR");
	
	public static final String ERRO_INTERNAL_SERVER_ERROR = "exception.internal_server_error";
	public static final String ERRO_USUARIO_NAO_ENCONTRADO = "exception.usuario_nao_encontrado";
	public static final String ERRO_USUARIOS_NAO_ENCONTRADOS = "exception.usuarios_nao_encontrados";
	public static final String ERRO_PERFIL_NAO_ENCONTRADO = "exception.perfil_nao_encontrado";
	public static final String ERRO_PARAMETRO_INVALIDO = "exception.erro_parametro_invalido";
	public static final String ERRO_EMAIL_DUPLICADO = "exception.email_duplicado";

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MensagensUtil.messageSource = messageSource;
	}
	
	public static String recuperarMensagem(String mensagem, Object ... parametros) {
		return messageSource.getMessage(mensagem, parametros, locale);
	}
}