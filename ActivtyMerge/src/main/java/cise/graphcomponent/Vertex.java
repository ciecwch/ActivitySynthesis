package cise.graphcomponent;


import cise.utils.StringType;

import java.io.Serializable;
import java.util.Objects;

/**
 * 节点类
 */
public class Vertex implements Serializable{

    public StringType type;
    public String id;
    public String name;
    // 节点所属的图
    public Graph graph;

    /**
     * 默认构造函数 缺省情况下id为-1
     */
    public Vertex() {
        new Vertex(StringType.defult(), "", "", null);
    }

    public Vertex(StringType type, String id, String name, Graph g) {
        this.type = type;
        this.id = id == null ? "" : id;
        this.name = name;
        this.graph = g;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        Vertex vertex = (Vertex) o;
//
//        if (!Objects.equals(id, vertex.id)) {
//            return false;
//        }
//        if (!type.equals(vertex.type)) {
//            return false;
//        }
//        if (!name.equals(vertex.name)) {
//            return false;
//        }
//        return graph != null ? graph.equals(vertex.graph) : vertex.graph == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = type.hashCode();
//        result = 31 * result + id.hashCode();
//        result = 31 * result + name.hashCode();
//        result = 31 * result + (graph != null ? graph.id : 0);
//        return result;
//    }

    @Override
    public String toString() {
        return this.graph.id + "." + this.id;
    }

//    @Override
//    public int compareTo(Vertex o) {
//        if (o.graph.equals(o.graph)) {
//            return this.id.compareTo(o.id);
//        }
//        return this.graph.compareTo(o.graph);
//    }
}
