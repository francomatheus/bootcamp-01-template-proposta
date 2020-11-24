package br.com.cartao.proposta.service;

import br.com.cartao.proposta.consumer.AnalisePropostaConsumer;
import br.com.cartao.proposta.domain.enums.EstadoAnaliseProposta;
import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.request.AnalisePropostaRequest;
import br.com.cartao.proposta.domain.response.AnalisePropostaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Optional;

import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnalisePropostaServiceTest {

    private ObjectMapper objectMapper;

    private static Proposta propostaElegivel;

    private static Proposta propostaNaoElegivel;

    @Autowired
    private AnalisePropostaConsumer analisePropostaConsumer;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        propostaNaoElegivel = new Proposta("380.817.210-02", "teste@exemplo.com","Endereco","José Maria", BigDecimal.valueOf(1000));
        propostaElegivel = new Proposta("862.490.890-63", "teste@exemplo.com","Endereco","José Maria", BigDecimal.valueOf(1000));

    }

    @Test
    @DisplayName("Deve retornar objeto AnalisePropostaResponse com o Status da Analise sem restricao")
    void deveRetornarStatusDaAnaliseFinanceiraSemRestricao() throws JsonProcessingException {
        // cenario
        AnalisePropostaResponse analisePropostaResponse = new AnalisePropostaResponse(propostaElegivel.getDocumento(), propostaElegivel.getNome(), propostaElegivel.getId(), EstadoAnaliseProposta.SEM_RESTRICAO);

        AnalisePropostaService analisePropostaService = new AnalisePropostaService(analisePropostaConsumer);

        // acao

        Optional<AnalisePropostaResponse> analisa = analisePropostaService.analisa(propostaElegivel);

        // verificação

        Assertions.assertEquals(EstadoAnaliseProposta.SEM_RESTRICAO,analisa.get().getResultadoSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar objeto AnalisePropostaResponse com o Status da Analise com restricao")
    void deveRetornarStatusDaAnaliseFinanceiraComRestricao() throws JsonProcessingException {

        AnalisePropostaRequest analisePropostaRequest = propostaNaoElegivel.toAnalisePropostaRequest();
        AnalisePropostaResponse analisePropostaResponse = new AnalisePropostaResponse(propostaNaoElegivel.getDocumento(), propostaNaoElegivel.getNome(), propostaNaoElegivel.getId(), EstadoAnaliseProposta.COM_RESTRICAO);

        AnalisePropostaService analisePropostaService = new AnalisePropostaService(analisePropostaConsumer);
        Optional<AnalisePropostaResponse> analisa = analisePropostaService.analisa(propostaNaoElegivel);

        FeignException feignException = Assertions.assertThrows(FeignException.class, () -> analisePropostaConsumer.avaliacaoFinanceira(analisePropostaRequest));
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), feignException.status());
        Assertions.assertEquals(EstadoAnaliseProposta.COM_RESTRICAO,analisa.get().getResultadoSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar nenhum objeto AnalisePropostaResponse com o Status da Analise com restricao")
    void deveRetornarNenhumStatusDaAnaliseFinanceira() throws JsonProcessingException {

        Proposta propostaErrada = new Proposta("", "teste@exemplo.com","Endereco","José Maria", BigDecimal.valueOf(1000));
        AnalisePropostaRequest analisePropostaRequest = propostaErrada.toAnalisePropostaRequest();
        AnalisePropostaConsumer analisePropostaConsumer = mock(AnalisePropostaConsumer.class);
        AnalisePropostaService analisePropostaService = new AnalisePropostaService(analisePropostaConsumer);

        Optional<AnalisePropostaResponse> analisa = analisePropostaService.analisa(propostaErrada);

        FeignException feignException = Assertions.assertThrows(FeignException.class, () -> analisePropostaConsumer.avaliacaoFinanceira(analisePropostaRequest));
        Assertions.assertTrue(feignException.status() != HttpStatus.UNPROCESSABLE_ENTITY.value());

    }



}
