package me.wheezygold.antiRooktube.skript;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.wheezygold.antiRooktube.common.JsonReader;

import java.io.IOException;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONException;

public class ExprRookSpigot extends SimpleExpression<String> {

  @Override
  @Nullable
  protected String[] get(Event e) {
	  String spigotname = null;
		try {
			String[] args = null;
			spigotname = JsonReader.grabRookSpigot(args);
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	if (spigotname != null) {
		return new String[] {spigotname};
	}
	return null;
  }

  @Override
  public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
    return true;
  }

  @Override
  public Class<? extends String> getReturnType() {
    return String.class;
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public String toString(@Nullable Event e, boolean b) {
    return getClass().getName();
  }
}