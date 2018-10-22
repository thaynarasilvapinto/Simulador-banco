package com.github.thaynarasilvapinto.simuladorbanco.controller.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import java.time.LocalDateTime

data class SaqueResponse(
        val idOperacao: Int,
        val valorOperacao: Double,
        val dataHora: LocalDateTime,
        val tipoOperacao: Operacao.TipoOperacao
) {
    constructor(operacao: Operacao) : this(
            idOperacao = operacao.idOperacao,
            valorOperacao = operacao.valorOperacao,
            dataHora = operacao.dataHoraOperacao,
            tipoOperacao = operacao.tipoOperacao
    )
}