package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.repository.ClienteRepository
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.CpfIsValidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ClienteService (private var repo: ClienteRepository,
                           private var serviceConta: ContaService) {


    fun find(id: String): Optional<Cliente> {
        return repo.findById(id)
    }

    fun insert(cliente: Cliente): Cliente {
        repo.save(cliente)
        return repo.findById(cliente.id).get()
    }

    fun update(cliente: Cliente): Cliente {
        find(cliente.id)
        repo.update(cliente)
        return repo.findById(cliente.id).get()
    }

    fun delete(id: String) {
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
            val client = Cliente(nome = cliente.nome, cpf = cliente.cpf, conta = conta)
            val clienteInserido = insert(client)
            return clienteInserido
        }
    }

    fun cliente(id: String): Cliente {
        val cliente = find(id)

        if (cliente.isPresent) {
            return cliente.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }
}