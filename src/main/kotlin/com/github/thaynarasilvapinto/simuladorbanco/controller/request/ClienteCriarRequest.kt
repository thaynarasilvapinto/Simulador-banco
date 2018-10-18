package com.github.thaynarasilvapinto.simuladorbanco.controller.request

import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.NotNull

data class ClienteCriarRequest(
        @NotNull
        val nome: String?,
        @NotNull
        @CPF
        val cpf: String?
)