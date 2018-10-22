package com.github.thaynarasilvapinto.simuladorbanco.controller.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

class OperacaoRequest(
        @get: NotNull
        @get: Min(1)
        val valorOperacao: Double?,
        val contaDestino: Int?)