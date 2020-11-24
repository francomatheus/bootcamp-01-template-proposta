package br.com.cartao.proposta.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class CpfOuCnpjValidatorTest {

    CpfOuCnpjValidator cpfOuCnpjValidator;

    @BeforeEach
    public void declarando(){
        cpfOuCnpjValidator = new CpfOuCnpjValidator();
    }

    @DisplayName("testa falso para cpf ou cnpj invalido")
    @ParameterizedTest
    @ValueSource(strings = {"12345678911", "32152896542","6582141230945276"})
    public void testeFalsoCpfOuCnpj(String value){

        boolean cpfOuCnpjValidatorInvalido = cpfOuCnpjValidator.isValid(value, null);
        Assertions.assertFalse(cpfOuCnpjValidatorInvalido,"Cpf ou Cnpj falso");
    }

    @ParameterizedTest
    @ValueSource(strings = {"649.870.980-40"})
    public void cpfValido(String value){
        boolean cpfValido = cpfOuCnpjValidator.isValid(value, null);
        Assertions.assertTrue(cpfValido);
    }

    @ParameterizedTest
    @ValueSource(strings = {"59.269.816/0001-68"})
    public void cnpjValido(String value){
        boolean cnpjValido = cpfOuCnpjValidator.isValid(value, null);
        Assertions.assertTrue(cnpjValido);
    }


}
