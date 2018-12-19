package cise.io;

import cise.graphcomponent.Edge;
import cise.graphcomponent.Vertex;
import cise.utils.StringType;
import java.util.Map;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.EdgeProvider;

public class DOTEdgeProvider implements EdgeProvider<Vertex, Edge> {

    /**
     * 边属性映射</br> <ul> <li>label -> type</li> </ul>
     */
    public Edge buildEdge(Vertex from, Vertex to, String label, Map<String, Attribute> map) {
        //System.out.println(map.containsKey("label"));
        StringType type = map.containsKey("type") ? new StringType(map.get("type").getValue())
                : StringType.defult();
        String name = map.containsKey("label") ? map.get("label").getValue() : "";
        return new Edge(from, to, type,name, null);
    }
}
