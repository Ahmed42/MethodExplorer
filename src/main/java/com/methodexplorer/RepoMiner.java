package com.methodexplorer;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.commit.OnlyModificationsWithFileTypes;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRemoteRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Admin on 2/10/2018.
 */
public class RepoMiner implements Study {
    private String repoURL;
    private String reportFilePath;

    public RepoMiner(String repoURL, String reportFilePath) {
        this.repoURL = repoURL;
        this.reportFilePath = reportFilePath;
    }

    @Override
    public void execute() {
        try {
            Files.write(Paths.get(reportFilePath), "Commit SHA, Old function signature, New function signature".getBytes());
        } catch (IOException ex) {
            // do nothing ...
        }

        new RepositoryMining()
                .in(GitRemoteRepository.singleProject(repoURL))
                .through(Commits.all())
                .filters(new OnlyModificationsWithFileTypes(Arrays.asList(".java")))
                //.process(new TestingMethodVisitor(), new CSVFile("dummy_report.csv"))
                //.process(new MethodVisitor(), new CSVFile("report.csv"))
                .process(new MethodVisitor(), new CSVEntryWriter(reportFilePath))
                .mine();
    }
}
