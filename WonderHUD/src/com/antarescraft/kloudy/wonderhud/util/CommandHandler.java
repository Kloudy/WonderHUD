package com.antarescraft.kloudy.wonderhud.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler
{
	String subcommand();
	String description();
	String permission();
	int numArgs();
	String argsDescription();
	boolean mustBePlayer();
}