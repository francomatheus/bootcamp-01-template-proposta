package br.com.cartao.proposta.controller;

import br.com.cartao.proposta.domain.model.Proposta;
import br.com.cartao.proposta.domain.response.NovaPropostaResponseDto;
import br.com.cartao.proposta.repository.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Carga intrínseca máxima permitida - 7
 * Carga intrínseca da classe - 4
 */

@RestController
@RequestMapping("/v1/propostas")
public class VerificaStatusPropostaController {
    private static Logger logger = LoggerFactory.getLogger(VerificaStatusPropostaController.class);

    // +1
    private final PropostaRepository propostaRepository;

    public VerificaStatusPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> verificaStatusProposta(@PathVariable String id){
//        String emailUsuarioSolicitante = getEmailUsuarioSolicitante();
//        logger.info("Usuario solicitante: {}", emailUsuarioSolicitante);

        logger.info("Requisição recebida para verificar o status da proposta. idProposta: {}", id);
        // +1
        Optional<Proposta> propostaBuscada  = propostaRepository.findById(id);
        // +1
        if (propostaBuscada.isEmpty()){
            logger.warn("Id proposta não encontrado. idProposta: {}", id);
            return ResponseEntity.notFound().build();
        }
        // +1
//        if (!emailUsuarioSolicitante.equals(propostaBuscada.get().getEmail())){
//            //Assert.isTrue(emailUsuarioSolicitante.equals(propostaBuscada.get().getEmail()), "Usuario não pode verificar proposta que não é dele. ");
//            return ResponseEntity.badRequest().body("Usuario não pode verificar proposta que não é dele!");
//        }

        NovaPropostaResponseDto novaPropostaResponseDto = new NovaPropostaResponseDto(propostaBuscada.get());

        return ResponseEntity.ok(novaPropostaResponseDto);
    }

//    protected String getEmailUsuarioSolicitante() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Jwt principal = (Jwt) authentication.getPrincipal();
//        String emailUsuario = (String) principal.getClaims().get("email");
//        return emailUsuario;
//    }
}
