package cise.io;

import cise.graphcomponent.MergedEdge;
import org.jgrapht.io.ComponentNameProvider;
import cise.graphcomponent.Edge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
public class MergedEdgeLabelProvider implements ComponentNameProvider<MergedEdge> {

    @Override
    public String getName(MergedEdge mergedEdge) {
        //return mergedEdge.type.value+"["+mergedEdge.getEdgeSet().size()+"]";
        StringBuffer s=new StringBuffer("");
        Map<String,Set<Edge>> mergedEdgeSet=new HashMap<>();
        for(Edge edge:mergedEdge.getEdgeSet())
        {
            if(!mergedEdgeSet.containsKey(edge.name))
            {
                mergedEdgeSet.put(edge.name, new HashSet<>());
            }
            mergedEdgeSet.get(edge.name).add(edge);
        }
        for(String string:mergedEdgeSet.keySet()) {
            if(!string.equals(""))
                s.append(string + ",");
        }

        return s.toString();
    }
}
