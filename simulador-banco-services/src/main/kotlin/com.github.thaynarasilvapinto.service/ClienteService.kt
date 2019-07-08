package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.repository.CustomerRepository
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.CpfIsValidException
import org.springframework.stereotype.Service

@Service
open class ClienteService (
    private val repo: CustomerRepository,
    private val repoAccount: AccountRepository
) {


    fun find(id: String): Cliente? {
        return repo.findById(id)
    }

    fun insert(cliente: Cliente): Cliente? {
        repo.save(cliente)
        return repo.findById(cliente.id)
    }

    fun update(cliente: Cliente): Cliente? {
        find(cliente.id)
        repo.update(cliente)
        return repo.findById(cliente.id)
    }

    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }

    fun findCPF(CPF: String): Cliente? {
        return repo.findByCpfEquals(CPF)
    }

    fun criarCliente(cliente: Cliente): Cliente? {

        if (findCPF(cliente.cpf) != null) {
            throw CpfIsValidException(message = "O CPF já existe")
        } else {
            val conta = insertToConta(cliente.conta)
            val client = Cliente(id = cliente.id, nome = cliente.nome, cpf = cliente.cpf, conta = conta!!)
            val clienteInserido = insert(client)
            return clienteInserido
        }
    }

    fun cliente(id: String): Cliente? {
        val cliente = find(id)

        if (cliente != null) {
            return cliente
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun insertToConta(account: Account): Account? {
        repoAccount.save(account)
        return repoAccount.findById(account.id)
    }
}