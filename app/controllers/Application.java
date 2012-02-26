package controllers;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import models.*;

public class Application extends Controller
{
	static final String HYPERJOY = "http://homepage1.nifty.com/yottoide/hyperjoy.html";

	@Before
	public static void loadSongs()
	{
//		List<Song> songs = Song.findAll();
//		if (songs.size() == 0)
		if (Song.count() == 0)
		{
			Song.deleteAll();
			Song.load(HYPERJOY);
		}
	}

	public static void index0(String keyword)
	{
		render(keyword);
	}

	public static void index(String keyword)
	{
//		try
//		{
//			String q = request.querystring;
//			q = URLDecoder.decode(q, "utf-8");
//			Logger.info("querystring=%s", q);
//		}
//		catch (UnsupportedEncodingException x)
//		{
//		}

		long total = Song.count();
		List<Song> songs;
//		List<Song> songs = Song.findAll();
//		int total = songs.size();
		if (keyword != null && keyword.length() > 0)
		{
			songs = Song.find("keywords like ?", "%" + keyword + "%").fetch();
			for (Song song : songs)
			{
				song.upgrade();
			}
		}
		else
		{
			songs = null;
		}
		render(keyword, songs, total);
	}

	public static void anime(String keyword)
	{
		List<Song> songs = Song.find("isVocaloid is false and isToho is false and keywords like ?", "%" + keyword + "%").fetch();
		for (Song song : songs)
		{
			song.upgrade();
		}
		long total = Song.count("isVocaloid is false and isToho is false");
		render("@index", keyword, songs, total);
	}

	public static void vocaloid(String keyword)
	{
		List<Song> songs = Song.find("isVocaloid is true and keywords like ?", "%" + keyword + "%").fetch();
		for (Song song : songs)
		{
			song.upgrade();
		}
		long total = Song.count("isVocaloid is true");
		render("@index", keyword, songs, total);
	}

	public static void toho(String keyword)
	{
		List<Song> songs = Song.find("isToho is true and keywords Like ?", "%" + keyword + "%").fetch();
		for (Song song : songs)
		{
			song.upgrade();
		}
		long total = Song.count("isToho is true");
		render("@index", keyword, songs, total);
	}

	public static void findSong(String tag, String keyword)
	{
		if (tag.equals("anime"))
		{
			anime(keyword);
		}
		else if (tag.equals("vocaloid"))
		{
			vocaloid(keyword);
		}
		else if (tag.equals("toho"))
		{
			toho(keyword);
		}
		else
		{
			index(keyword);
		}
	}
}
