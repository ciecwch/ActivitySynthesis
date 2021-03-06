package cise.io;

import cise.graphcomponent.MergedEdge;
import cise.graphcomponent.MergedGraph;
import cise.graphcomponent.MergedVertex;
import java.io.File;
import java.io.IOException;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.IntegerComponentNameProvider;

public class Exporter {
    public static void exportDot(MergedGraph mergedGraph, String path, String time) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //DOTExporter<MergedVertex, MergedEdge> dotExporter = new DOTExporter<>(new IntegerComponentNameProvider<>(), new MergedVertexLabelProvider(), new MergedEdgeLabelProvider());
        //DOTExporter<MergedVertex,MergedEdge> dotExporter=new DOTExporter<>(new IntegerComponentNameProvider<>(),new MergedVertexLabelProvider(),new MergedEdgeLabelProvider(),new MergedAttributeProvider(),new MergedEdgeAttributeProvider());
        DOTExporter<MergedVertex,MergedEdge> dotExporter=new DOTExporter<>(new IntegerComponentNameProvider<>(),new MergedVertexLabelProvider(),new MergedEdgeLabelProvider(),new MergedAttributeProvider(),new MergedEdgeAttributeProvider());
        try {
            dotExporter.exportGraph(mergedGraph, file);
            //dotExporter.putGraphAttribute("time",time);
        } catch (ExportException e) {
            e.printStackTrace();
        }
    }
}
