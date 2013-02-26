package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;

import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart(async=true)
@Every("24h")
public class Bootstrap extends Job
{
	public void doJob()
	{
		try
		{
			Logger.info("曲リストを初期化します。");
			// TODO 曲リストの更新を確認
			Song.find("").first();
			Site site = Site.find("byName", "hyperjoy").first();
			Logger.info("site=%s", site);
			if (site == null)
			{
				site = new Site("hyperjoy", "http://homepage1.nifty.com/yottoide/hyperjoy.html");
				site.save();
			}

			if (site.loading)
			{
				Logger.info("曲リストを読み込み中です。: %s", site.url);
				return;
			}

			// Check if the database is empty
//			if (User.count() == 0)
//			{
//				Fixtures.loadModels("initial-data.yml");
//			}
			if (Song.count() == 0)
			{
				site.beginLoading();
				Song.loadAll(site.url);
				site.endLoading();
//				loadAll(Song.HYPERJOY);
			}
			else
			{
				Logger.info("曲リストは初期化済みです。");
			}
		}
		catch (Exception x)
		{
			Logger.error(x, "曲リストの初期化を中止します。");
		}
	}
}
