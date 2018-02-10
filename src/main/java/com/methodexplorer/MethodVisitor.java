package com.methodexplorer;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2/10/2018.
 */
public class MethodVisitor implements CommitVisitor {
    private Map<String, JavaMethod> Methods;

    public MethodVisitor() {
        Methods = new HashMap<String, JavaMethod>();
    }

    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        System.out.println("Currently processing: " + commit.getHash());
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
                            persistenceMechanism.write(
                                    commit.getHash(),
                                    oldMethodDetails.getCallSignature(),
                                    javaMethod.getCallSignature());
                            Methods.put(javaMethod.getName(), javaMethod);
                        }
                    }
                    else {
                        Methods.put(javaMethod.getName(), javaMethod);
                    }
                }
            }
        }
    }
}
