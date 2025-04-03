import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grafo g = new Grafo();
        Vertice v1 = new Vertice("A");
        Vertice v2 = new Vertice("B");
        Vertice v3 = new Vertice("C");
        Aresta a1 = new Aresta(v2, v1);
        Aresta a2 = new Aresta(v1, v3);
        g.setVertices(List.of(v1, v2, v3));
        g.setArestas(List.of(a1, a2));
        System.out.println(g);
    }
}