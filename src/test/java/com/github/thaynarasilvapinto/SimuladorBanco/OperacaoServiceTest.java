package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
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
        this.operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 200.00, TipoOperacao.DEPOSITO);

        operacaoService.insert(operacaoDepositoJoao);

        joaoConta.deposito(operacaoDepositoJoao);

        contaService.update(joaoConta);
    }

    @After
    public void delete() {
        clienteService.delete(joao.getId());
        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
        for (int i = 0; i < extrato.size(); i++) {
            operacaoService.delete(extrato.get(i).getIdOperacao());
        }
        contaService.delete(joaoConta.getId());
    }

    private Cliente createClient() {
        joaoConta = new Conta(0.00);
        this.joao = new Cliente("Cliente Test Operacao", "15142542675", null);
        clienteService.insert(joao);
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
        assertEquals(operacaoDepositoJoao.getValorOperacao(), operacaoBuscada.getValorOperacao(), 0.00001);
    }
}
