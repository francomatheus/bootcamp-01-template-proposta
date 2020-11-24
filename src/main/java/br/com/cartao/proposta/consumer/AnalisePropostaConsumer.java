package br.com.cartao.proposta.consumer;

import br.com.cartao.proposta.domain.request.AnalisePropostaRequest;
import br.com.cartao.proposta.domain.response.AnalisePropostaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analise", url = "${feign.url-analise}")
public interface AnalisePropostaConsumer {

    @RequestMapping(method = RequestMethod.POST, path = "/api/solicitacao" )
    AnalisePropostaResponse avaliacaoFinanceira(AnalisePropostaRequest analisePropostaRequest);
}
