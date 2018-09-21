package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import java.util.ArrayList;

public class Banco {
    ArrayList<Cliente> clientes = new ArrayList<Cliente>();

    public  Banco(){
    }
    public Banco(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }
    public void add(Cliente cliente){
            cliente.setId(clientes.size());
            cliente.getConta().setId(clientes.size());
            clientes.add(cliente);
    }
    public Cliente find(Integer id){
        return clientes.get(id);
    }
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
    public Conta findConta(Integer id){
        return clientes.get(id).getConta();
    }
    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

}
