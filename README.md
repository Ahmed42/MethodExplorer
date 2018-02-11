# MethodExplorer
A tool for mining Java Github repositories. Specifically, it produces a CSV file containing commits that added parameters to existing methods.

Building and Usage
---------------
Maven can be used to produce a JAR by running the following in the root directory:
```
mvn clean compile assembly:single
```
To use, pass the repository URL and the report file name:
```
cd target
java -jar MethodExplorer.jar https://github.com/pathto/repository.git report_file_name_and_path 
```

Results and Performance
-----------------------
Results of testing on a few repos:

|Repository  | No of processed commits | Execution Time | Report File |
|------------|----------------------|----------------|-------------|
|[Motasim/Chat-System](https://github.com/Motasim/Chat-System)|18 commits|9 seconds|[chatsys_report.csv](dubbo_report.csv)|
|[paul-hammant/qdox](https://github.com/paul-hammant/qdox)|1136 commits|110 seconds|[qdox_report.csv](qdox_report.csv)|
|[alibaba/dubbo](https://github.com/alibaba/dubbo)|1595 commits|210 seconds|[dubbo_report.csv](dubbo_report.csv)|

The tool is slow. This could be because it parses all modified source files at each commit for methods. A better alternative might be to parse the diff blocks only instead of entire files.

Dependencies
------------
[RepoDriller](https://github.com/mauricioaniche/repodriller) A framework for mining git repositories. Makes traversing repositories' commits a breeze.

[QDox](https://github.com/paul-hammant/qdox) A parser for extracting classes, interfaces, and methods definitions.
