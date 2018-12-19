package cise.checkMergeResult;

import cise.graphcomponent.Graph;
import cise.graphcomponent.MergedGraph;
import cise.graphcomponent.MergedVertex;
import cise.graphcomponent.Vertex;
import cise.io.Importer;
import cise.io.ReadObjectFromFile;
import cise.io.WriteObjectToFile;
import cise.io.WriteStringToFile;
import cise.utils.StringType;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 * Created by wangchunhui on 2017/12/17.
 */
public class CheckMergeResult {
    public static void main(String[] args)
    {
        MergedGraph mtest= (MergedGraph) ReadObjectFromFile.readObjectFromFile();
        EdgeConflictCheck edgeConflictCheck=new EdgeConflictCheck(mtest);
        edgeConflictCheck.getOutEdgeClassify();
        //System.out.println(edgeConflictCheck);
        edgeConflictCheck.checkEdgeConflict();
        StringBuffer s=edgeConflictCheck.getConflictMessage();
        System.out.println(s);
        WriteStringToFile.writeStringToFile(String.valueOf(s));
    }
}
