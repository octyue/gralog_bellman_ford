package main.java.gralog.bellmanford.algorithm;

import gralog.algorithm.*;
import gralog.progresshandler.ProgressHandler;
import gralog.rendering.GralogColor;
import gralog.structure.Edge;
import gralog.structure.Structure;
import gralog.structure.Vertex;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.gralog.bellmanford.entity.ParameterVerificationEntity;

import java.util.*;

@AlgorithmDescription(
        name = "Bellman-Ford-Algorithm",
        text = "Constructs a Bellman-Ford-Algorithm",
        url = "https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm"
)
public class BellmanFordAlgorithm extends Algorithm {

    private final int MAX_VALUE = Integer.MAX_VALUE;
    private final String PATH_NULL = "INF";
    private int colorFlag = 0;

    public Object run(Structure s, AlgorithmParameters p, Set<Object> selection,
                      ProgressHandler onprogress) throws Exception {

        Vertex selectVertex = selectedUniqueVertex(selection);
        ParameterVerification  parameterVerification = new ParameterVerification();
        ParameterVerificationEntity entity = parameterVerification.incomingParameterVerification(s,selectVertex);
        if (entity.getVerificationResults()) {
            Vertex allVertexTarget = selectedUniqueVertex(selection);
            // begin algorithm
            int vertexNum = s.getVertices().size();
            String[][] pathwayList = new String[vertexNum][vertexNum];
            int[][] weightList = new int[vertexNum][vertexNum];
            for (int i=0;i<vertexNum;i++) {
                for (int j=0;j<vertexNum;j++){
                    if (i==0) {
                        weightList[i][j] = 0;
                        pathwayList[i][j] = String.valueOf(allVertexTarget.getId());
                    } else {
                        weightList[i][j] = MAX_VALUE;
                        pathwayList[i][j] = PATH_NULL;
                    }
                }
            }
            for (int i=1;i<vertexNum;i++){
                for (int w=1;w<vertexNum;w++){
                    if (MAX_VALUE == weightList[w][i] && MAX_VALUE != weightList[w][i-1]) {
                        weightList[w][i] = weightList[w][i-1];
                    }
                }

                for (Edge e : (Set<Edge>) s.getEdges()) {
                    int targetVertex = e.getTarget().id;
                    int sourceVertex = e.getSource().id;
                    int edgeWeight = e.weight.intValue();
                    if ((weightList[targetVertex][i] != MAX_VALUE) && (weightList[sourceVertex][i-1] > weightList[targetVertex][i-1]+ edgeWeight)) {
                        String tempPathway = sourceVertex +","+pathwayList[targetVertex][i-1];
                        String pureTempPathway = tempPathway.replace(",","");
                        int commasNumber = tempPathway.length() - pureTempPathway.length();
                        int infNumber = pureTempPathway.length() - pureTempPathway.replace(PATH_NULL,"").length();
                        if ((commasNumber+1 == i+1) && (infNumber == 0)){
                            pathwayList[sourceVertex][i] = tempPathway;
                            weightList[sourceVertex][i] = weightList[targetVertex][i-1]+ edgeWeight;
                        }
                    }
                }

                for (int k=1;k<vertexNum;k++) {
                    if (PATH_NULL.equals(pathwayList[k][i]) && ! PATH_NULL.equals(pathwayList[k][i-1])) {
                        pathwayList[k][i] = pathwayList[k][i-1];
                    }
                }
            }
            //end algorithm
            // start javafx
            showResult(s,pathwayList,weightList);
            // end
            return null;
        } else {
            return entity.getErrorMessage();
        }
    }

