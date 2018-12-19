package cise.checkMergeResult;

import cise.graphcomponent.MergedEdge;
import cise.graphcomponent.MergedGraph;
import cise.graphcomponent.MergedVertex;
import cise.utils.StringType;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.*;

/**
 * Created by wangchunhui on 2017/12/19.
 */
class MergedVertexInfo
{
    private Map<MergedVertex,Set<StringType>> sameTargetDiffType;
    private Map<StringType,Set<MergedVertex>> sameTypeDiffTarget;
    MergedGraph mergedGraph;
    MergedVertex mergedVertex;
    public MergedVertexInfo(MergedVertex mergedVertex)
    {
        this.mergedVertex=mergedVertex;
        sameTargetDiffType=new HashMap<>();
        sameTypeDiffTarget=new HashMap<>();
    }
    public void setSameTargetDiffType(Map<MergedVertex,Set<StringType>> sameTargetDiffType)
    {
        this.sameTargetDiffType=sameTargetDiffType;
    }
    public void setSameTypeDiffTarget(Map<StringType,Set<MergedVertex>> sameTypeDiffTarget)
    {
        this.sameTypeDiffTarget=sameTypeDiffTarget;
    }
    public Map<MergedVertex,Set<StringType>> getSameTargetDiffType()
    {
        return sameTargetDiffType;
    }
    public Map<StringType,Set<MergedVertex>> getSameTypeDiffTarget()
    {
        return sameTypeDiffTarget;
    }
    public MergedVertex getMergedVertex()
    {
        return mergedVertex;
    }
    public String toString()
    {
        StringBuffer stringBuffer=new StringBuffer("");
        stringBuffer.append(mergedVertex);
        stringBuffer.append("\n"+sameTargetDiffType);
        stringBuffer.append("\n"+sameTypeDiffTarget);
        return String.valueOf(stringBuffer);
    }

}

public class EdgeConflictCheck {
    //具有同源的边有不同的节点,返回冲突信息。
    private StringBuffer conflictMessage;
    private MergedGraph mergedGraph;
    private Set<MergedVertexInfo> mergedVertexInfos;
    public EdgeConflictCheck(MergedGraph mergedGraph)
    {
        this.mergedGraph=mergedGraph;
        conflictMessage=new StringBuffer("");
        mergedVertexInfos=new HashSet<>();
    }


    public void getOutEdgeClassify()
    {
        Map<MergedVertex,Set<StringType>> sameTargetDiffType;
        Map<StringType,Set<MergedVertex>> sameTypeDiffTarget;

        //对图进行深度优先搜索，把每个节点要检查的边分类分别放在sameTypeDiffTarget和sameTargetDiffType中

        DepthFirstIterator depthFirstIterator=new DepthFirstIterator(mergedGraph);
        while(depthFirstIterator.hasNext())
        {
            MergedVertex mergedVertex=(MergedVertex)depthFirstIterator.next();
            MergedVertexInfo mergedVertexInfo=new MergedVertexInfo(mergedVertex);
            Set<MergedEdge> outEdges=mergedGraph.outgoingEdgesOf(mergedVertex);
            sameTargetDiffType=new HashMap<>();
            sameTypeDiffTarget=new HashMap<>();
            for(MergedEdge outedge: outEdges)
            {
                Set<StringType> s1;
                MergedVertex mv=outedge.getTarget();
                if(sameTargetDiffType.containsKey(mv))
                {
                    s1=sameTargetDiffType.get(mv);
                    s1.add(outedge.type);
                    sameTargetDiffType.put(mv,s1);
                }
                else
                {
                    s1=new HashSet<>();
                    s1.add(outedge.type);
                    sameTargetDiffType.put(mv,s1);
                }
                StringType type=outedge.type;
                Set<MergedVertex> mvSet;
                if(sameTypeDiffTarget.containsKey(type))
                {
                    mvSet=sameTypeDiffTarget.get(type);
                    mvSet.add(outedge.getTarget());
                    sameTypeDiffTarget.put(type,mvSet);
                }
                else
                {
                    mvSet=new HashSet<>();
                    mvSet.add(outedge.getTarget());
                    sameTypeDiffTarget.put(type,mvSet);
                }
            }
            mergedVertexInfo.setSameTargetDiffType(sameTargetDiffType);
            mergedVertexInfo.setSameTypeDiffTarget(sameTypeDiffTarget);
            mergedVertexInfos.add(mergedVertexInfo);
        }
    }
    //在两个边分类map中检查冲突
    public void checkEdgeConflict()
    {
        //检查每个mergedVertexInfo的边冲突情况
        conflictMessage.append("the source vertex, the target vertex, the conflict type\n");
        for(MergedVertexInfo mergedVertexInfo:mergedVertexInfos) {
            Map<MergedVertex, Set<StringType>> sameTargetDiffType = mergedVertexInfo.getSameTargetDiffType();
            Iterator iterator1 = sameTargetDiffType.entrySet().iterator();
            //共同的源和目标有不同的边类型
            while (iterator1.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator1.next();
                MergedVertex mergedVertex = (MergedVertex) entry.getKey();
                Set<StringType> stringTypes = (Set<StringType>) entry.getValue();
                if (stringTypes.size() > 1) {
                    conflictMessage.append(mergedVertexInfo.getMergedVertex() + ",");
                    conflictMessage.append(mergedVertex + ",");
                    conflictMessage.append(stringTypes + "\n");
                }
            }
        }
        conflictMessage.append("the edge type, the source vertex, the conflict target vertex\n");
        for(MergedVertexInfo mergedVertexInfo:mergedVertexInfos) {
            Map<StringType, Set<MergedVertex>> sameTypeDiffTarget = mergedVertexInfo.getSameTypeDiffTarget();
            Iterator iterator2 = sameTypeDiffTarget.entrySet().iterator();
            //共同边类型,不同目标节点
            while(iterator2.hasNext())
            {
                Map.Entry entry=(Map.Entry) iterator2.next();
                StringType stringType=(StringType)entry.getKey();
                Set<MergedVertex> setMergedVertex=(Set<MergedVertex>)entry.getValue();
                if(setMergedVertex.size()>1)
                {
                    conflictMessage.append(stringType+",");
                    conflictMessage.append(mergedVertexInfo.getMergedVertex()+",");
                    conflictMessage.append(setMergedVertex+"\n");

                }
            }
        }

    }
    public StringBuffer getConflictMessage() {
        return conflictMessage;
    }
    public String toString()
    {
        StringBuffer stringBuffer=new StringBuffer("");
        for(MergedVertexInfo mergedVertexInfo:mergedVertexInfos)
        {
            stringBuffer.append(mergedVertexInfo+",");
        }
        return String.valueOf(stringBuffer);
    }

}
