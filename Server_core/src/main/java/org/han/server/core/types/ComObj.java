package org.han.server.core.types;

import java.util.function.Consumer;

import org.han.server.core.types.FunctionalInterfaces.AccCheck;
import org.han.server.core.types.FunctionalInterfaces.Check;
import org.han.server.core.types.FunctionalInterfaces.tpeCheck;

public class ComObj{
	private final Consumer<Msg> run;
	private final String disc;
	private final ComBase CB;
	private String parameters = "";
	private Check hide = f -> false;
	private Check[] can_Invoke = { f -> true };
	private Check type = f -> true;

	public ComObj(Consumer<Msg> run, String disc,ComBase CB) {
		this.run = run;
		this.disc = disc;
		this.CB = CB;
	}
	public ComObj hide() {
		hide = f -> true;
		return this;
	}
	public ComObj hide(Check chk) {
		hide = chk;
		return this;
	}
	public ComObj setType(tpeCheck chk) {
		type = chk;
		return this;
	}
	
	public ComObj setGuild() {
		type = tpeCheck.Guild;
		return this;
	}
	
	public ComObj setPrivate() {
		type = tpeCheck.Private;
		return this;
	}
	public ComObj setPar(String par) {
		parameters = par;
		return this;
	}
	public ComObj setPerms(AccCheck... checks) {
		can_Invoke = checks;
		return this;
	}
	
	public String getPar() {
		return parameters;
	}
	
	public boolean isHidden(Msg input) {
		return hide.chk(input);
	}
	
	public boolean hasPerms(Msg input) {
		for (Check chk : can_Invoke) {
			if (chk.chk(input)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the type
	 */
	
	public boolean isrightplace(Msg input) {
		return type.chk(input);
	}
	
	public ComBase getComBase() {
		// TODO Auto-generated method stub
		return CB;
	}
	
	public String getDisc() {
		return disc;
	}
	public Consumer<Msg> getRun() {
		return run;
	}

}