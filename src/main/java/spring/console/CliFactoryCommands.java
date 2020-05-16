package spring.console;

import spring.console.entity.ModelCommand;
import spring.console.entity.ModelHandler;
import org.springframework.boot.cli.command.Command;
import org.springframework.boot.cli.command.CommandFactory;
import java.util.Arrays;
import java.util.Collection;

public class CliFactoryCommands implements CommandFactory {

	@Override
	public Collection<Command> getCommands() {
		System.out.println("All of the commands");
//		ScaffoldInfoHelper scaffoldInfoHelper = new ScaffoldInfoHelper();

		//				new ModelCommand("entity", "generate entities", new ModelHandler(scaffoldInfoHelper)),
		//				new RepositoryCommand("repository", "generate repositories", new RepositoryHandler()),
		//				new ServiceCommand("service", "generate META-INF.services", new ServiceHandler()),
		//				new ControllerCommand("controller", "generate controllers", new ControllerHandler()),
		//				new ScaffoldCommand("scaffold", "generate api scaffold", new ScaffoldHandler()),
		//				new SetupScaffoldCommand("setup:scaffold", "setup scaffold", new SetupScaffoldHandler()),
		//				new TemplateCommand("template", "generate setup project", new TemplateHandler(scaffoldInfoHelper)));
		return Arrays.<Command>asList(
				new ModelCommand("model", "create:entity", new ModelHandler())
		);
	}

}