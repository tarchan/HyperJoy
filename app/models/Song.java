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
import javax.persistence.Lob;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Song extends Model
{
	public static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	public String tid;

	@Lob
	public String title;

	@Lob
	public String artist;

	@Lob
	public String groups;

	@Lob
	public String keywords;

	@Lob
	public String hot;

	public boolean isHot;

	public boolean isMedley;

	public boolean isVocaloid;

	public boolean isToho;

	static Pattern p = Pattern.compile("^<li>.*?([0-9]{5,})(.+)", Pattern.CASE_INSENSITIVE);

	static Pattern p2 = Pattern.compile("([0-9/]{5})?.*?([0-9,-]{5,})／([^／]+?)／([^／]+)／?(.*)$");

	static Pattern HOT_PAT = Pattern.compile("<FONT COLOR=\"#FF0000\"><B>");

//	static Pattern MEDLEY_PAT = Pattern.compile("<P CLASS=\"medley\">(.*?)</P>");
	static Pattern MEDLEY_PAT = Pattern.compile("<P CLASS=\"medley\">\\[メドレー曲目\\]<BR>(.*?)</P>");

	static Pattern VOCALOID_PAT = Pattern.compile("(初音ミク|鏡音リン|鏡音レン|巡音ルカ|MEIKO|KAITO|GUMI)");

	static Pattern TOHO_PAT = Pattern.compile("(東方)");

	public static Matcher matcher(String input)
	{
		return p.matcher(input);
	}

//	public static void loadAll(String path)
//	{
//		try
//		{
//			URLConnection con = new URL(path).openConnection();
////			con.setReadTimeout(10000);
////			con.setConnectTimeout(10000);
//			con.connect();
//			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "Windows-31J"));
//			int i = 0;
//			while (true)
//			{
//				String line = r.readLine();
//				if (line == null) break;
//				
//				Matcher m = p.matcher(line);
//				if (m.find())
//				{
////					Logger.info("[%d] %s", i, line);
//					
////					Matcher m2 = p2.matcher(line);
////					if (m2.find())
////					{
////						Song song = new Song();
////						song.hot = m2.group(1);
////						song.isHot = song.hot != null && song.hot.length() > 0;
////						song.tid = m2.group(2);
////						song.title = m2.group(3);
////						song.artist = m2.group(4);
////						song.groups = m2.group(5);
////						song.keywords = line;
////						song.save();
////					}
//					Song song = new Song();
//					song.load(line);
//				}
//
//				++i;
//				if (i % 1000 == 0) Logger.info("%d loaded.", i);
////				if (i == 10) break;
//			}
//			Logger.info("%d loaded.", i);
//		}
//		catch (IOException x)
//		{
//			throw new RuntimeException("エラー: " + path, x);
//		}
//	}

	public void load(String input)
	{
		keywords = input;
		input = input.replaceAll("&lt;", "<");
		input = input.replaceAll("&gt;", ">");
		Matcher m2 = p2.matcher(input);
		if (m2.find())
		{
//			hot = m2.group(1);
//			isHot = hot != null && hot.length() > 0;
			tid = m2.group(2);
			title = m2.group(3);
			artist = m2.group(4);
			groups = m2.group(5);
			medley(artist);
			medley(groups);
			setTag(input);
			save();
		}
		else
		{
//			Logger.info("曲番号未定: %s", line);
			return;
		}
	}

	public void upgrade()
	{
		load(keywords);
	}

	void medley(String text)
	{
		if (text == null) return;

		Matcher mm = MEDLEY_PAT.matcher(text);
		if (mm.find())
		{
//			Logger.info("メドレー: %s", tid);
			isMedley = true;
			groups = mm.group(1);
			artist = mm.replaceAll("");
//			save();
		}
	}

	void setTag(String text)
	{
		if (text == null) return;

		if (HOT_PAT.matcher(text).find())
		{
//			Logger.info("新曲: %s", tid);
			isHot = true;
		}
		if (VOCALOID_PAT.matcher(text).find())
		{
//			Logger.info("ボカロ: %s", tid);
			isVocaloid = true;
		}
		if (TOHO_PAT.matcher(text).find())
		{
//			Logger.info("東方: %s", tid);
			isToho = true;
		}
//		if (isHot || isVocaloid || isToho)
//		{
//			save();
//		}
	}
}
