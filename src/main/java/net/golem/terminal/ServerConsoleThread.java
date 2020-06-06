package net.golem.terminal;

public class ServerConsoleThread extends Thread {

	public ServerConsole console;

	public ServerConsoleThread(ServerConsole console) {
		this.console = console;
	}

	public void run() {
		console.start();
	}
}
