package com.github.thaynarasilvapinto.simuladorbanco.controller.request

import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.NotNull

data class ClienteCriarRequest(
        @get: NotNull
        val nome: String?,
        @get: NotNull
        @get: CPF
        val cpf: String?
)