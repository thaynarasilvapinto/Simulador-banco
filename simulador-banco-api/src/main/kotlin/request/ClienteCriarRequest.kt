package com.github.thaynarasilvapinto.api.request

import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.NotNull

data class ClienteCriarRequest(
    @get: NotNull
    @get: NotEmpty
    val nome: String?,
    @get: NotNull
    @get: CPF
    val cpf: String?
)