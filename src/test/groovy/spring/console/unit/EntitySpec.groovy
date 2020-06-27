package spring.console.unit

import joptsimple.AbstractOptionSpec
import joptsimple.OptionParser
import joptsimple.OptionSet
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.building.DefaultModelBuildingRequest
import org.apache.maven.model.building.ModelBuildingException
import org.apache.maven.model.building.ModelBuildingRequest
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.StringUtils
import spock.lang.Specification
import spring.console.TestSpec
import spring.console.config.FileDirectory
import spring.console.entity.EntityHandler
import spring.console.utils.Helper

import java.lang.reflect.Array
import java.util.function.Consumer

@SpringBootTest(classes = [TestSpec.class])
class EntitySpec extends Specification {


    EntityHandler entityHandler

    @SpringBean
    Writer writer = Stub()

    def setup() {
        entityHandler = new EntityHandler()
    }

    def "run should create a new entity with the specific name"(){

        given:
        OptionParser parser = new OptionParser( "n:" )
        OptionSet options = parser.parse("-n", "user")

        when:
        entityHandler.run(options)

        then:
        File file = new File(FileDirectory.DIR + "/spring/console/entity/" + StringUtils.capitalize("user") + ".java")
        assert file.exists()
    }

    def "run should create a new entity with the specific name using --name"(){

        given:
        OptionParser parser = new OptionParser()
        parser.accepts("name").withRequiredArg()
        OptionSet options = parser.parse("--name", "user")

        when:
        entityHandler.run(options)

        then:
        File file = new File(FileDirectory.DIR + "/spring/console/entity/" + StringUtils.capitalize("user") + ".java")
        assert file.exists()
    }

    def "run should create a new entity with the specific fields"(){


        given:
        OptionParser parser = new OptionParser()
        parser.accepts("name").withRequiredArg()
        parser.accepts("field").withRequiredArg()
        OptionSet options = parser.parse("--name", "user", "--field", "name:string")

        when:
        entityHandler.run(options)

        then:
        File file = new File(FileDirectory.DIR + "/spring/console/entity/" + StringUtils.capitalize("user") + ".java")
        assert file.exists()
    }


    def "run should add dependencies to pom.xml file"(){

        given:
        OptionParser parser = new OptionParser( "n:" )
        OptionSet options = parser.parse("-n", "user")

        when:
        entityHandler.run(options)

        then:
        MavenXpp3Reader mavenreader = new MavenXpp3Reader()
        def reader = new FileReader(FileDirectory.ROOT_DIR + "/pom.xml")
        def model = mavenreader.read(reader)
        List<Dependency> dependencies = model.getDependencies()

        List<String> string = new ArrayList<String>()
        for(Dependency dependency : dependencies){
            if(dependency.getArtifactId() == "spring-boot-starter-data-jpa"){
                string.add("spring-boot-starter-data-jpa")
            } else if(dependency.getArtifactId() == "lombok"){
                string.add("spring-boot-starter-data-jpa")
            }
        }

        assert string.size() == 2
    }

}
