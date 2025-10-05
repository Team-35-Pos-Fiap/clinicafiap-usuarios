package br.com.clinicafiap.mappers;

import br.com.clinicafiap.entities.dto.PerfilDtoResponse;
import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;
import br.com.clinicafiap.grpc.usuario.UsuarioResponse;
import br.com.clinicafiap.grpc.usuario.TipoPerfil;

import java.util.Locale;

public class UsuarioProtoMapper {


    public static TipoPerfil toTipoPerfil(PerfilDtoResponse perfil){
       return switch (perfil.nome().toUpperCase(Locale.ROOT)){
           case "MÉDICO", "MEDICO" -> TipoPerfil.MEDICO;
           case "PACIENTE" -> TipoPerfil.PACIENTE;
           case "ENFERMEIRO" -> TipoPerfil.ENFERMEIRO;
           default -> TipoPerfil.TIPO_PERFIL_INDEFINIDO;
       };
    }

    public static UsuarioResponse toUsuarioResponse(UsuarioDtoResponse usuarioDtoResponse) {
        TipoPerfil tipoPerfil = toTipoPerfil(usuarioDtoResponse.perfil());

        return UsuarioResponse.newBuilder()
                .setId(String.valueOf(usuarioDtoResponse.id()))
                .setNome(usuarioDtoResponse.nome())
                .setEmail(usuarioDtoResponse.email())
                .setPerfil(tipoPerfil)
                .build();
    }
}
