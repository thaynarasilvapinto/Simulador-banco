////package com.github.thaynarasilvapinto.simuladorbanco;
//
//
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.TipoOperacao;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService;
//import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class ContaServiceTest {
//
//    @Autowired
//    private ClienteService clienteService;
//    @Autowired
//    private ContaService contaService;
//    @Autowired
//    private OperacaoService operacaoService;
//    private Cliente joao;
//    private Conta joaoConta;
//
//    @Before
//    public void setup() {
//        createClient();
//    }
//
//    private Cliente createClient() {
//        joaoConta = new Conta(0.00);
//        joao = new Cliente("Cliente Test Conta", "15142542675", null);
//        joao = clienteService.insert(joao);
//        joaoConta = contaService.insert(joaoConta);
//        joao.setConta(joaoConta);
//        joao = clienteService.update(joao);
//        return new Cliente();
//    }
//
//    @After
//    public void delete() {
//        clienteService.delete(joao.getId());
//        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
//        for (int i = 0; i < extrato.size(); i++) {
//            operacaoService.delete(extrato.get(i).getIdOperacao());
//        }
//        contaService.delete(joaoConta.getId());
//    }
//    @Test
//    public void buscar() {
//        Conta contaBuscado = contaService.find(joaoConta.getId());
//        assertEquals(joaoConta.getId(), contaBuscado.getId());
//    }
//
//    @Test
//    public void update() {
//        joaoConta.setSaldo(100);
//        contaService.update(joaoConta);
//        Conta contaBuscada = contaService.find(joaoConta.getId());
//        assertEquals(joaoConta.getSaldo(), contaBuscada.getSaldo(), 0.00001);
//    }
//
//    @Test
//    public void deveBuscarSaldoDoCliente() {
//        Conta contaBuscado = contaService.find(joaoConta.getId());
//        assertEquals(joaoConta.getSaldo(), contaBuscado.getSaldo(), 0.00001);
//
//    }
//
//    @Test
//    public void deveBuscarOExtratoDeUmCliente() {
//
//        Operacao operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.DEPOSITO);
//        Operacao operacaoSaqueJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.SAQUE);
//
//        Operacao statusDaOpercaoDeposito = joao.getConta().deposito(operacaoDepositoJoao);
//        Operacao statusDaOperacaoSaque = joao.getConta().saque(operacaoSaqueJoao);
//
//        operacaoService.insert(operacaoDepositoJoao);
//        operacaoService.insert(operacaoSaqueJoao);
//
//        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
//
//        assertEquals(statusDaOpercaoDeposito.getIdOperacao(), extrato.get(0).getIdOperacao());
//        assertEquals(statusDaOperacaoSaque.getIdOperacao(), extrato.get(1).getIdOperacao());
//    }
//}
