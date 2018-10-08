package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class OperacaoServiceTest {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContaService contaService;
    @Autowired
    private OperacaoService operacaoService;
    private Cliente joao;
    private Conta joaoConta;
    private Operacao operacaoDepositoJoao;

    @Before
    public void setup() {
        createClient();

        this.operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.SAQUE);
        operacaoDepositoJoao = operacaoService.insert(operacaoDepositoJoao);

        this.joaoConta.deposito(operacaoDepositoJoao);

        contaService.update(joaoConta);
    }

    private Cliente createClient() {
        joaoConta = new Conta(0.00);
        joao = new Cliente("Cliente Test Operacao", "151.425.426-75", null);
        joao = clienteService.insert(joao);
        contaService.insert(joaoConta);
        joao.setConta(joaoConta);
        clienteService.update(joao);
        return new Cliente();
    }
    @Test
    public void salvar() {
        Operacao contaOperacao = operacaoService.find(operacaoDepositoJoao.getIdOperacao());
        assertEquals(operacaoDepositoJoao.getIdOperacao(), contaOperacao.getIdOperacao());
    }

    @Test
    public void buscar() {
        Operacao contaOperacao = operacaoService.find(operacaoDepositoJoao.getIdOperacao());
        assertEquals(operacaoDepositoJoao.getIdOperacao(), contaOperacao.getIdOperacao());
    }

    @Test
    public void update() {
        operacaoDepositoJoao.setValorOperacao(50);
        operacaoService.update(operacaoDepositoJoao);
        Operacao operacaoBuscada = operacaoService.find(operacaoDepositoJoao.getIdOperacao());
        assertEquals(operacaoDepositoJoao.getValorOperacao(), operacaoBuscada.getValorOperacao(),0.00001);
    }
}
