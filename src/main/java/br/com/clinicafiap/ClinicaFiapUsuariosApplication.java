package br.com.clinicafiap;

import net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {GrpcServerSecurityAutoConfiguration.class})
public class ClinicaFiapUsuariosApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClinicaFiapUsuariosApplication.class, args);
	}
}