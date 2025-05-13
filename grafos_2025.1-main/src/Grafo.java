import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class Grafo {
    private boolean isDirecionado;
    private List<Vertice> vertices;
    private List<Aresta> arestas;
    private int ordem;
    private int tamanho;

    // Cache para acesso rápido às arestas
    private Map<VerticesPar, Aresta> arestaMap;

    /**
     * Classe utilitária para pares de vértices
     */
    private static class VerticesPar {
        private final Vertice v1;
        private final Vertice v2;

        public VerticesPar(Vertice v1, Vertice v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VerticesPar that = (VerticesPar) o;
            return Objects.equals(v1, that.v1) && Objects.equals(v2, that.v2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1, v2);
        }
    }

    /**
     * Inicializa o mapa de arestas para acesso rápido
     */
    private void inicializarArestaMap() {
        arestaMap = new HashMap<>();

        for (Aresta aresta : arestas) {
            arestaMap.put(new VerticesPar(aresta.getOrigem(), aresta.getDestino()), aresta);

            // Para grafos não direcionados, adiciona também no sentido contrário
            if (!isDirecionado) {
                arestaMap.put(new VerticesPar(aresta.getDestino(), aresta.getOrigem()), aresta);
            }
        }
    }

    /**
     * Retorna a aresta entre dois vértices, se existir
     */
    private Optional<Aresta> getAresta(Vertice origem, Vertice destino) {
        if (arestaMap == null) {
            inicializarArestaMap();
        }

        return Optional.ofNullable(arestaMap.get(new VerticesPar(origem, destino)));
    }

    /**
     * NOVA ADIÇÃO: Verifica se existe um caminho simples do vértice de origem até o vértice de destino.
     */
    public boolean existeCaminhoSimples(Vertice origem, Vertice destino) {
        Set<Vertice> visitados = new HashSet<>();
        return dfsExisteCaminho(origem, destino, visitados);
    }

    /**
     * Método auxiliar DFS otimizado
     */
    private boolean dfsExisteCaminho(Vertice atual, Vertice destino, Set<Vertice> visitados) {
        // Caso base: encontrou o destino
        if (atual.equals(destino)) {
            return true;
        }

        visitados.add(atual);

        // Usa streams para processar adjacências de forma mais concisa
        return atual.getAdjacencias().stream()
                .filter(vizinho -> !visitados.contains(vizinho))
                .anyMatch(vizinho -> dfsExisteCaminho(vizinho, destino, visitados));
    }

    /**
     * NOVA ADIÇÃO: Calcula o comprimento do caminho entre dois vértices.
     */
    public double calcularComprimentoCaminho(Vertice origem, Vertice destino, boolean ponderado) {
        List<Vertice> caminho = encontrarCaminho(origem, destino);

        if (caminho.isEmpty()) {
            return -1;
        }

        // Para grafos não ponderados, retorna o número de arestas
        if (!ponderado) {
            return caminho.size() - 1;
        }

        // Para grafos ponderados
        double comprimento = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            Optional<Aresta> aresta = getAresta(caminho.get(i), caminho.get(i + 1));

            if (aresta.isPresent()) {
                comprimento += aresta.get().getPeso();
            }
        }

        return comprimento;
    }

    /**
     * Método auxiliar otimizado para encontrar um caminho entre dois vértices
     */
    private List<Vertice> encontrarCaminho(Vertice origem, Vertice destino) {
        // BFS para encontrar o caminho mais curto em número de arestas
        Map<Vertice, Vertice> predecessores = new HashMap<>();
        Queue<Vertice> fila = new LinkedList<>();
        Set<Vertice> visitados = new HashSet<>();

        fila.add(origem);
        visitados.add(origem);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (atual.equals(destino)) {
                break;
            }

            for (Vertice vizinho : atual.getAdjacencias()) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        // Reconstruir o caminho
        LinkedList<Vertice> caminho = new LinkedList<>();
        Vertice atual = destino;

        while (atual != null) {
            caminho.addFirst(atual);
            atual = predecessores.get(atual);
        }

        // Verifica se o caminho começa com a origem
        return caminho.size() > 0 && caminho.getFirst().equals(origem) ? caminho : new LinkedList<>();
    }

    /**
     * NOVA ADIÇÃO: Identificar o caminho de menor comprimento usando Dijkstra
     */
    public List<Vertice> encontrarCaminhoMinimo(Vertice origem, Vertice destino) {
        // Inicializa estruturas de dados para o algoritmo de Dijkstra
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> predecessores = new HashMap<>();
        PriorityQueue<Vertice> filaPrioridade = new PriorityQueue<>(
                Comparator.comparingDouble(v -> distancias.getOrDefault(v, Double.POSITIVE_INFINITY))
        );

        // Inicializar distâncias
        vertices.forEach(v -> distancias.put(v, Double.POSITIVE_INFINITY));
        distancias.put(origem, 0.0);

        filaPrioridade.add(origem);

        // Executa Dijkstra
        while (!filaPrioridade.isEmpty()) {
            Vertice atual = filaPrioridade.poll();

            // Se chegou ao destino
            if (atual.equals(destino)) {
                break;
            }

            // Se a distância for infinita, não há mais caminhos
            if (distancias.get(atual) == Double.POSITIVE_INFINITY) {
                break;
            }

            // Relaxamento das arestas
            for (Vertice vizinho : atual.getAdjacencias()) {
                Optional<Aresta> arestaOpt = getAresta(atual, vizinho);

                if (arestaOpt.isPresent()) {
                    double peso = arestaOpt.get().getPeso();
                    double novaDistancia = distancias.get(atual) + peso;

                    if (novaDistancia < distancias.get(vizinho)) {
                        // Atualiza distância
                        distancias.put(vizinho, novaDistancia);
                        predecessores.put(vizinho, atual);

                        // Atualiza a fila
                        filaPrioridade.remove(vizinho);
                        filaPrioridade.add(vizinho);
                    }
                }
            }
        }

        // Reconstrói o caminho
        return reconstruirCaminho(origem, destino, predecessores);
    }

    /**
     * Reconstrói o caminho a partir do mapa de predecessores
     */
    private List<Vertice> reconstruirCaminho(Vertice origem, Vertice destino, Map<Vertice, Vertice> predecessores) {
        LinkedList<Vertice> caminho = new LinkedList<>();

        // Se o destino não foi alcançado
        if (!predecessores.containsKey(destino) && !origem.equals(destino)) {
            return caminho;
        }

        // Reconstrói o caminho
        for (Vertice atual = destino; atual != null; atual = predecessores.get(atual)) {
            caminho.addFirst(atual);
        }

        return caminho;
    }

    /**
     * Método para manter as adjacências atualizadas
     */
    public void preencherAdjacencias() {
        // Limpa adjacências existentes
        for (Vertice v : vertices) {
            v.setAdjacencias(new ArrayList<>());
        }

        // Preenche novas adjacências
        for (Aresta aresta : arestas) {
            aresta.getOrigem().getAdjacencias().add(aresta.getDestino());

            // Para grafos não direcionados, adiciona também no sentido contrário
            if (!isDirecionado) {
                aresta.getDestino().getAdjacencias().add(aresta.getOrigem());
            }
        }

        // Inicializa o mapa de arestas
        inicializarArestaMap();
    }
}