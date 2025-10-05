package br.com.clinicafiap.grpc.exceptions;


import br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import io.grpc.Status;

@GrpcAdvice
public class GrpcServiceExceptionHandler {

    @GrpcExceptionHandler(br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException.class)
    public Status handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e) {
        return Status.NOT_FOUND.withDescription(
                e.getMessage() != null ? e.getMessage() : "Usuário não encontrado"
        );
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleIllegalArgument(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(
                e.getMessage() != null ? e.getMessage() : "Parâmetro inválido"
        );
    }

    @GrpcExceptionHandler(Throwable.class)
    public Status handleGeneric(Throwable e) {
        return Status.INTERNAL.withDescription("Ocorreu um erro interno");
    }
}
