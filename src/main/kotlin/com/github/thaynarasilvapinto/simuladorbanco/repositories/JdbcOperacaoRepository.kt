package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.OperacaoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate

open class JdbcOperacaoRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : OperacaoRepository{
    companion object {
        const val TABLE_NAME = "operacao"
        const val ID_COLUMN = "id"
        const val VALOR_OPERACAO_COLUMN = "valor_operacao"
        const val DATA_HORA_OPERACAO_COLUMN = "data_hora_operacao"
        const val TIPO_OPERACAO_COLUMN = "tipo_operacao"
        const val CONTA_ORIGEM_CONLUMN = "conta_id_origem"
        const val CONTA_DESTINO_CONLUMN = "conta_id_destino"
    }
    override fun save(operacao: Operacao): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(operacaoId: Int): Operacao? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(operacao: Operacao): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByContaOrigem(conta: Conta): List<Operacao> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByContaDestinoAndTipoOperacao(conta: Conta, operacao: Operacao.TipoOperacao): List<Operacao> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}