package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class ContaTest {
    @Test
    public void deveTer0NaConta(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        double saldoEsperado = 0;
        double saldoAtual = joao.getConta().getSaldo();
        assertEquals(saldoEsperado,saldoAtual,0.00001);

    }
    @Test
    public void deveRealizarDeposito(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Operacao operacao = new Operacao(1,1,200.252342, TipoOperacao.DEPOSITO);
        int statusDaOpercao = joao.getConta().deposito(operacao);
        int statusEsperadoDaOpercao = 1;
        double saldoEsperado = 200.252342;
        double saldoAtual = joao.getConta().getSaldo();
        assertEquals(statusDaOpercao,statusEsperadoDaOpercao);
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void deveRealizarSaque(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Operacao operacaoDeposito = new Operacao(1,1,200.25, TipoOperacao.DEPOSITO);
        Operacao operacaoSaque = new Operacao(1,1,100.00, TipoOperacao.SAQUE);
        double saldoEsperado = 100.25;
        double saldoAtual;
        int statusDaOpercaoDeposito =  joao.getConta().deposito(operacaoDeposito);
        int statusEsperadoDaOpercaoDeposito = 1;
        int statusDaOperacaoSaque = joao.getConta().saque(operacaoSaque);
        int statusEsperadoDaOpercaoSaque = 1;
        saldoAtual = joao.getConta().getSaldo();
        assertEquals(statusDaOpercaoDeposito,statusEsperadoDaOpercaoDeposito);
        assertEquals(statusDaOperacaoSaque,statusEsperadoDaOpercaoSaque);
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void naoDeveRealizarSaqueComSaldo0EValorMaiorQueOSaldo(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Operacao operacaoSaque = new Operacao(1,1,100.00, TipoOperacao.SAQUE);
        double saldoEsperado = 0.0;
        double saldoAtual;
        int statusDaOperacaoSaque = joao.getConta().saque(operacaoSaque);
        int statusEsperadoDaOpercaoSaque = 0;
        saldoAtual = joao.getConta().getSaldo();
        assertEquals(statusDaOperacaoSaque,statusEsperadoDaOpercaoSaque);
        assertEquals(saldoEsperado,saldoAtual,0.00001);
    }
    @Test
    public void deveRealizarTransferencia(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        Operacao operacaoDepositoJoao = new Operacao(1,1,600,TipoOperacao.DEPOSITO);
        Operacao operacaoTransferencia = new Operacao(1,2,200,TipoOperacao.TRANSFERENCIA);
        double saldoEsperadoJoao = 400;
        double saldoEsperadoMaria = 200;
        double saldoAtualJoao;
        double saldoAtualMaria;
        int statusDoDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        int statusDaTransferencia = joao.getConta().Transferencia(maria.getConta(),operacaoTransferencia);
        int statusEsperadoDeposito = 1;
        int statusEsperadoTransferencia = 1;
        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(statusDoDeposito,statusEsperadoDeposito);
        assertEquals(statusDaTransferencia,statusEsperadoTransferencia);
        assertEquals(saldoEsperadoJoao,saldoAtualJoao,0.00001);
        assertEquals(saldoEsperadoMaria,saldoAtualMaria,0.00001);
    }
    @Test
    public void naoDeveRealizarTransacaoDeValorMaisAltoDoQueOSaldo(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        Operacao operacaoDepositoJoao = new Operacao(1,1,100,TipoOperacao.DEPOSITO);
        Operacao operacaoTransferencia = new Operacao(1,2,200,TipoOperacao.TRANSFERENCIA);
        double saldoEsperadoJoao = 100;
        double saldoEsperadoMaria = 0;
        double saldoAtualJoao;
        double saldoAtualMaria;
        int statusDoDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        int statusDaTransferencia = joao.getConta().Transferencia(maria.getConta(),operacaoTransferencia);
        int statusEsperadoDeposito = 1;
        int statusEsperadoTransferencia = 0;

        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(statusDoDeposito,statusEsperadoDeposito);
        assertEquals(statusDaTransferencia,statusEsperadoTransferencia);
        assertEquals(saldoEsperadoJoao,saldoAtualJoao,0.00001);
        assertEquals(saldoEsperadoMaria,saldoAtualMaria,0.00001);
    }
    @Test
    public void deveConterExtratoDeTodasAsOperacoes(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        Operacao operacaoDepositoJoao = new Operacao(1,1,600,TipoOperacao.DEPOSITO);
        Operacao operacaoTransferencia = new Operacao(1,2,200,TipoOperacao.TRANSFERENCIA);
        Operacao operacaoSaque = new Operacao(1,1,100.00, TipoOperacao.SAQUE);
        double saldoEsperadoJoao = 300;
        double saldoEsperadoMaria = 200;
        double saldoAtualJoao;
        double saldoAtualMaria;
        int statusDepositoEsperado = 1;
        int statusTransferenciaEsperado  = 1;
        int statusSaqueEsperado = 1;

        ArrayList<Operacao> extratoEsperadoJoao = new ArrayList<>();
        extratoEsperadoJoao.add(operacaoDepositoJoao);
        extratoEsperadoJoao.add(operacaoTransferencia);
        extratoEsperadoJoao.add(operacaoSaque);

        ArrayList<Operacao> extratoEsperadoMaria = new ArrayList<>();
        extratoEsperadoMaria.add(operacaoTransferencia);

        int statusDeposito = joao.getConta().deposito(operacaoDepositoJoao);
        int statusTransferencia = joao.getConta().Transferencia(maria.getConta(),operacaoTransferencia);
        int statusSaque = joao.getConta().saque(operacaoSaque);

        saldoAtualJoao = joao.getConta().getSaldo();
        saldoAtualMaria = maria.getConta().getSaldo();

        assertEquals(statusDeposito,statusDepositoEsperado);
        assertEquals(statusTransferencia,statusTransferenciaEsperado);
        assertEquals(statusSaque,statusSaqueEsperado);
        assertEquals(saldoEsperadoJoao,saldoAtualJoao,0.00001);
        assertEquals(saldoEsperadoMaria,saldoAtualMaria,0.00001);
        assertEquals(joao.getConta().getExtrato().size(),extratoEsperadoJoao.size());
        assertEquals(joao.getConta().getExtrato().get(0),extratoEsperadoJoao.get(0));
        assertEquals(joao.getConta().getExtrato().get(1),extratoEsperadoJoao.get(1));
        assertEquals(joao.getConta().getExtrato().get(2),extratoEsperadoJoao.get(2));
        assertEquals(maria.getConta().getExtrato().size(),extratoEsperadoMaria.size());
        assertEquals(maria.getConta().getExtrato().get(0),extratoEsperadoMaria.get(0));
    }
}
