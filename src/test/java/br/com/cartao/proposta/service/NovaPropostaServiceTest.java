package br.com.cartao.proposta.service;

import br.com.cartao.proposta.consumer.AnalisePropostaConsumer;
import br.com.cartao.proposta.domain.enums.EstadoAnaliseProposta;
import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.request.NovaPropostaRequest;
import br.com.cartao.proposta.domain.response.AnalisePropostaResponse;
import br.com.cartao.proposta.domain.response.NovaPropostaResponseDto;
import br.com.cartao.proposta.repository.PropostaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class NovaPropostaServiceTest {

    private static NovaPropostaRequest novaPropostaRequest;

    @Autowired
    private AnalisePropostaConsumer analisePropostaConsumer;

    @BeforeEach
    void setUp(){
        novaPropostaRequest = new NovaPropostaRequest("837.948.840-78","teste@gmail.com","Administrador","Rua governador", BigDecimal.valueOf(800));
    }

    @Test
    @DisplayName("Deve criar uma nova proposta")
    void deveCriarNovaProposta() throws JsonProcessingException {
        PropostaRepository propostaRepository = mock(PropostaRepository.class);
        ObjectMapper objectMapper = new ObjectMapper();
        AnalisePropostaService analisePropostaService = new AnalisePropostaService(analisePropostaConsumer);
        NovaPropostaService novaPropostaService = new NovaPropostaService(propostaRepository,analisePropostaService);
        Proposta proposta = novaPropostaRequest.toModel();
        proposta.setId("1");
        AnalisePropostaResponse analisePropostaResponse = new AnalisePropostaResponse(proposta.getDocumento(), proposta.getNome(), proposta.getId(), EstadoAnaliseProposta.SEM_RESTRICAO);

        when(propostaRepository.save(proposta)).thenReturn(any(Proposta.class));

        NovaPropostaResponseDto novaPropostaResponseDto = novaPropostaService.criaNovaProposta(novaPropostaRequest);

        Assertions.assertTrue(novaPropostaResponseDto != null);
        verify(propostaRepository, times(2)).save(any(Proposta.class));
    }

    @Test
    @DisplayName("Não deve atualizar estado proposta")
    void naoDeveAtualizarEstadoProposta() throws JsonProcessingException {
        PropostaRepository propostaRepository = mock(PropostaRepository.class);
        AnalisePropostaService analisePropostaService = mock(AnalisePropostaService.class);
        NovaPropostaService novaPropostaService = new NovaPropostaService(propostaRepository,analisePropostaService);
        Proposta proposta = novaPropostaRequest.toModel();

        when(propostaRepository.save(proposta)).thenReturn(any(Proposta.class));
        when(analisePropostaService.analisa(proposta)).thenReturn(Optional.empty());

        NovaPropostaResponseDto novaPropostaResponseDto = novaPropostaService.criaNovaProposta(novaPropostaRequest);

        Assertions.assertTrue(novaPropostaResponseDto != null);
        verify(propostaRepository, times(1)).save(any(Proposta.class));
    }
}