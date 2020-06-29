package spring.console;

import spring.console.entity.EntityCommand;
import org.springframework.boot.cli.command.Command;
import org.springframework.boot.cli.command.CommandFactory;
import spring.console.entity.EntityHandler;

import java.util.Arrays;
import java.util.Collection;

public class CliFactoryCommands implements CommandFactory {

	@Override
	public Collection<Command> getCommands() {
		return Arrays.<Command>asList(
				new EntityCommand("console-create:entity", "Create a new entity", new EntityHandler())
		);
	}

}