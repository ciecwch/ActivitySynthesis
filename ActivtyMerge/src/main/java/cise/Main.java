package cise;

import cise.graphcomponent.Graph;
import cise.graphcomponent.MergedGraph;
import cise.io.Exporter;
import cise.io.Importer;
import cise.io.WriteObjectToFile;
import cise.mergeinfo.BasicEntropyCalculator;
import cise.mergeinfo.Entropy;
import cise.mergeinfo.GraphsInfo;
import cise.mergeinfo.MergedGraphInfo;
import cise.operator.BasicCrossover;
import cise.operator.BasicMutator;
import cise.operator.Crossover;
import cise.operator.Mutator;
import cise.operator.RouletteSelector;
import cise.operator.Selector;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String argv[]) throws IOException {
        String pathPrefix = "../ActivitySyntheis/Examples/CaseStudy/";
        //String pathPrefix = "E:\\CISE\\Code\\GraphData\\smallgraph";

        List<Graph> graphList = Importer.importDotFileInDir(pathPrefix);

        for (Graph g : graphList) {
            System.out.println(g);
        }

//        System.out.println("测试融合");
//
//        GraphsInfo graphsInfo = new GraphsInfo(graphList);
//
//        MergedGraphInfo mergedGraphInfo = new MergedGraphInfo(graphsInfo);
//
//        mergedGraphInfo.randomMergeGraph();
//
//        System.out.println("Graph1: \n" + mergedGraphInfo.getMergedGraph());
//
//        Mutator mutator = new BasicMutator(1.0);
//        mutator.mutate(mergedGraphInfo);
//
//        System.out.println("Graph1 after mutation: \n" + mergedGraphInfo.getMergedGraph());
//
//        // 测试交叉
//
//        MergedGraphInfo mergedGraphInfo1 = new MergedGraphInfo(graphsInfo);
//        mergedGraphInfo1.randomMergeGraph();
//
//        System.out.println("Graph2: \n" + mergedGraphInfo1.getMergedGraph());
//
//        Crossover crossover = new BasicCrossover();
//        MergedGraphInfo newGraph = crossover.crossover(mergedGraphInfo, mergedGraphInfo1);
//
//        System.out.println("Cossover: \n" + newGraph.getMergedGraph());
//
//        // 测试熵
//        Entropy entropyCalculator = new BasicEntropyCalculator();
//        mergedGraphInfo1.setEntropy(entropyCalculator.calculateEntropy(mergedGraphInfo1));
//        newGraph.setEntropy(entropyCalculator.calculateEntropy(newGraph));
//
//        System.out.println("Entropy: \n" + mergedGraphInfo1.getEntropy() + "\n" + newGraph.getEntropy());

        BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter("../result"+".txt"));


        int GACnt = 2;
        for (int i = 1; i < GACnt; i++) {

            long begin = System.currentTimeMillis();

            GAProcess ga = new GAProcess(100, 0.2,1000, graphList);
            MergedGraph bestResult = ga.Run(i,bufferedWriter);


            long end = System.currentTimeMillis();

            //WriteObjectToFile.writeObjectToFile(bestResult);
            System.out.println( " Finished, Total Time: " + (double) (end - begin) / 1000);

            Exporter.exportDot(bestResult, pathPrefix + "\\result11"+i + ".dot", Double.toString((end - begin) / 1000));

        }
    }
}

class GAProcess {
    public int populationSize;
    public double mutationRate;
    public int maxGeneration;
    public double targetEntropy = 0;
    // 一个个体最多占总种群数量的多大比例
    public double maxDiversityRate = 0.03;
    public GraphsInfo graphsInfo;

    private int currentGeneration = 0;

    private int lastEntropySize = 500;
    private List<Double> lastEntropy = new LinkedList<>();

    List<MergedGraphInfo> population = new ArrayList<>();
    List<MergedGraphInfo> newGeneration = new ArrayList<>();

    public GAProcess(int size, double rate, int maxGeneration, Collection<Graph> graphSet) {
        this.populationSize = size;
        this.mutationRate = rate;
        this.maxGeneration = maxGeneration;
        graphsInfo = new GraphsInfo(graphSet);
    }

    /**
     * 初始化种群
     */
    public void initialize() {
        // 随机生成size个融合图
        for (int i = 0; i < populationSize; i++) {
            MergedGraphInfo individual = new MergedGraphInfo(graphsInfo);
            individual.randomMergeGraph();
            population.add(individual);
        }
    }

    public void calcAllEntropy() {
        Entropy entropyCalculator = new BasicEntropyCalculator();
        population.forEach(mergedGraphInfo -> mergedGraphInfo.setEntropy(entropyCalculator.calculateEntropy(mergedGraphInfo)));
    }

