import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
public class Grafo {
    private boolean isDirecionado;
    private List<Vertice> vertices;
    private List<Aresta> arestas;
    private int ordem;
    private int tamanho;

    public Grafo(boolean isDirecionado) {
        this.isDirecionado = isDirecionado;
    }

    public void setVertices(List<Vertice> vertices) {
        this.vertices = vertices;
        ordem = vertices.size();
        // NOVA ADIÇÃO: Atualiza o flag isDirecionado em cada vértice
        for (Vertice v : vertices) {
            v.setIsDirecionado(isDirecionado);
        }
    }

    public void setArestas(List<Aresta> arestas) {
        this.arestas = arestas;
        tamanho = arestas.size();
        // Se o grafo não é direcionado, verifica se deve ser convertido para direcionado
        if (!isDirecionado) {
            verificaDirecionado();
        }
        preencherAdjacencias();
        // NOVA ADIÇÃO: Atualiza novamente o flag dos vértices após definir as arestas
        for (Vertice v : vertices) {
            v.setIsDirecionado(isDirecionado);
        }
    }

    // NOVA ADIÇÃO: Metodo renomeado para preencherAdjacencias (corrigindo a nomenclatura)
    public void preencherAdjacencias() {
        for (Aresta aresta : arestas) {
            Vertice origem = aresta.getOrigem();
            Vertice destino = aresta.getDestino();
            avaliarGrausDosVertices(origem, destino);

            if (isDirecionado) {
                // Para grafo direcionado, adiciona o destino como adjacente do origem
                origem.addAdjacencia(destino);
            } else {
                // Para grafo não-direcionado, os vértices são adjacentes mutuamente
                origem.addAdjacencia(destino);
                destino.addAdjacencia(origem);
            }
        }
    }

    private void avaliarGrausDosVertices(Vertice origem, Vertice destino) {
        if (isDirecionado) {
            origem.incrementaOutDegree();
            destino.incrementaInDegree();
        }
        // Em ambos os casos incrementa o grau
        origem.incrementaGrau();
        destino.incrementaGrau();
    }

    public void verificaDirecionado() {
        for (int i = 0; i < arestas.size(); i++) {
            Aresta arestaI = arestas.get(i);
            if (isSelfLoop(arestaI)) {
                defineComoDirecionado();
                return;
            }
            for (int j = 0; j < arestas.size(); j++) {
                if (i != j) {
                    Aresta arestaJ = arestas.get(j);
                    if (isViaMaoDupla(arestaI, arestaJ)) {
                        defineComoDirecionado();
                        return;
                    } else if (isMesmoSentido(arestaI, arestaJ)) {
                        defineComoDirecionado();
                        return;
                    }
                }
            }
        }
    }

    private boolean isMesmoSentido(Aresta i, Aresta j) {
        return i.getOrigem() == j.getOrigem() && i.getDestino() == j.getDestino();
    }

    private void defineComoDirecionado() {
        isDirecionado = true;
    }

    private boolean isViaMaoDupla(Aresta arestaAlvo, Aresta arestaInterna) {
        return arestaAlvo.getOrigem() == arestaInterna.getDestino()
                && arestaAlvo.getDestino() == arestaInterna.getOrigem();
    }

    private boolean isSelfLoop(Aresta arestaAlvo) {
        return arestaAlvo.getOrigem() == arestaAlvo.getDestino();
    }

    @Override
    public String toString() {
        // NOVA ADIÇÃO: Metodo toString() reformulado para exibir de forma organizada as informações do grafo
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo:\n");
        sb.append("Direcionado: ").append(isDirecionado).append("\n");
        sb.append("Ordem: ").append(ordem).append("\n");
        sb.append("Tamanho: ").append(tamanho).append("\n");
        sb.append("Vertices:\n");
        for (Vertice v : vertices) {
            sb.append(v.toString()).append("\n");
        }
        sb.append("Arestas:\n");
        for (Aresta a : arestas) {
            sb.append(a.toString()).append("\n");
        }
        return sb.toString();
    }
}
