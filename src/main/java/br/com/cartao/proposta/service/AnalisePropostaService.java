package br.com.cartao.proposta.service;

import br.com.cartao.proposta.consumer.AnalisePropostaConsumer;
import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.response.AnalisePropostaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Carga intrínseca máxima permitida - 7
 * Carga intrínseca da classe - 6
 */

@Service
public class AnalisePropostaService {

    private static Logger logger = LoggerFactory.getLogger(AnalisePropostaService.class);

    // +1
    private final AnalisePropostaConsumer analisePropostaConsumer;

    public AnalisePropostaService(AnalisePropostaConsumer analisePropostaConsumer) {
        this.analisePropostaConsumer = analisePropostaConsumer;
    }

    // +2
    public Optional<AnalisePropostaResponse> analisa(Proposta proposta) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // +1
        try{
            AnalisePropostaResponse analisePropostaResponse = analisePropostaConsumer.avaliacaoFinanceira(proposta.toAnalisePropostaRequest());
            logger.info("Proposta enviada para analise financeira. idProposta: {}", proposta.getId());
            return Optional.of(analisePropostaResponse);
        }
        // +1
        catch (FeignException exception){

            //+1
            if (exception.status() == HttpStatus.UNPROCESSABLE_ENTITY.value()){
                logger.info("Proposta com restrição. idProposta: {}", proposta.getId());
                return Optional.of(objectMapper.readValue(exception.contentUTF8(), AnalisePropostaResponse.class));
            }
            logger.error("Erro ao solicitar analise de proposta. idProposta: {}", proposta.getId());

            throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Erro na API externa");
        }

    }

}
