package br.com.cartao.proposta.controller;

import br.com.cartao.proposta.domain.model.Cartao;
import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.request.AvisoViagemRequest;
import br.com.cartao.proposta.domain.request.InformacaoRede;
import br.com.cartao.proposta.service.AvisoViagemService;
import br.com.cartao.proposta.service.ColetaInformacaoRedeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class AvisoViagemControllerTest {

    private static String idCartao = "1";
    private static InformacaoRede informacaoRede;
    private static AvisoViagemRequest avisoViagemRequest;
    private static AvisoViagemController avisoViagemController;
    private static EntityManager manager;
    private static ColetaInformacaoRedeService informacaoRedeService;
    private static AvisoViagemService avisoViagemService;
    private static HttpServletRequest httpServletRequest;

    @BeforeEach
    void setup(){
        manager = mock(EntityManager.class);
        informacaoRedeService = mock(ColetaInformacaoRedeService.class);
        avisoViagemService = mock(AvisoViagemService.class);
        httpServletRequest = mock(HttpServletRequest.class);
        informacaoRede = new InformacaoRede("Monzilla", "Localhost");
        avisoViagemRequest = new AvisoViagemRequest("Lugar indefinido", LocalDate.of(2020,11,10),"","");
        avisoViagemController = new AvisoViagemController(manager, informacaoRedeService, avisoViagemService);
    }

    @Test
    @DisplayName("Deve criar aviso sobre viagem com sucesso e retornar 201")
    void deveAvisarViagemComSucesso() throws JsonProcessingException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        Proposta proposta = new Proposta("documento","email","endereço","nome",new BigDecimal(1000));
        Cartao cartao = new Cartao("1",proposta);

        when(informacaoRedeService.getInformacaoRede(httpServletRequest)).thenReturn(informacaoRede);
        when(manager.find(Cartao.class,"1")).thenReturn(cartao);

        ResponseEntity<?> responseEntity = avisoViagemController.avisoViagem(idCartao, avisoViagemRequest, httpServletRequest, uriComponentsBuilder);

        Assertions.assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

    }

    @Test
    @DisplayName("Não deve achar cartão e retornar 404")
    void naoDeveAvisarViagemComSucessoERetornar_404() throws JsonProcessingException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        when(manager.find(Cartao.class,"1")).thenReturn(null);

        ResponseEntity<?> responseEntity = avisoViagemController.avisoViagem(idCartao, avisoViagemRequest, httpServletRequest, uriComponentsBuilder);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());

    }

}