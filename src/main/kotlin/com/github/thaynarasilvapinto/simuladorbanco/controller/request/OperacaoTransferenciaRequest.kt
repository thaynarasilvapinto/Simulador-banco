package com.github.thaynarasilvapinto.simuladorbanco.controller.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class OperacaoTransferenciaRequest(
        @NotNull
        @Min(1)
        var valorOperacao: Double?,
        @NotNull
        var contaDestino: Int?
)