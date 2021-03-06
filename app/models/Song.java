package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Index;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class Song extends Model
{
	public static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	@Index(name = "tid")
	public String tid;

	@Lob
	public String title;

	@Lob
	public String artist;

	@Lob
	public String groups;

	@Lob
	public String keywords;

	public String type;

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

	static Pattern VOCALOID_PAT = Pattern.compile("(初音ミク|鏡音リン|鏡音レン|巡音ルカ|MEIKO|KAITO|GUMI|無印ミク)");

	static Pattern TOHO_PAT = Pattern.compile("(東方)");

	public static Matcher matcher(String input)
	{
		return p.matcher(input);
	}

	public static boolean matches(String input)
	{
		return p.matcher(input).find();
	}

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

	public static void loadAll(String path)
	{
		int i = 1;
		try
		{
			Logger.info("曲リストの読み込みを開始します。: %s", path);
			URLConnection con = new URL(path).openConnection();
//			con.setReadTimeout(10000);
//			con.setConnectTimeout(10000);
			con.connect();
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "Windows-31J"));
			while (true)
			{
				String input = r.readLine();
				if (input == null) break;
				
//				Matcher m = Song.matcher(line);
//				if (m.find())
				if (Song.matches(input))
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
					song.load(input);
				}

				if (i % 1000 == 0) Logger.info("%d loaded.", i);
				i++;
//				if (i == 10) break;
			}
			Logger.info("%,d 曲のデータが見つかりました。", i);
		}
		catch (IOException x)
		{
			throw new IllegalArgumentException(String.format("%,d 行目にエラーがあります。", i), x);
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
			type = "hot";
		}
		if (VOCALOID_PAT.matcher(text).find())
		{
//			Logger.info("ボカロ: %s", tid);
			isVocaloid = true;
			type = "vocaloid";
		}
		if (TOHO_PAT.matcher(text).find())
		{
//			Logger.info("東方: %s", tid);
			isToho = true;
			type = "toho";
		}
//		if (isHot || isVocaloid || isToho)
//		{
//			save();
//		}
	}

	public static List<Song> byKeywords(String keywords)
	{
		try
		{
			List<Song> songs = Song.find("byKeywordsIlike", "%" + keywords + "%").fetch();
			return songs;
		}
		catch (Exception x)
		{
			Logger.warn(x, "曲リストの準備中です。");
			return null;
		}
	}

	public static List<Song> byKeywords(String keywords, boolean isVocaloid, boolean isToho)
	{
		try
		{
			List<Song> songs = Song.find("byKeywordsIlikeAndIsVocaloidAndIsToho", "%" + keywords + "%", isVocaloid, isToho).fetch();
			return songs;
		}
		catch (Exception x)
		{
			Logger.warn(x, "曲リストの準備中です。");
			return null;
		}
	}
}
