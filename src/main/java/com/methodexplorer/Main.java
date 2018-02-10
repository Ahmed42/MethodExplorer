package com.methodexplorer;

import org.repodriller.RepoDriller;


/**
 * Created by Admin on 2/9/2018.
 */

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        String repoURL = "https://github.com/paul-hammant/qdox.git";
        String reportFilePath = "qdox_report.csv";

        new RepoDriller().start(new RepoMiner(repoURL, reportFilePath));

        long finishTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (finishTime - startTime)/1000 + " s");
    }
}



// https://github.com/mauricioaniche/repodriller.git    ~300 commits
// https://github.com/Motasim/Chat-System.git           22 commits
// https://github.com/alibaba/dubbo.git                 ~2100 commits
// https://github.com/paul-hammant/qdox.git             ~1600 commits   96 seconds