    public void showResult(Structure s, String[][] pathwayList, int[][] weightList) {
        Platform.runLater(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Algorithm Results");
            TableView<String[]> tableView = new TableView<>();
            for (int i=0;i<s.getVertices().size()+1;i++){
                TableColumn<String[], String> column;
                if (i==0) {
                    column = new TableColumn<>("");
                } else{
                    column = new TableColumn<>(""+(i-1));
                }
                final int rowIndex = i;
                column.setId(i+"");
                column.setCellValueFactory(cellDataFeatures -> {
                    String[] rowData = cellDataFeatures.getValue();
                    return new SimpleStringProperty(String.valueOf(rowData[rowIndex]));
                });
                tableView.getColumns().add(column);
            }
            double v = 300.00 / tableView.getColumns().size();
            for (TableColumn<String[],?> column : tableView.getColumns()) {
                column.setPrefWidth(v);
            }
            Collection<Vertex> V = s.getVertices();
            String[][] weightResultShow = new String[s.getVertices().size()][s.getVertices().size()+1];
            for (Vertex vLab: V) {
                weightResultShow[vLab.getId()][0] = vLab.label;
            }
            for (int i =0;i<s.getVertices().size();i++){
                for (int j =1;j<s.getVertices().size()+1;j++){
                    if (weightList[i][j-1] == MAX_VALUE){
                        weightResultShow[i][j] = "∞";
                    } else {
                        weightResultShow[i][j] = String.valueOf(weightList[i][j-1]);
                    }
                }
            }
            tableView.getItems().addAll(weightResultShow);
            colorFlag = 0;
            Label sentenceLabel = new Label();
            sentenceLabel.setText("Current number of relaxation operations："+colorFlag);
            // previous button
            Button showPreviousButton = new Button("Previous");
            showPreviousButton.setOnAction(event -> {
                if (colorFlag > 1) {
                    initVertexColour(V);
                    initEdgeColour(s);
                    for (int i=1;i<s.getVertices().size();i++){
                        boolean edgeFlag = false;
                        // deal vertex colour
                        if (weightList[i][colorFlag-1] != weightList[i][colorFlag-2]) {
                            modifyVertexColour(i, V);
                            edgeFlag = true;
                        }
                        // deal edge colour
                        if (edgeFlag) {
                            modifyEdgeColour(pathwayList[i][colorFlag-1],s);
                        }
                    }
                    colorFlag = colorFlag -1;
                    sentenceLabel.setText("Current number of relaxation operations："+colorFlag);
                } else if (colorFlag == 1){
                    initVertexColour(V);
                    initEdgeColour(s);
                    colorFlag = colorFlag -1;
                    sentenceLabel.setText("Current number of relaxation operations："+colorFlag);
                }
            });
            // next button
            Button showNextButton = new Button("Next");
            showNextButton.setOnAction(event -> {
                if (colorFlag!= s.getVertices().size()-1) {
                    initVertexColour(V);
                    initEdgeColour(s);
                    for (int i=1;i<s.getVertices().size();i++){
                        boolean edgeFlag = false;
                        // deal vertex colour
                        if (weightList[i][colorFlag] != weightList[i][colorFlag+1]) {
                            modifyVertexColour(i, V);
                            edgeFlag = true;
                        }
                        // deal edge colour
                        if (edgeFlag) {
                            modifyEdgeColour(pathwayList[i][colorFlag+1],s);
                        }
                    }
                    colorFlag = colorFlag + 1;
                    sentenceLabel.setText("Current number of relaxation operations："+colorFlag);
                }
            });
            HBox buttonsContainer = new HBox(showPreviousButton,showNextButton);
            buttonsContainer.setSpacing(10);
            VBox root = new VBox(tableView,sentenceLabel, buttonsContainer);
            root.setSpacing(10);
            root.setPadding(new javafx.geometry.Insets(10));
            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    private void initVertexColour(Collection<Vertex> vertices){
        for (Vertex vertex:vertices){
            vertex.fillColor = GralogColor.WHITE;
        }
    }
    private void modifyVertexColour( int vertexNumber, Collection<Vertex> vertices){
        for (Vertex vertex:vertices){
            if (vertexNumber == vertex.getId()) {
                vertex.fillColor = GralogColor.PURPLE;
            }
        }
    }

    private void initEdgeColour(Structure s){
        for (Edge e : (Set<Edge>) s.getEdges()) {
            e.color = GralogColor.BLACK;
        }
    }

    private void modifyEdgeColour(String path, Structure s){
        String[] vertexList = path.split(",");
        for (int i=0;i<vertexList.length-1;i++){
            for (Edge e : (Set<Edge>) s.getEdges()) {
                if ((e.getSource().id == Integer.parseInt(vertexList[i])) && (e.getTarget().id == Integer.parseInt(vertexList[i+1]))) {
                    e.color = GralogColor.RED;
                }
            }
        }
    }
}
