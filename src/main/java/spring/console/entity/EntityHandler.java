package spring.console.entity;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.boot.cli.command.options.OptionHandler;
import org.springframework.boot.cli.command.status.ExitStatus;
import org.springframework.util.StringUtils;
import spring.console.config.FileDirectory;
import spring.console.utils.Helper;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author NetoDevel
 * @since 0.0.1
 */
public class EntityHandler extends OptionHandler {

    @SuppressWarnings("unused")
    private OptionSpec<String> nameEntity;

    @SuppressWarnings("unused")
    private OptionSpec<String> parametersEntity;

    @Override
    protected void options() {
        this.nameEntity = option(Arrays.asList("nameEntity", "n"), "Name of entity").withRequiredArg();
        this.parametersEntity = option(Arrays.asList("parameterEntity", "p"), "Parameters of entity").withRequiredArg();
    }

    @Override
    public ExitStatus run(OptionSet options) throws Exception {

        Helper helper = new Helper();

        String nameClass = (String) options.valueOf("name");
        String parameters = (String) options.valueOf("field");

        if (nameClass == null || nameClass.replace(" ", "").isEmpty()) {

            nameClass = (String) options.valueOf("n");
            if (nameClass == null || nameClass.replace(" ", "").isEmpty()) {
                System.out.println("[INFO] - name of entity is required. use: -n ${entity_name}");
                return ExitStatus.ERROR;
            }
        }


        // Create empty entity file
        String rootDir = helper.findRoot(FileDirectory.DIR);
        File newFile = new File(rootDir + "/entity/"+StringUtils.capitalize(nameClass)+".java");
        boolean success = newFile.createNewFile();

        // Add the contents to the entity file
        BufferedWriter writer = new BufferedWriter(new FileWriter(rootDir + "/entity/"+nameClass+".java"));
        writer.write(createEntityScaffold(rootDir, nameClass, parameters));
        writer.close();

        // Add the dependencies to the pom.xml
        addDependencies();

        return ExitStatus.OK;
    }

    private String createEntityScaffold(String rootDir, String nameClass, String paramaters){

        String nameSpaceDirectory = StringUtils.replace(rootDir, FileDirectory.DIR+"/", "");
        nameSpaceDirectory = nameSpaceDirectory.replace('/', '.') + ".entity";

        StringBuilder fields = new StringBuilder();
        String[] parameters = paramaters.split(";");
        for(String s: parameters){
            String[] field = s.split(":");
            fields.append("\n\tprivate ").append(checkPrimitiveType(field[1])).append(" ").append(field[0]).append(";").append("\n");
        }

        return "package "+nameSpaceDirectory+";" +
                "\n" +
                "\n" +
                "import lombok.*;\n" +
                "import javax.persistence.*;\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "@Entity\n" +
                "@Setter\n" +
                "@Getter\n" +
                "@Table(name = \""+nameClass+"\")\n" +
                "@Builder\n" +
                "public class "+StringUtils.capitalize(nameClass)+" implements Serializable {\n" +
                "\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" + fields.toString() +
                "}";
    }

    private void addDependencies() throws IOException, XmlPullParserException {

        Model model = null;
        FileReader reader = null;
        MavenXpp3Reader mavenreader = new MavenXpp3Reader();
        reader = new FileReader(FileDirectory.ROOT_DIR + "/pom.xml"); // <-- pomfile is your pom.xml
        model = mavenreader.read(reader);


        // Add lombok as dependency
        Dependency lombokDependency = new Dependency();
        lombokDependency.setGroupId("org.projectlombok");
        lombokDependency.setArtifactId("lombok");
        lombokDependency.setOptional(true);
        model.removeDependency(lombokDependency);

        // Add lombok as dependency
        Dependency jpaDependency = new Dependency();
        jpaDependency.setGroupId("org.springframework.boot");
        jpaDependency.setArtifactId("spring-boot-starter-data-jpa");
        model.removeDependency(jpaDependency);

        model.addDependency(lombokDependency);
        model.addDependency(jpaDependency);

        reader.close();

        Writer writer2 = new FileWriter(FileDirectory.ROOT_DIR + "/pom.xml");
        MavenXpp3Writer pomWriter = new MavenXpp3Writer();
        pomWriter.write( writer2, model );
    }

    private String checkPrimitiveType(String type){

        String result = "";
        switch (type){
            case "string":
                result = "String";
                break;
            case "int":
            case "integer":
                result = "Int";
                break;
            case "bool":
            case "boolean":
                result = "Boolean";
                break;
        }

        return result;
    }


}
