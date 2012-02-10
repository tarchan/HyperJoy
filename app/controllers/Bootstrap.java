package controllers;

import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job
{
	static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	public void doJob()
	{
		// Check if the database is empty
//		if (User.count() == 0)
//		{
//			Fixtures.loadModels("initial-data.yml");
//		}
//		if (Song.count() == 0)
//		{
//			Song.load(HYPERJOY);
//		}
	}
}
