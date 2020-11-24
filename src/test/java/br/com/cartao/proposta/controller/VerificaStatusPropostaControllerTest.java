package br.com.cartao.proposta.controller;

import br.com.cartao.proposta.domain.enums.EstadoAnaliseProposta;
import br.com.cartao.proposta.domain.model.Cartao;
import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.response.AnalisePropostaResponse;
import br.com.cartao.proposta.repository.PropostaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class VerificaStatusPropostaControllerTest {


    @Test
    @DisplayName("Deve retornar todos detalhes da proposta pelo idProposta")
    void deveRetornarDetalhesProposta(){

        final String id = "abc";
        PropostaRepository propostaRepository = mock(PropostaRepository.class);
        Proposta proposta = new Proposta("83794884078","teste@gmail.com","Rua governador","Administrador", BigDecimal.valueOf(800));
        Cartao cartao = new Cartao("abcdefg",proposta);
        proposta.adicionaNumeroCartao(cartao);
        proposta.alteraStatusCartaoCriado(Boolean.TRUE);
        AnalisePropostaResponse analisePropostaResponse = new AnalisePropostaResponse("83794884078","Administrador","123456", EstadoAnaliseProposta.SEM_RESTRICAO);
        proposta.adicionaEstadoProposta(analisePropostaResponse);

        VerificaStatusPropostaController verificaStatusPropostaController = new VerificaStatusPropostaController( propostaRepository);

//        when(verificaStatusPropostaController.getEmailUsuarioSolicitante()).thenReturn("jose@exemplo.com");
        when(propostaRepository.findById(id)).thenReturn(Optional.of(proposta));

        ResponseEntity<?> responseEntity = verificaStatusPropostaController.verificaStatusProposta(id);

        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

        Assertions.assertTrue(responseEntity.getBody() != null);

    }

    @Test
    @DisplayName("Não deve retornar todos detalhes da proposta com um idProposta falso")
    void naoDeveRetornarDetalhesProposta_Status404(){
        final String id = "abc";
        PropostaRepository propostaRepository = mock(PropostaRepository.class);

        VerificaStatusPropostaController verificaStatusPropostaController = new VerificaStatusPropostaController( propostaRepository);

        when(propostaRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = verificaStatusPropostaController.verificaStatusProposta(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

}