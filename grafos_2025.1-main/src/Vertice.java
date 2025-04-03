import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Vertice {
    private String nome;
    private List<Vertice> adjacencias = new ArrayList<>();
    private int grau; // para grafos não-direcionados
    private int inDegree;
    private int outDegree;

    // NOVA ADIÇÃO: Flag para identificar se o vértice pertence a um grafo direcionado,
    // permitindo exibir inDegree/outDegree quando necessário.
    private boolean isDirecionado;

    public Vertice(String nome) {
        this.nome = nome;
    }

    public void addAdjacencia(Vertice vertice) {
        adjacencias.add(vertice);
    }

    public void incrementaGrau(){
        grau++;
    }

    public void incrementaInDegree(){
        inDegree++;
    }

    public void incrementaOutDegree(){
        outDegree++;
    }

    @Override
    public String toString() {
        StringBuilder adjNames = new StringBuilder();
        // NOVA ADIÇÃO: Percorre a lista de adjacências e exibe apenas os nomes, evitando recursão.
        for (Vertice v : adjacencias) {
            adjNames.append(v.getNome()).append(", ");
        }
        String adjStr = !adjNames.isEmpty() ? adjNames.substring(0, adjNames.length() - 2) : "";

        // NOVA ADIÇÃO: Exibe inDegree/outDegree se o grafo for direcionado; caso contrário, exibe o grau total.
        if (isDirecionado) {
            return nome + " (inDegree: " + inDegree + ", outDegree: " + outDegree + ", adjacencias: [" + adjStr + "])";
        } else {
            return nome + " (grau: " + grau + ", adjacencias: [" + adjStr + "])";
        }
    }

    public void setIsDirecionado(boolean isDirecionado) {
    }

    public void setDirecionado(boolean direcionado) {
        isDirecionado = direcionado;
    }
}