    public void sortByEntropy() {
        population.sort((o1, o2) -> {
            if (o1.getEntropy() < o2.getEntropy()) {
                return -1;
            } else if (o1.getEntropy() > o2.getEntropy()) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    public void selection() {
        // 保留前size个个体
        List<MergedGraphInfo> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            newPopulation.add(population.get(i));
        }
        this.population = newPopulation;

    }


    public List<MergedGraphInfo> roulette(List<MergedGraphInfo> parents) {
        // 选择的个数
        int selectNum = populationSize * 2;
        // 计算熵倒数
        class MGInfoEntropyPair {
            double reciprocalEntropy;
            MergedGraphInfo mergedGraphInfo;
            public MGInfoEntropyPair(MergedGraphInfo mginfo) {
                this.mergedGraphInfo = mginfo;

            }
        }
        return null;
    }

    public void crossover() {
        // 种群随机打乱，之后相邻的两个交叉，子代最后加入种群
        Collections.shuffle(population);
        Crossover crossoveror = new BasicCrossover();
        Set<MergedGraphInfo> children = new HashSet<>();
        for (int i = 0; i+1 < population.size(); i+=2) {
            MergedGraphInfo child = crossoveror.crossover(population.get(i), population.get(i+1));
            children.add(child);
        }
        population.addAll(children);
    }


    public void mutation(double keepBest) {

        calcAllEntropy();
        sortByEntropy();

        int keepNum = (int) (populationSize * keepBest);

        Set<MergedGraphInfo> keptBest = new HashSet<>(population.subList(0, Math.max(keepNum, populationSize)));

        // 种群每个个体按变异设置随机变异
        Mutator mutator = new BasicMutator(mutationRate);
        for (MergedGraphInfo mergedGraphInfo : population) {
            mutator.mutate(mergedGraphInfo);
        }

        population.addAll(keptBest);
    }

    public boolean isFinish() {
        // 最大代数达到
        if (currentGeneration >= maxGeneration)
            return true;

        // 目标熵达到
        if (Math.abs(targetEntropy - population.get(0).getEntropy()) < 1e-6) {
            return true;
        }

        // 收敛
        if (lastEntropy.size() < lastEntropySize)
            return false;
        double maxEntropy = lastEntropy.get(0);
        double minEntropy = lastEntropy.get(0);
        for (Double entropy : lastEntropy) {
            if (entropy > maxEntropy) {
                maxEntropy = entropy;
            }
            if (entropy < minEntropy) {
                minEntropy = entropy;
            }
        }
        if (maxEntropy - minEntropy <= 1e-8) {
            return true;
        }

        return false;
    }

    public void updateLastEntropy() {
        if (lastEntropy.size() >= lastEntropySize) {
            lastEntropy.remove(0);
        }
        lastEntropy.add(population.get(0).getEntropy());
    }


    public int keepDiversity() {
        // 检查种群中的重复个体
        List<MergedGraphInfo> filteredPopulation = new ArrayList<>();

        for (MergedGraphInfo mgInfo : population) {
            int numberInFilteredPopulation = 0;
            int upbound = (int) (population.size() * maxDiversityRate);
            for (MergedGraphInfo savedMGInfo : filteredPopulation) {
                if (MergedGraph.isSame(mgInfo.getMergedGraph(), savedMGInfo.getMergedGraph())) {
                    numberInFilteredPopulation++;
                }
                if (numberInFilteredPopulation >= upbound) {
                    break;
                }
            }
            // 超过上限，则种群中有过多和mginfo一样的融合方案，不再加入
            if (numberInFilteredPopulation < upbound) {
                filteredPopulation.add(mgInfo);
            }
        }

        int removed = population.size() - filteredPopulation.size();

        // 所有个体经过筛选后，补充随机个体
        while (filteredPopulation.size() < populationSize) {
            MergedGraphInfo newMergedGraph = new MergedGraphInfo(graphsInfo);
            newMergedGraph.randomMergeGraph();
            filteredPopulation.add(newMergedGraph);
        }

        // 用新的种群替换原有种群，没有经过熵计算
        population = filteredPopulation;

        return removed;
    }

    public void runRound() {

        //calcAllEntropy();
//        updateLastEntropy();

        // 交叉
        long currentTime = System.currentTimeMillis();

        // 每一轮操作
        // 交叉
        crossover();


        System.out.print("crossover: " + (System.currentTimeMillis() - currentTime) + ", " );
        currentTime = System.currentTimeMillis();
        // 变异
        mutation(0.2);
        // 计算适应度
        System.out.print("mutation: " + (System.currentTimeMillis() - currentTime) + ", " );
        currentTime = System.currentTimeMillis();

        calcAllEntropy();
        System.out.print("entropy: " + (System.currentTimeMillis() - currentTime) + ", " );
        currentTime = System.currentTimeMillis();
        // 排序
        sortByEntropy();
        System.out.print("sort: " + (System.currentTimeMillis() - currentTime) + ", " );
        currentTime = System.currentTimeMillis();
        // 筛选
        selection();
        System.out.print("selection: " + (System.currentTimeMillis() - currentTime) + "\n" );
        currentTime = System.currentTimeMillis();
        // 更新最大适应度
        updateLastEntropy();
        // 更新代数
        currentGeneration++;

        System.out.println("Generation: " + currentGeneration + ", min entropy: " + population.get(0).getEntropy());
    }

    public void runRound2(BufferedWriter bufferedWriter) throws IOException{


        boolean timing = true;
        long currentTime = System.currentTimeMillis();

        //System.out.println(population.size());




        // 计算适应度
        calcAllEntropy();

        sortByEntropy();
        // 保留前100
        selection();

        // 更新最大适应度
        updateLastEntropy();

        currentGeneration++;

        // 种群多样性
        int removed = keepDiversity();
        System.out.println(removed + " Individuals removed");
        calcAllEntropy();
        sortByEntropy();

        System.out.println("Generation: " + currentGeneration + ", min entropy: " + population.get(0).getEntropy() + " Max Entropy: " + population.get(population.size() - 1).getEntropy());

        bufferedWriter.write(currentGeneration+" "+population.get(0).getEntropy()+"\n");
        //System.out.println("write string success!");
        bufferedWriter.flush();
        //bufferedWriter.close();

        if (timing) {
            System.out.print("entropy: " + (System.currentTimeMillis() - currentTime) + ", " );
            currentTime = System.currentTimeMillis();
        }

        // 轮盘赌进行200次选择，得到交叉的候选
        Selector rouletteSeletor = new RouletteSelector();
        List<MergedGraphInfo> selectList = new ArrayList<>(rouletteSeletor.multipleSelect(population, populationSize * 2));

        if (timing) {
            System.out.print("selection: " + (System.currentTimeMillis() - currentTime) + ", " + selectList.size() + " ");
            currentTime = System.currentTimeMillis();
        }

        // 在选出的list中，每两个进行交叉，得到子代
        Collections.shuffle(selectList);
        Crossover crossover = new BasicCrossover();
        for (int i = 0; i+1 < selectList.size(); i += 2) {
            newGeneration.add(crossover.crossover(selectList.get(i), selectList.get(i+1)));
        }

        if (timing) {
            System.out.print("crossover: " + (System.currentTimeMillis() - currentTime) + ", " );
            currentTime = System.currentTimeMillis();
        }

        // 对子代进行变异
        Mutator mutator = new BasicMutator(mutationRate);
        for (MergedGraphInfo mergedGraphInfo : newGeneration) {
            mutator.mutate(mergedGraphInfo);
        }

        // or 对原种群除熵最低的几个之外也进行变异
        Mutator mutator2 = new BasicMutator(mutationRate / 2);
        double keepBest = 0.2;
        for (int i = (int) (populationSize * keepBest); i < population.size(); i++) {
            mutator2.mutate(population.get(i));
        }

        if (timing) {
            System.out.println("mutation: " + (System.currentTimeMillis() - currentTime) + ", " );
            currentTime = System.currentTimeMillis();
        }

        //System.out.println(newGeneration.size());

        // 子代加入种群
        population.addAll(newGeneration);
        newGeneration.clear();




    }


    public MergedGraph Run(int i,BufferedWriter bufferedWriter) throws IOException {
        initialize();
        //BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter("../result"+i+".txt"));
        while (!isFinish()) {
            runRound2(bufferedWriter);

        }

        calcAllEntropy();
        sortByEntropy();
        selection();

        StringBuilder res = new StringBuilder();
        res.append("Finished\n");
        res.append("total generation: " + currentGeneration + "\n");
        res.append(String.format("min entropy: %s\n", population.get(0).getEntropy()));
        //bufferedWriter.write(i+" "+population.get(0).getEntropy()+"\n");
        //System.out.println("write string success!");
        //bufferedWriter.flush();

        res.append("final merged graph: \n" + population.get(0).getMergedGraph());
        System.out.println(res);

        return population.get(0).getMergedGraph();

        //Exporter.exportDot(population.get(0).getMergedGraph(), "E:\\CISE\\毕设\\ProgrammingGrids\\ManualPick\\TempForMerge\\result.dot");
    }
}
