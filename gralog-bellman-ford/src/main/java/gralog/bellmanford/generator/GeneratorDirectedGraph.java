package main.java.gralog.bellmanford.generator;

import gralog.algorithm.AlgorithmParameters;
import gralog.generator.Generator;
import gralog.generator.GeneratorDescription;
import gralog.structure.DirectedGraph;
import gralog.structure.Edge;
import gralog.structure.Structure;
import gralog.structure.Vertex;


@GeneratorDescription(
        name = "Bellman Ford Algorithm For Directed Graph",
        text = "Construct a directed graph for the Bellman Ford algorithm",
        url = "https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm")
public class GeneratorDirectedGraph extends Generator {

    @Override
    public Structure generate(AlgorithmParameters algorithmParameters) throws Exception {
        DirectedGraph result = new DirectedGraph();
        // init vertex
        Vertex vertexA = result.addVertex();
        vertexA.setId(1);
        vertexA.setLabel("a");
        vertexA.setCoordinates(1,0);
        Vertex vertexB = result.addVertex();
        vertexB.setId(2);
        vertexB.setLabel("b");
        vertexB.setCoordinates(1,5);
        Vertex vertexC = result.addVertex();
        vertexC.setId(3);
        vertexC.setLabel("c");
        vertexC.setCoordinates(1,10);
        Vertex vertexD = result.addVertex();
        vertexD.setId(4);
        vertexD.setLabel("d");
        vertexD.setCoordinates(5,4);
        Vertex vertexE = result.addVertex();
        vertexE.setId(5);
        vertexE.setLabel("e");
        vertexE.setCoordinates(5,6);
        Vertex vertexT = result.addVertex();
        vertexT.setId(0);
        vertexT.setLabel("t");
        vertexT.setCoordinates(12,5);

        // init edge
        result.addEdge(createEdgeForInit(0,"-4",vertexA,vertexB));
        result.addEdge(createEdgeForInit(1,"-3",vertexA,vertexT));
        result.addEdge(createEdgeForInit(2,"-1",vertexB, vertexD));
        result.addEdge(createEdgeForInit(3,"-2",vertexB, vertexE));
        result.addEdge(createEdgeForInit(4,"8",vertexC, vertexB));
        result.addEdge(createEdgeForInit(5,"3",vertexC, vertexT));
        result.addEdge(createEdgeForInit(6,"6",vertexD, vertexA));
        result.addEdge(createEdgeForInit(7,"4",vertexD, vertexT));
        result.addEdge(createEdgeForInit(8,"-3",vertexE, vertexC));
        result.addEdge(createEdgeForInit(9,"2",vertexE, vertexT));

        return result;
    }

    /**
     *  Create Edges
     * @param id       Edges id
     * @param label    Edges label
     * @param source   start vertex
     * @param target   target vertex
     * @return Edge
     */
    public Edge createEdgeForInit(int id, String label, Vertex source, Vertex target){
        Edge edge = new Edge();
        edge.setId(id);
        edge.setLabel(label);
        edge.setSource(source);
        edge.setTarget(target);
        edge.weight = Double.parseDouble(label);
        return edge;
    }
}
