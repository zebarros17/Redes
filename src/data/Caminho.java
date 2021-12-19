package data;

import java.io.Serializable;


public class Caminho implements Serializable {
    private int id;            
    private Link proxSalto;
    private Link nodoDestino;


    // --- CONSTRUCTOR ---
    public Caminho(int id, Link proxSalto, Link nodoDestino) {
        this.id = id;
        this.proxSalto = proxSalto;
        this.nodoDestino = nodoDestino;
    }


    // --- GETTERS ---
    public int getId() {
        return id;
    }

    public Link getProxSalto() {
        return proxSalto;
    }

    public Link getNodoDestino() {
        return nodoDestino;
    }

    public Boolean proxSaltoIsON() {
        return this.proxSalto.isOn();
    }

    public Boolean nodoDestinoIsON() {
        return this.proxSalto.isOn();
    }


    // --- SETTERS ---
    public void setId(int id) {
        this.id = id;
    }

    public void setProxSalto(Link proxSalto) {
        this.proxSalto = proxSalto;
    }

    public void setNodoDestino(Link nodoDestino) {
        this.nodoDestino = nodoDestino;
    }

    public boolean isVizinho(){
        return this.proxSalto.equals(this.nodoDestino);
    }

    // --- OTHER ---
    public void print() {
        System.out.println("Proximo Salto:");
        this.proxSalto.print();
        System.out.println();
        System.out.println("Destino:");
        this.nodoDestino.print();
    }
}