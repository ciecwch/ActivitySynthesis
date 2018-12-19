package cise.io;

import cise.graphcomponent.Vertex;
import cise.utils.StringType;
import java.util.Map;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.VertexProvider;

public class DOTVertexProvider implements VertexProvider<Vertex> {

    /**
     * 节点的属性映射</br>
     * <ul>
     *     <li>string s -> id</li>
     *     <li>label -> name</li>
     *     <li>type -> type</li>
     * </ul>
     */
    public Vertex buildVertex(String s, Map<String, Attribute> map) {



        StringType type = map.containsKey("type") ? new StringType(map.get("type").getValue()) : StringType.defult();
        String name = map.containsKey("label") ? map.get("label").getValue() : "";
        System.out.println(s);
        return new Vertex(type, s, name, null);
    }
}
