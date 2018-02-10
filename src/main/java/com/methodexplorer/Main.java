package com.methodexplorer;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.domain.*;
import org.repodriller.filter.commit.CommitFilter;
import org.repodriller.filter.commit.OnlyModificationsWithFileTypes;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRemoteRepository;
import org.repodriller.scm.SCMRepository;

import java.io.StringReader;
import java.util.*;

/**
 * Created by Admin on 2/9/2018.
 */
public class Main implements Study {
    public static void main(String[] args) {
        //JavaProjectBuilder builder = new JavaProjectBuilder();

        //JavaSource src = builder.addSource(new StringReader("public int test(int x)"));

        //JavaMethod javaMethod = src
        //System.out.println(javaClass.getName());
        new RepoDriller().start(new Main());
    }

    public void execute() {
        // https://github.com/ReactiveX/RxJava.git
        // https://github.com/Ahmed42/Chat-System.git
        // https://github.com/ome/minimal-omero-client.git
        // https://github.com/physikerwelt/MathMLQueryGenerator.git
        new RepositoryMining()
                .in(GitRemoteRepository.singleProject("https://github.com/physikerwelt/MathMLQueryGenerator.git"))
                .through(Commits.all())
                .filters(new OnlyModificationsWithFileTypes(Arrays.asList(".java")),
                        new MethodSignatureChangeFilter())
                .process(new DeveloperVisitor())
                .mine();
    }
}

class MethodSignatureChangeFilter implements CommitFilter {
    private Map<String, JavaMethod> Methods;

    public MethodSignatureChangeFilter() {
        Methods = new HashMap<String, JavaMethod>();
    }

    public boolean accept(Commit commit) {
        //System.out.println("Commit Hash: " + commit.getHash());
        for(Modification modification : commit.getModifications()) {
            // add or modify modifications only
            String code = modification.getSourceCode();

            JavaProjectBuilder builder = new JavaProjectBuilder();
            JavaSource src = builder.addSource(new StringReader(code));

            for(JavaClass javaClass : src.getClasses()) {
                List<JavaMethod> javaMethods = javaClass.getMethods();
                for(JavaMethod javaMethod : javaMethods) {
                    int currentNoOfParams = javaMethod.getParameters().size();
                    if(Methods.containsKey(javaMethod.getName())) {
                        JavaMethod oldMethodDetails = Methods.get(javaMethod.getName());
                        if (currentNoOfParams > oldMethodDetails.getParameters().size()) {
                            // record the old signature and the new
                            System.out.println("old: " + oldMethodDetails.getCallSignature() + ", new: " + javaMethod.getCallSignature());

                            Methods.put(javaMethod.getName(), javaMethod);
                        }
                    }
                    else {
                        Methods.put(javaMethod.getName(), javaMethod);
                    }
                }
            }


            /*System.out.println("Modification: " + modification.getFileName());
            System.out.println("===========");
            if(modification.getType() == ModificationType.MODIFY) {
                System.out.println("Source code:");
                System.out.println("===========");

                // get changed files
                //System.out.println(modification.getSourceCode());
                // get methods and params

                //System.out.println("File name: " + modification.getFileName());


                String diff = modification.getDiff();
                DiffParser parsedDiff = new DiffParser(diff);

                List<DiffLine> oldLines =  parsedDiff.getBlocks().get(0).getLinesInOldFile();
                List<DiffLine> newLines = parsedDiff.getBlocks().get(0).getLinesInNewFile();

                String oldBlock = "";
                for(DiffLine line : oldLines) {
                    //System.out.println(line.getLine());
                    oldBlock += "\n" + line.getLine();
                }

                System.out.println("old block: ");
                System.out.println("-----------");
                System.out.println(oldBlock);

                String newBlock = "";
                for(DiffLine line : newLines) {
                    //System.out.println(line.getLine());
                    newBlock += "\n" + line.getLine();
                }

                System.out.println("new block: " );
                System.out.println("-----------");
                System.out.println(newBlock);


            }*/
        }
        return false;
    }
}

class DeveloperVisitor implements CommitVisitor {

    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        //System.out.println(commit.getCommitter().getName());
    }
}