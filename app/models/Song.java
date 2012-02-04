package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Song extends Model
{
	public String tid;

	public String title;

	public String artist;

	public String groups;

	public String keywords;

	public boolean isHot;

	static Pattern p = Pattern.compile("^<li>.*?([0-9]{5,})(.+)", Pattern.CASE_INSENSITIVE);

	static Pattern p2 = Pattern.compile("(\\[[0-9]{2}/[0-9]{2}\\])?.*?([0-9,-]{5,})／([^／]+?)／([^／]+)／?(.*)$", Pattern.CASE_INSENSITIVE);

	public static void load(String path)
	{
		try
		{
			URLConnection con = new URL(path).openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			con.connect();
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "Windows-31J"));
			int i = 0;
			while (true)
			{
				String line = r.readLine();
				if (line == null) break;
				
				Matcher m = p.matcher(line);
				if (m.find())
				{
					Logger.info("[%d] %s", i, line);
					
					Matcher m2 = p2.matcher(line);
					if (m2.find())
					{
						Song song = new Song();
						String hot = m2.group(1);
						song.isHot = hot != null;
						song.tid = m2.group(2);
						song.title = m2.group(3);
						song.artist = m2.group(4);
						song.groups = m2.group(5);
						song.keywords = line;
						song.save();
					}
				}

				++i;
				if (i % 1000 == 0) Logger.info("%d loaded.", i);
				if (i == 100) break;
			}
		}
		catch (IOException x)
		{
			throw new RuntimeException("エラー: " + path, x);
		}
	}
}
