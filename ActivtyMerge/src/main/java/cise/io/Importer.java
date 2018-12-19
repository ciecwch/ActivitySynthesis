package cise.io;

import cise.graphcomponent.Edge;
import cise.graphcomponent.Graph;
import cise.graphcomponent.Vertex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.GraphImporter;
import org.jgrapht.io.ImportException;

public class Importer {

    public static Graph importDot(String path){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.out.println("未找到文件: " + path);
        }

        GraphImporter<Vertex, Edge> dotImporter = new DOTImporter<>(
            new DOTVertexProvider(), new DOTEdgeProvider());
        Graph inputGraph = new Graph();
        try {
            dotImporter.importGraph(inputGraph, fileReader);
        } catch (ImportException e) {
            e.printStackTrace();
        }
        inputGraph.vertexSet().forEach(vertex -> vertex.graph = inputGraph);
        inputGraph.edgeSet().forEach(edge -> edge.graph = inputGraph);
        return inputGraph;
    }

    public static List<Graph> importDotFileInDir(String path) {

        List<Graph> retGraphs = new ArrayList<>();

        File dir = new File(path);
        if (dir.exists()) {
            File[] fileList = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".gv") || name.endsWith(".dot");
                }
            });
            for (File file : fileList) {
                if (file.isFile())
                    retGraphs.add(importDot(file.getAbsolutePath()));
            }
        }

        return retGraphs;
    }

    public static List<Graph> importDotFileList(String pathPrefix, int num) {
        List<Graph> retGraphs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            retGraphs.add(importDot(pathPrefix + i + ".gv"));
        }
        return retGraphs;
    }
}


