Running in Eclipse
------------------

1) Import
- File → Import → Maven → Existing Maven Projects → select the project root (`/Users/kevin/Documents/ElectronicStore`) → Finish.

2) JDK & Maven
- Make sure the project uses JDK 11 (Project → Properties → Java Compiler / Java Build Path).

3) JavaFX VM arguments
- Edit the launch configuration `ElectronicStore.launch` or create a Run Configuration for `electronicstore.Main` and set the VM arguments (replace the path):

```
--module-path /path/to/javafx-sdk-21/lib --add-modules=javafx.controls,javafx.fxml,javafx.graphics,javafx.base --add-opens=java.base/sun.misc=ALL-UNNAMED -Dprism.order=es2 -Dprism.verbose=false
```

4) Run
- Run → Run Configurations → Java Application → select `electronicstore.Main` (or import the `ElectronicStore.launch`) → Run.

Notes
- If you prefer Maven to manage the JavaFX modules, you can run the plugin goal instead:

```
cd /Users/kevin/Documents/ElectronicStore
mvn -DskipTests javafx:run
```

You should commit & push these files to the `main` branch. Example commands:

```
git add .project ElectronicStore.launch README_ECLIPSE.md
git commit -m "Add Eclipse project + launch config for easy running in Eclipse"
git push origin main
```
