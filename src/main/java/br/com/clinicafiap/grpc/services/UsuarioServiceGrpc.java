package br.com.clinicafiap.grpc.services;

import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;
import br.com.clinicafiap.grpc.usuario.GetUsuarioRequest;
import br.com.clinicafiap.grpc.usuario.UsuarioResponse;
import br.com.clinicafiap.grpc.usuario.ValidaUsuariosParaAgendamentoResponse;
import br.com.clinicafiap.mappers.UsuarioProtoMapper;
import br.com.clinicafiap.services.UsuarioService;
import br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import br.com.clinicafiap.grpc.usuario.UsuarioServiceGrpc.UsuarioServiceImplBase;
import br.com.clinicafiap.grpc.usuario.TipoPerfil;
import br.com.clinicafiap.grpc.usuario.ErroValidacao;
import br.com.clinicafiap.grpc.usuario.ValidaUsuariosParaAgendamentoRequest;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@GrpcService
public class UsuarioServiceGrpc extends UsuarioServiceImplBase {

    private final UsuarioService usuarioService;

    public UsuarioServiceGrpc(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void getUsuario(GetUsuarioRequest request, StreamObserver<UsuarioResponse> responseObserver) {
        UUID id = UUID.fromString(request.getId());

        UsuarioDtoResponse usuarioDtoResponse = usuarioService.buscarPorId(id);
        UsuarioResponse usuarioResponse = UsuarioProtoMapper.toUsuarioResponse(usuarioDtoResponse);

        responseObserver.onNext(usuarioResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void validaUsuariosParaAgendamento(ValidaUsuariosParaAgendamentoRequest request, StreamObserver<ValidaUsuariosParaAgendamentoResponse> responseObserver) {
        ValidaUsuariosParaAgendamentoResponse.Builder validacao = ValidaUsuariosParaAgendamentoResponse.newBuilder();

        validarUsuario("medico", request.getIdMedico(), TipoPerfil.MEDICO, validacao::addErros)
                .ifPresent(validacao::setMedico);
        validarUsuario("paciente", request.getIdPaciente(), TipoPerfil.PACIENTE, validacao::addErros)
                .ifPresent(validacao::setPaciente);
        validarUsuario("usuario_criacao", request.getIdUsuarioCriacao(), null, validacao::addErros)
                .ifPresent(validacao::setUsuarioCriacao);

        responseObserver.onNext(validacao.build());
        responseObserver.onCompleted();
    }

    private Optional<UsuarioResponse> validarUsuario(
            String campo,
            String idUsuario,
            TipoPerfil perfilObrigatorio,
            Consumer<ErroValidacao> adicionarErro
    ) {
        final UUID id;
        try {
            id = UUID.fromString(idUsuario);
        } catch (IllegalArgumentException e) {
            adicionarErro.accept(ErroValidacao.newBuilder()
                    .setCampo(campo).setCodigo("INVALID_UUID").setMensagem("UUID inválido").build());
            return Optional.empty();
        }

        final UsuarioDtoResponse usuarioDtoResponse;
        try {
            usuarioDtoResponse = usuarioService.buscarPorId(id);
        } catch (UsuarioNaoEncontradoException e) {
            adicionarErro.accept(ErroValidacao.newBuilder()
                    .setCampo(campo).setCodigo("NOT_FOUND").setMensagem("Usuário não encontrado").build());
            return Optional.empty();
        }

        if (!Boolean.TRUE.equals(usuarioDtoResponse.ativo())) {
            adicionarErro.accept(ErroValidacao.newBuilder()
                    .setCampo(campo).setCodigo("INACTIVE").setMensagem("Usuário inativo").build());
        }

        if (perfilObrigatorio != null) {
            TipoPerfil perfilAtual;

            try {
                perfilAtual = UsuarioProtoMapper.toTipoPerfil(usuarioDtoResponse.perfil());
            } catch (Exception e) {
                perfilAtual = TipoPerfil.TIPO_PERFIL_INDEFINIDO;
            }

            if (perfilAtual != perfilObrigatorio){
                adicionarErro.accept(ErroValidacao.newBuilder()
                        .setCampo(campo).setCodigo("INVALID_ROLE").setMensagem("Perfil inválido").build());
            }
        }

        return Optional.of(UsuarioProtoMapper.toUsuarioResponse(usuarioDtoResponse));
    }
}
