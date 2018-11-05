package com.github.thaynarasilvapinto.simuladorbanco.api.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import java.time.LocalDateTime

data class ExtratoResponse(
        val idOperacao: Int,
        val valorOperacao: Double,
        val dataHora: LocalDateTime,
        val tipoOperacao: Operacao.TipoOperacao,
        val contaDestino: Int?
) {
    constructor(operacao: Operacao) : this(
            idOperacao = operacao.idOperacao,
            valorOperacao = operacao.valorOperacao,
            dataHora = operacao.dataHoraOperacao,
            tipoOperacao = operacao.tipoOperacao,
            contaDestino = operacao.contaDestino.id
    )
}