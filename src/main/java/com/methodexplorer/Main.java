package com.methodexplorer;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.domain.Commit;
import org.repodriller.filter.commit.OnlyModificationsWithFileTypes;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRemoteRepository;
import org.repodriller.scm.SCMRepository;

import java.util.Arrays;

/**
 * Created by Admin on 2/9/2018.
 */
public class Main implements Study {
    public static void main(String[] args) {
        new RepoDriller().start(new Main());
    }

    public void execute() {
        new RepositoryMining()
                .in(GitRemoteRepository.singleProject("https://github.com/Ahmed42/Chat-System.git"))
                .through(Commits.all())
                .filters(new OnlyModificationsWithFileTypes(Arrays.asList(".java")))
                .process(new DeveloperVisitor())
                .mine();
    }
}

class DeveloperVisitor implements CommitVisitor {

    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        System.out.println(commit.getCommitter().getName());
    }
}