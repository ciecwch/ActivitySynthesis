package cise.io;

import cise.graphcomponent.MergedEdge;
import cise.graphcomponent.MergedVertex;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.AttributeType;
import org.jgrapht.io.ComponentAttributeProvider;
import cise.graphcomponent.Edge;
import cise.utils.StringType;
import org.jgrapht.io.DefaultAttribute;

import java.util.*;

/**
 * Created by wangchunhui on 2018/3/19.
 */
//用于表示一个融合节点的属性map，如一个融合节点包括一个属性列表<type,shape,color,style="filled">，每个值从MergedVertex中读取
public class MergedAttributeProvider implements ComponentAttributeProvider<MergedVertex> {

    @Override
    public Map<String, Attribute> getComponentAttributes(MergedVertex mergedVertex) {
        Map<String, Attribute> map=new HashMap<>();
        StringType stringType=mergedVertex.getType();
//        switch(stringType.value)
//        {
//            case "C": map.put("shape",new DefaultAttribute<String>("ellipse", AttributeType.STRING));break;
//                default:map.put("shape",new DefaultAttribute<String>("rectangle",AttributeType.STRING));
//        }
        if(stringType.value.equals("C")) {
            map.put("shape", new DefaultAttribute<String>("ellipse", AttributeType.STRING));
        }
        else
        {
                map.put("shape",new DefaultAttribute<String>("rectangle",AttributeType.STRING));
        }
        return map;
    }
}
