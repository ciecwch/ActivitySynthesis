package cise.io;
import cise.graphcomponent.MergedVertex;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.AttributeType;

import javax.print.DocFlavor;

/**
 * Created by wangchunhui on 2018/3/19.
 */
//节点属性类定义，存储节点的某一个属性，如<type,"Event">
public class MergedAttribute implements Attribute {
    private String typeName;
    private String value;
    public String getValue()
    {
        return value;
    }
    public void setTypeName(String typeName)
    {
        this.typeName=typeName;
    }
    public void setValue(String value)
    {
        this.value=value;
    }

    public AttributeType getType()
    {
        return AttributeType.STRING;
    }
}
