package com.methodexplorer;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.io.StringReader;

/**
 * Created by Admin on 2/10/2018.
 */

// Just list methods .. only for testing
public class TestingMethodVisitor implements CommitVisitor {
    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        String hash = commit.getHash();
        String code = commit.getModifications().get(0).getSourceCode();

        JavaProjectBuilder builder = new JavaProjectBuilder();
        JavaSource src = builder.addSource(new StringReader(code));

        JavaMethod javaMethod = src.getClasses().get(0).getMethods().get(0);
        persistenceMechanism.write(hash, javaMethod.getCallSignature());
    }
}
