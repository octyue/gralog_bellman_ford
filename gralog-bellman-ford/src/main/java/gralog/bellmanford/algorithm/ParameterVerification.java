package main.java.gralog.bellmanford.algorithm;

import gralog.structure.Edge;
import gralog.structure.Structure;
import gralog.structure.Vertex;
import main.java.gralog.bellmanford.entity.ParameterVerificationEntity;

import java.util.Collection;
import java.util.Set;

public class ParameterVerification {

    public ParameterVerificationEntity incomingParameterVerification(Structure s, Vertex selectVertex){
        ParameterVerificationEntity para = new ParameterVerificationEntity();
        para.setVerificationResults(true);
        // vertices check
        checkVertex(para, s, selectVertex);
        if (para.getVerificationResults()) {
            // edges check
            checkEdges(para,s);
        }
        if (para.getVerificationResults()) {
            // negative weight cycle check
            checkNegativeCycle(para,s);
        }
        return para;
    }

    public void checkVertex(ParameterVerificationEntity p, Structure s, Vertex selectVertex){
        if (s.getVertices().size() == 0) {
            p.setVerificationResults(false);
            p.setErrorMessage("Your graph needs at least one vertex.");
        } else if(selectVertex == null) {
            p.setVerificationResults(false);
            p.setErrorMessage("Please select the vertex with id 0 as the target vertex.");
        } else if(selectVertex.getId() != 0){
            p.setVerificationResults(false);
            p.setErrorMessage("Please select the vertex with id 0 as the target vertex.");
        } else{
            Collection<Vertex> vertices = s.getVertices();
            boolean checkVertexLabel = true;
            for (Vertex v:vertices){
                if (v.label == "" || v.label == null || v.label.length() == 0){
                    checkVertexLabel = false;
                }
            }
            if (!checkVertexLabel) {
                p.setVerificationResults(false);
                p.setErrorMessage("The label of a vertex is empty.");
            }
        }
    }

    public void checkEdges(ParameterVerificationEntity p, Structure s){
        Set<Edge> edges = s.getEdges();
        if (s.getEdges().size() == 0) {
            p.setVerificationResults(false);
            p.setErrorMessage("Your graph needs at least one edge.");
        } else {
            for (Edge edge:edges){
                // label == weight
                if (edge.label == null || edge.label == "" || edge.label.length() == 0) {
                    p.setVerificationResults(false);
                    p.setErrorMessage("Inconsistent labels and weights on edges.");
                    break;
                }
                if (Double.parseDouble(edge.label) != edge.weight) {
                    p.setVerificationResults(false);
                    p.setErrorMessage("Inconsistent labels and weights on edges.");
                    break;
                }
                if (edge.weight - edge.weight.intValue() != 0) {
                    p.setVerificationResults(false);
                    p.setErrorMessage("Decimal places exist for edge weights.Please enter the decimal places as 0.");
                    break;
                }
                if (!edge.isDirected()){
                    p.setVerificationResults(false);
                    p.setErrorMessage("All edges have to be directed edges");
                    break;
                }
                if (edge.getSource().getId() == 0) {
                    p.setVerificationResults(false);
                    p.setErrorMessage("Existence of edges pointing to other vertices at the target source point.");
                    break;
                }
            }
        }
    }

    public void checkNegativeCycle(ParameterVerificationEntity p, Structure s){
        Set<Edge> edges = s.getEdges();
        int[] distances = new int[s.getVertices().size()];
        for (int i=0;i<s.getVertices().size();i++) {
            distances[i] = Integer.MAX_VALUE ;
        }
        distances[0] = 0;
        for (int i = 1; i < s.getVertices().size(); i++) {
            for (Edge edge : edges) {
                int sourceV = edge.getSource().id;
                int targetV = edge.getTarget().id;
                int weight = edge.weight.intValue();
                if (distances[targetV] != Integer.MAX_VALUE && (distances[sourceV] > distances[targetV] + weight)) {
                    distances[sourceV] = distances[targetV] + weight;
                }
            }
        }
        // check negative cycle
        for (Edge edge : edges) {
            int sourceV = edge.getSource().id;
            int targetV = edge.getTarget().id;
            int weight = edge.weight.intValue();
            if (distances[targetV] != Integer.MAX_VALUE && distances[targetV] + weight < distances[sourceV]) {
                p.setVerificationResults(false);
                p.setErrorMessage("There exists a negative weight cycle for directed graphs.");
                break;
            }
        }
    }
}
