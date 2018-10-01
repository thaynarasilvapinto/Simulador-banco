package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ContaTest {


    private Cliente joao;
    private Cliente maria;
    private Conta joaoConta;
    private Conta mariaConta;

    private Operacao operacaoDepositoJoao;
    private Operacao operacaoSaqueJoao;
    private Operacao operacaoTransferencia;

    @Before
    public void criaUmAmbiente() {
        mariaConta = new Conta(0.00);
        joaoConta = new Conta(0.00);
        this.joao = new Cliente("João Pedro da Silva", "151.425.426-75",joaoConta);
        this.maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67",mariaConta);
        this.operacaoDepositoJoao = new Operacao(0,0,200.00, TipoOperacao.DEPOSITO);
        this.operacaoSaqueJoao = new Operacao(0,0,100.00, TipoOperacao.SAQUE);
        this.operacaoTransferencia = new Operacao(0,1,100,TipoOperacao.TRANSFERENCIA);
    }


    @Test
    public void deveTer0NaConta(){
        double saldoEsperado = 0;
        double saldoAtual = joao.getConta().getSaldo();
        assertEquals(saldoEsperado,saldoAtual,0.00001);

    }
    @Test
    public void deveRealizarDeposito(){
        Operacao statusDaOperacao = joao.getConta().deposito(operacaoDepositoJoao);
        Integer idEsperado = 0;

        double saldoEsperado = 200.00;
        double saldoAtual = joao.getConta().getSaldo();

        assertEquals(idEsperado,statusDaOperacao.getIdOperacao());
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void deveRealizarSaque(){
        double saldoEsperado = 100.00;
        double saldoAtual;

        Operacao statusDaOpercaoDeposito =  joao.getConta().deposito(operacaoDepositoJoao);
        Integer idEsperadoDaOpercaoDeposito = 0;
        Operacao statusDaOperacaoSaque = joao.getConta().saque(operacaoSaqueJoao);
        Integer idEsperadoDaOpercaoSaque = 1;

        saldoAtual = joao.getConta().getSaldo();
        assertEquals(idEsperadoDaOpercaoDeposito,statusDaOpercaoDeposito.getIdOperacao());
        assertEquals(idEsperadoDaOpercaoSaque,statusDaOperacaoSaque.getIdOperacao());
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void naoDeveRealizarSaqueComSaldo0EValorMaiorQueOSaldo(){
        double saldoEsperado = 0.0;
        double saldoAtual;
        Operacao statusDaOperacaoSaque = joao.getConta().saque(operacaoSaqueJoao);
        Operacao statusEsperadoDaOpercaoSaque = null;

        saldoAtual = joao.getConta().getSaldo();
        assertEquals(statusDaOperacaoSaque,statusEsperadoDaOpercaoSaque);
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void deveRealizarTransferencia(){
        double saldoEsperadoJoao = 100;
        double saldoEsperadoMaria = 100;
        double saldoAtualJoao;
        double saldoAtualMaria;
        Operacao statusDoDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        Operacao statusDaTransferencia = joao.getConta().Transferencia(maria.getConta(),operacaoTransferencia);
        Integer statusEsperadoDeposito = 0;
        Integer statusEsperadoTransferencia = 1;
        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(statusEsperadoDeposito,statusDoDeposito.getIdOperacao());
        assertEquals(statusEsperadoTransferencia,statusDaTransferencia.getIdOperacao());
        assertEquals(saldoEsperadoJoao,saldoAtualJoao,0.00001);
        assertEquals(saldoEsperadoMaria,saldoAtualMaria,0.00001);
    }
    @Test
    public void naoDeveRealizarTransacaoDeValorMaisAltoDoQueOSaldo(){
        Operacao transferencia = new Operacao(0,1,300,TipoOperacao.TRANSFERENCIA);
        double saldoEsperadoJoao = 200;
        double saldoEsperadoMaria = 0;
        double saldoAtualJoao;
        double saldoAtualMaria;
        Operacao statusDoDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        Operacao statusDaTransferencia = joao.getConta().Transferencia(maria.getConta(),transferencia);
        Integer statusEsperadoDeposito = 0;
        Operacao statusEsperadoTransferencia = null;

        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(statusEsperadoDeposito,statusDoDeposito.getIdOperacao());
        assertEquals(statusEsperadoTransferencia,statusDaTransferencia);
        assertEquals(saldoEsperadoJoao,saldoAtualJoao,0.00001);
        assertEquals(saldoEsperadoMaria,saldoAtualMaria,0.00001);
    }
    @Test
    public void naoDeveTerIdsIguaisParaOpercoesDeMesmaConta(){
        Integer statusDepositoEsperado = 0;
        Integer statusTransferenciaEsperado  = 1;
        Integer statusSaqueEsperado = 2;

        Operacao statusDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        Operacao statusTransferencia = joao.getConta().Transferencia(maria.getConta(),operacaoTransferencia);
        Operacao statusSaque = joao.getConta().saque(operacaoSaqueJoao);

        assertEquals(statusDepositoEsperado,statusDeposito.getIdOperacao());
        assertEquals(statusTransferenciaEsperado,statusTransferencia.getIdOperacao());
        assertEquals(statusSaqueEsperado,statusSaque.getIdOperacao());

        Integer esperado = 0;
        assertEquals(esperado,joao.getConta().getExtrato().get(0).getIdOperacao());
        esperado = 1;
        assertEquals(esperado,joao.getConta().getExtrato().get(1).getIdOperacao());
        esperado = 2;
        assertEquals(esperado,joao.getConta().getExtrato().get(2).getIdOperacao());
        esperado = 0;
        assertEquals(esperado,maria.getConta().getExtrato().get(0).getIdOperacao());
    }
}
