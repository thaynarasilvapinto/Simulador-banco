package com.github.thaynarasilvapinto.simuladorbanco.controller.request

import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Min

data class OperacaoSaqueRequest(
        @NotNull
        @Min(1)
        val valorOperacao: Double?
)
