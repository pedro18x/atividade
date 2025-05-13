import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Criar um grafo
        Grafo g = new Grafo();
        g.setDirecionado(true); // Corrigido: setDirecionado em vez de setIsDirecionado

        // Criar vértices
        Vertice v1 = new Vertice("A");
        Vertice v2 = new Vertice("B");
        Vertice v3 = new Vertice("C");
        Vertice v4 = new Vertice("D");

        // Criar arestas (com pesos para demonstrar grafo ponderado)
        Aresta a1 = new Aresta(v1, v2, 2.0);
        Aresta a2 = new Aresta(v2, v3, 3.0);
        Aresta a3 = new Aresta(v1, v3, 7.0);
        Aresta a4 = new Aresta(v3, v4, 1.0);

        // Configurar o grafo
        g.setVertices(List.of(v1, v2, v3, v4));
        g.setArestas(List.of(a1, a2, a3, a4));
        g.preencherAdjacencias();

        // Testes
        System.out.println("Existe caminho A->D: " + g.existeCaminhoSimples(v1, v4));

        System.out.println("Comprimento (não ponderado) A->D: " +
                g.calcularComprimentoCaminho(v1, v4, false));

        System.out.println("Comprimento (ponderado) A->D: " +
                g.calcularComprimentoCaminho(v1, v4, true));

        List<Vertice> caminhoMinimo = g.encontrarCaminhoMinimo(v1, v4);
        System.out.println("Caminho mínimo: " +
                caminhoMinimo.stream().map(Vertice::getNome).reduce("", (a, b) -> a + (a.isEmpty() ? "" : "->") + b));
    }
}