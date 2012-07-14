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

@OnApplicationStart
public class Bootstrap extends Job
{
	public void doJob()
	{
		Logger.info("曲リストを初期化します。");
		// Check if the database is empty
//		if (User.count() == 0)
//		{
//			Fixtures.loadModels("initial-data.yml");
//		}
		if (Song.count() == 0)
		{
			loadAll(Song.HYPERJOY);
		}
		else
		{
			Logger.info("曲リストは初期化済みです。");
		}
	}

	public static void loadAll(String path)
	{
		try
		{
			Logger.info("曲リストの読み込みを開始します。: %s", path);
			URLConnection con = new URL(path).openConnection();
//			con.setReadTimeout(10000);
//			con.setConnectTimeout(10000);
			con.connect();
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "Windows-31J"));
			int i = 0;
			while (true)
			{
				String line = r.readLine();
				if (line == null) break;
				
				Matcher m = Song.matcher(line);
				if (m.find())
				{
//					Logger.info("[%d] %s", i, line);
					
//					Matcher m2 = p2.matcher(line);
//					if (m2.find())
//					{
//						Song song = new Song();
//						song.hot = m2.group(1);
//						song.isHot = song.hot != null && song.hot.length() > 0;
//						song.tid = m2.group(2);
//						song.title = m2.group(3);
//						song.artist = m2.group(4);
//						song.groups = m2.group(5);
//						song.keywords = line;
//						song.save();
//					}
					Song song = new Song();
					song.load(line);
				}

				++i;
				if (i % 1000 == 0) Logger.info("%d loaded.", i);
//				if (i == 10) break;
			}
			Logger.info("%,d 曲のデータが見つかりました。", i);
		}
		catch (IOException x)
		{
			throw new RuntimeException("エラー: " + path, x);
		}
	}
}
