package cise.mergeinfo;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.NGram;

/**
 * Created by wangchunhui on 2017/12/24.
 */
public class Similarity {
    private static ILexicalDatabase db = new NictWordNet();
    public static double[][] getSimilarityMatrix( String[] words1, String[] words2, RelatednessCalculator rc )
    {
        double[][] result = new double[words1.length][words2.length];
        for ( int i=0; i<words1.length; i++ ){
            for ( int j=0; j<words2.length; j++ ) {
                double score = rc.calcRelatednessOfWords(words1[i], words2[j]);
                if(score==1.7976931348623157E308D)
                    score=1.0;
//                else
//                    if(score>1.0)
//                        score=0.98;
                result[i][j] = score;
            }
        }
        return result;
    }
    public static double similarity (String str1, String str2) {
        double s = 0.0, max = 0.0;
        //System.out.println("WuPalmer");
        RelatednessCalculator rc1 = new WuPalmer(db);
        String[] words1=str1.split(" ");String[] words2=str2.split(" ");
        if(words1.length>words2.length)
        {
            String[] word=words1;
            words1=words2;
            words2=word;
        }
        {

            double[][] s1 = getSimilarityMatrix(words1, words2, rc1);
            for (int i = 0; i < words1.length; i++) {
                max = 0.0;
                for (int j = 0; j < words2.length; j++) {
                    if (max < s1[i][j]) {
                        max = s1[i][j];
                    }
                    //System.out.print(s1[i][j] + "\t");
                }
                //System.out.println("max=" + max);
                s = s + max;
            }
            for (int j = 0; j < words2.length; j++) {
                max = 0.0;
                for (int i = 0; i < words1.length; i++) {
                    if (max < s1[i][j]) {
                        max = s1[i][j];
                    }
                    //System.out.print(s1[i][j] + "\t");
                }
                //System.out.println("max=" + max);
                s = s + max;
            }

        }
        //System.out.println();
        //System.out.println();
        return ( s) / (words1.length + words2.length);
    }
    //计算两个字符串的Ngram相似度
    public static double NgramSimilarity(String str1,String str2)
    {
        double max=0.0,score,s=0.0;
        String[] words1=str1.split(" ");
        String[] words2=str2.split(" ");
        if(words1.length>words2.length)
        {
            String[] word=words1;
            words1=words2;
            words2=word;
        }
        //构建匹配矩阵
        double[][] s1 = new double[words1.length][words2.length];
        for ( int i=0; i<words1.length; i++ ) {
            for (int j = 0; j < words2.length; j++) {
                    score = new NGram().distance(words1[i], words2[j]);
                    s1[i][j] = 1.0-score;
            }
        }

            for (int i = 0; i < words1.length; i++) {
                max = 0.0;
                for (int j = 0; j < words2.length; j++) {
                    if (max < s1[i][j]) {
                        max = s1[i][j];
                    }
                    //System.out.print(s1[i][j] + "\t");
                }
                //System.out.println("max=" + max);
                s = s + max;
            }
            for (int j = 0; j < words2.length; j++) {
                max = 0.0;
                for (int i = 0; i < words1.length; i++) {
                    if (max < s1[i][j]) {
                        max = s1[i][j];
                    }
                    //System.out.print(s1[i][j] + "\t");
                }
                //System.out.println("max=" + max);
                s = s + max;
            }

//        //System.out.println();
//        //System.out.println();
        return ( s) / (words1.length + words2.length);
    }
    public static double totalSimialrity(String str1,String str2,double d)
    {
        double s1,s2;
        s1=similarity(str1,str2);
        s2=NgramSimilarity(str1,str2);
        return d*s1+(1-d)*s2;
    }
    public static void main(String[] args)
    {
        //System.out.println(NgramSimilarity("display yes","display no"));
        //System.out.println(new NGram().distance("ok","display"));
        System.out.println(totalSimialrity("System display ok","System display no",1.0));
    }
}
