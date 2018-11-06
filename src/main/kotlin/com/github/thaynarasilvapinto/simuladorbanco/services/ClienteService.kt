package com.github.thaynarasilvapinto.simuladorbanco.services

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.repositories.ClienteRepository
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.CpfIsValidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
open class ClienteService {

    @Autowired
    private lateinit var repo: ClienteRepository


    @Autowired
    private lateinit var serviceConta: ContaService

    fun find(id: Int): Optional<Cliente> {
        return repo.findById(id)
    }

    fun insert(obj: Cliente) = repo.save(obj)

    fun update(cliente: Cliente): Cliente {
        find(cliente.id)
        return repo.save(cliente)
    }

    fun delete(id: Int) {
        find(id)
        repo.deleteById(id)
    }

    fun findCPF(CPF: String): Optional<Cliente> {
        return repo.findByCpfEquals(CPF)
    }

    fun criarCliente(cliente: Cliente): Cliente {

        if (findCPF(cliente.cpf).isPresent) {
            throw CpfIsValidException(message = "O CPF já existe")
        } else {
            val conta = serviceConta.insert(Conta(saldo = 0.00))
            val client = Cliente(nome = cliente.nome, cpf = cliente.cpf, conta = -1)
            val clienteInserido = insert(client)
            return clienteInserido
        }
    }

    fun cliente(id: Int): Cliente {
        val cliente = find(id)

        if (cliente.isPresent) {
            return cliente.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }
}