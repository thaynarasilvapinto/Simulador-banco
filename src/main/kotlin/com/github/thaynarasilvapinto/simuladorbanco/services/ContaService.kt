package com.github.thaynarasilvapinto.simuladorbanco.services

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.repositories.ContaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ContaService {

    @Autowired
    private lateinit var repo: ContaRepository

    fun find(id: Int): Optional<Conta>{
        return repo.findById(id)
    }

    fun insert(obj: Conta) = repo.save(obj)

    fun update(obj: Conta): Conta {
        find(obj.id)
        return repo.save(obj)
    }

    fun delete(id: Int) {
        find(id)
        repo.deleteById(id)
    }
}
