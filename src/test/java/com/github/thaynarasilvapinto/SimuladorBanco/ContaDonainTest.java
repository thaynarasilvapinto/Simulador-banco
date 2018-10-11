package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ContaDonainTest {

    private Cliente joao;
    private Cliente maria;
    private Conta joaoConta;
    private Conta mariaConta;

    private Operacao operacaoDepositoJoao;
    private Operacao operacaoSaqueJoao;
    private Operacao operacaoTransferencia;

    @Before
    public void setup() {
        mariaConta = new Conta(0.00);
        joaoConta = new Conta(0.00);
        this.joao = new Cliente("João Pedro da Silva", "15142542675", joaoConta);
        this.maria = new Cliente("Maria Abadia de Oliveira", "17708289667", mariaConta);


        this.operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 200.00, TipoOperacao.DEPOSITO);
        this.operacaoSaqueJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.SAQUE);
        this.operacaoTransferencia = new Operacao(joaoConta, mariaConta, 100, TipoOperacao.TRANSFERENCIA);
    }

    @Test
    public void deveRealizarDeposito() {
        joao.getConta().deposito(operacaoDepositoJoao);

        double saldoEsperado = 200.00;
        double saldoAtual = joao.getConta().getSaldo();

        assertEquals(saldoEsperado, saldoAtual, 0.00001);
    }

    @Test
    public void deveRealizarSaque() {
        double saldoEsperado = 100.00;
        double saldoAtual;

        joao.getConta().deposito(operacaoDepositoJoao);
        joao.getConta().saque(operacaoSaqueJoao);

        saldoAtual = joao.getConta().getSaldo();

        assertEquals(saldoEsperado, saldoAtual, 0.00001);
    }

    @Test
    public void naoDeveRealizarSaqueComSaldo0EValorMaiorQueOSaldo() {
        double saldoEsperado = 0.0;
        double saldoAtual;
        Operacao statusDaOperacaoSaque = joao.getConta().saque(operacaoSaqueJoao);
        Operacao statusEsperadoDaOpercaoSaque = null;

        saldoAtual = joao.getConta().getSaldo();
        assertEquals(statusDaOperacaoSaque, statusEsperadoDaOpercaoSaque);
        assertEquals(saldoEsperado, saldoAtual, 0.00001);
    }

    @Test
    public void deveRealizarTransferencia() {
        double saldoEsperadoJoao = 100;
        double saldoEsperadoMaria = 100;
        double saldoAtualJoao;
        double saldoAtualMaria;
        joao.getConta().deposito(operacaoDepositoJoao);
        joao.getConta().Transferencia(maria.getConta(), operacaoTransferencia);
        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(saldoEsperadoJoao, saldoAtualJoao, 0.00001);
        assertEquals(saldoEsperadoMaria, saldoAtualMaria, 0.00001);
    }

    @Test
    public void naoDeveRealizarTransacaoDeValorMaisAltoDoQueOSaldo() {
        Operacao transferencia = new Operacao(joaoConta, mariaConta, 300, TipoOperacao.TRANSFERENCIA);

        double saldoEsperadoJoao = 200;
        double saldoEsperadoMaria = 0;
        double saldoAtualJoao;
        double saldoAtualMaria;

        joao.getConta().deposito(operacaoDepositoJoao);
        joao.getConta().Transferencia(maria.getConta(), transferencia);

        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(saldoEsperadoJoao, saldoAtualJoao, 0.00001);
        assertEquals(saldoEsperadoMaria, saldoAtualMaria, 0.00001);
    }
}
