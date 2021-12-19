package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;


public class Caminhos implements Serializable{
    private List<Caminho> caminhos;


    // --- CONSTRUCTOR ---
    public Caminhos() {
        this.caminhos = new ArrayList<>();
    }


    // --- GETTERS ---
    public List<Caminho> getCaminhos() {
        return caminhos;
    }

    public Caminho getCaminho(int idDestino){
        for(Caminho c: this.caminhos)
            if(c.getId() == idDestino)
                return c;
        return null;
    }

    public List<Link> getVizinhos(){
        List<Link> res = new ArrayList<>();
        for(Caminho c: caminhos)
            if(c.isVizinho())
                res.add(c.getNodoDestino());

        return res;
    }


    // --- SETTERS ---
    public void setCaminhos(ArrayList<Caminho> caminhos) {
        this.caminhos = caminhos;
    }

    public void clear() {
        this.caminhos.clear();
    }

    public void addAll(Caminhos caminhos) {
        this.caminhos.addAll(caminhos.getCaminhos());
    }

    public void addCalculaCaminho(int nodoInicial, int nodoDestino, HashMap<Integer, Integer> parents, Underlay table) {
        if(parents.containsKey(nodoDestino)){
            int idProxSalto = calculaProxSalto(nodoInicial, nodoDestino, parents);
            Link proxNodo = table.getLinks(nodoInicial).getLink(idProxSalto);
            int idParentNodoFinal = parents.get(nodoDestino);
            Link nodoFinal = table.getLinks(idParentNodoFinal).getLink(nodoDestino);
            Caminho caminho = new Caminho(nodoDestino, proxNodo, nodoFinal);
            this.caminhos.add(caminho);
        }
    }


    // --- OTHER ---
    public static Caminhos calculaCaminhos(int nodoInicial, Underlay table) {
        HashMap<Integer, Integer> parents = table.caminhoMaisCurto(nodoInicial);
        Set<Integer> nodosAVisitar = new HashSet<>(table.getNodosAtivos());
        Caminhos caminhos = new Caminhos();
        nodosAVisitar.remove(nodoInicial);

        for(Integer id : nodosAVisitar) caminhos.addCalculaCaminho(nodoInicial, id, parents, table);

        return caminhos;
    }

    private Integer calculaProxSalto(int nodoInicial, int nodoDestino, HashMap<Integer, Integer> parents) {
        int res, proximo = nodoDestino;
        do {
            res = proximo;
            proximo = parents.get(res);
        }
        while (proximo != nodoInicial);
        return res;
    }

    public void print() {
        for (Caminho caminho : this.caminhos) {
            caminho.print();
            System.out.println("---------------");
        }
    }
}